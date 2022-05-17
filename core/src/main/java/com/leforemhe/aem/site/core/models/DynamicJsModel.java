package com.leforemhe.aem.site.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sling model of Dynamic Javascript Component
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicJsModel {

    private static final String JCR_PRIMARYTYPE = JcrConstants.JCR_PRIMARYTYPE;
    private static final String DAM_ASSET = DamConstants.NT_DAM_ASSET;
    private static final String JCR_CONTENT = JcrConstants.JCR_CONTENT;
    private static final String METADATA = "metadata";

    private final ResourceResolver resourceResolver;
    private final List<String> javascripts;
    private final Page currentPage;

    private final GlobalConfigService globalConfigService;

    private static final Logger LOG = LoggerFactory.getLogger(DynamicJsModel.class);

    /**
     * Public constructor taking the fields as parameter.
     *
     * @param resourceResolver     CRX Resource Resolver
     * @param javascripts          List of dynamic javascript experience fragments
     */
    @Inject
    public DynamicJsModel(@SlingObject ResourceResolver resourceResolver, @ValueMapValue(name = "javascripts") List<String> javascripts, @ScriptVariable(name = "currentPage") Page currentPage, GlobalConfigService globalConfigService) {
        LOG.debug("Inside DynamicJsModel");
        this.resourceResolver = resourceResolver;
        this.currentPage = currentPage;
        this.globalConfigService = globalConfigService;
        this.javascripts = javascripts != null ? javascripts : new ArrayList<>();
    }

    /**
     * @return Size of the experience fragments configured
     */
    public int getSize() {
        return javascripts.size();
    }

    /**
     * @return Paths of the javascript file to include
     */
    public Set<String> getJavascriptFilePaths() {
        LOG.debug("Inside getJavascriptFilePaths");
        Set<String> jsPaths = new LinkedHashSet<>();
        LOG.debug("Process all dynamic JS configured via EF footer or page template...");
        LOG.debug("Javascripts: {}", javascripts);
        javascripts.forEach(js -> {
            LOG.debug("Resource resolver with {}", js);
            Resource javascriptFolder = resourceResolver.getResource(js);
            if (javascriptFolder == null) {
                LOG.error("Dynamic JavaScript experience fragment resource is null");
            } else {
                LOG.debug("Dynamic JavaScript name: {}", javascriptFolder.getName());
                Resource jsAsset = getAsset(javascriptFolder);
                if (jsAsset != null && addAssetToPage(jsAsset)) {
                    jsPaths.add(StringUtils.appendIfMissingIgnoreCase(jsAsset.getPath(), ".js"));
                }
            }
        });
        LOG.debug("Number of Dynamic JavaScript: {}", jsPaths.size());
        return jsPaths;
    }

    /**
     * @param resource to add
     * @return true if everything was okay
     */
    private boolean addAssetToPage(Resource resource) {
        LOG.debug("Inside addAssetToPage");
        if (resource != null && resource.getValueMap().get(JCR_PRIMARYTYPE, String.class).equals(DAM_ASSET)) {
            LOG.debug("Trying to add asset to page...");
            String metadataPath = globalConfigService.getConfig().pathSeparator() + JCR_CONTENT + globalConfigService.getConfig().pathSeparator() + METADATA;
            Resource metaData = resourceResolver.getResource(resource.getPath() + metadataPath);
            if (metaData != null) {
                LOG.debug("metaData is not NULL");
                boolean hideOnAuthor = BooleanUtils.toBooleanDefaultIfNull(metaData.getValueMap().get("authorinstance", Boolean.class), false);
                LOG.debug("Check if Asset has page restrictions configured");
                if (metaData.getChild("pagesRestrict") != null && !checkRestriction(metaData)) {
                    return false;
                }
                LOG.debug("{} - If {} contains author runmode ({}) and hideOnAuthor option is enabled ({})", resource.getPath(), globalConfigService.getConfig().runMode(), globalConfigService.getConfig().runMode().contains("author"), hideOnAuthor);
                if (globalConfigService.getConfig().runMode().contains("author") && hideOnAuthor) {
                    LOG.debug("{} is not injected", resource.getPath());
                    return false;
                }
                LOG.debug("{} is injected", resource.getPath());
            }
        }
        return true;
    }

    private boolean checkRestriction(Resource metaData) {
        LOG.debug("Page restrictions configured");
        Resource pagesRestrictRes = metaData.getChild("pagesRestrict");
        for (Resource pageRestrictItem : pagesRestrictRes.getChildren()) {
            JsConfigModel.PageRestriction restriction = pageRestrictItem.adaptTo(JsConfigModel.PageRestriction.class);
            if (restriction.includeChildren() && currentPage.getPath().startsWith(restriction.getPath())) {
                return true;
            } else if (!restriction.includeChildren() && currentPage.getPath().equals(restriction.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param javascriptFolder to get
     * @return js asset
     */
    private Resource getAsset(Resource javascriptFolder) {
        LOG.debug("Inside getAsset");
        Resource jsAsset = getSpecificRunmodeAsset(javascriptFolder);
        if (jsAsset == null && javascriptFolder.getChild(javascriptFolder.getName() + ".js") != null) {
            LOG.debug("javascriptFolder.getChild is running...");
            jsAsset = javascriptFolder.getChild(javascriptFolder.getName() + ".js");
        }
        return jsAsset;
    }

    /**
     * @param javascriptFolder to get
     * @return js asset for specific runmode
     */
    private Resource getSpecificRunmodeAsset(Resource javascriptFolder) {
        LOG.debug("Inside getSpecificRunmodeAsset");
        for (Resource javascriptAsset : javascriptFolder.getChildren()) {
            if (javascriptAsset.getName().endsWith(".js") && isCurrentRunMode(javascriptAsset.getName())) {
                LOG.debug("JavascriptAsset will be injected");
                return javascriptAsset;
            }
        }
        LOG.debug("JavascriptAsset will NOT be injected");
        return null;
    }

    /**
     * @param fileName Name of the file to extract the runmode
     * @return Run mode on which the file should be included
     */
    private static String extractRunMode(String fileName) {
        LOG.debug("Inside extractRunMode");

        final Pattern pattern = Pattern.compile("\\.(\\w*)\\.js");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            LOG.debug("Extract runmode from the file: {}", fileName);
            return matcher.group(1);
        }
        return null;
    }

    /**
     * @param fileName Name of the file
     * @return true if the current run mode is the one from the file
     */
    private boolean isCurrentRunMode(String fileName) {
        LOG.debug("Inside isCurrentRunMode");

        String env = extractRunMode(fileName);
        LOG.debug("Runmode extracted (null means dynamicJS is used for ALL environments): {}", env);
        LOG.debug("Runmode running: {}", globalConfigService.getConfig().runMode());
        return env == null || globalConfigService.getConfig().runMode().contains(env);
    }
}
