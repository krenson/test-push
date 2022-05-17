package com.leforemhe.aem.site.core.services;

import com.adobe.granite.asset.api.AssetException;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.day.cq.wcm.api.WCMMode;
import com.leforemhe.aem.site.core.config.JavascriptConfig;
import com.leforemhe.aem.site.core.models.pojo.BinaryImpl;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service to write Javascript into JS files
 */
@Component(
        service = JavascriptToFileService.class,
        immediate = true
)
public class JavascriptToFileService {
    private static final String ALL_ENVIRONMENTS = "all";
    private static final String FILE_EXTENSION = ".js";
    private static final String FILENAME_PROPERTY = "filename";
    private static final String ENVIRONMENT_PROPERTY = "environment";
    private static final String JCR_CONTENT = JcrConstants.JCR_CONTENT;
    private static final String METADATA = "metadata";

    private static final Logger LOG = LoggerFactory.getLogger(JavascriptToFileService.class);

    @Reference
    private RequestResponseFactory requestResponseFactory;
    @Reference
    private SlingRequestProcessor requestProcessor;
    @Reference
    private DynamicJavascriptConfigService dynamicJavascriptConfigService;
    @Reference
    private GlobalConfigService globalConfigService;

    /**
     * Create a JS file
     * @param path path where to fetch the Javascript
     * @param componentProperties properties of the Javascript
     * @param resourceResolver CRX Resource Resolver
     */
    public void createJsFile(String folderName, String path, ValueMap componentProperties, ResourceResolver resourceResolver){
        LOG.debug("JavascriptToFileService: Inside createJsFile");
        LOG.debug("Creating javascript file at {}", path);
        JavascriptConfig config = dynamicJavascriptConfigService.getConfig();
        try {
            if(resourceResolver==null){
                LOG.error("Unable to get resourceResolver");
                return;
            }
            String jsName = componentProperties.get(FILENAME_PROPERTY, String.class);
            String jsEnvironment = componentProperties.get(ENVIRONMENT_PROPERTY, String.class);
            if(jsName==null){
                LOG.error("Unable to parse javascript file name for '{}'", path);
                return;
            }
            LOG.debug("jsName: {}", jsName);

            String jsFileName = String.format("%s%s%s",
                    jsName,
                    (ALL_ENVIRONMENTS.equals(jsEnvironment) ? "":("."+jsEnvironment)),
                    FILE_EXTENSION
            );
            LOG.debug("jsFileName: {}", jsFileName);
            LOG.debug("path: {}", path);

            String requestPath = String.format("%s.%s", path, "html");
            LOG.debug("requestPath: {}", requestPath);

            AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
            if(assetManager==null){
                LOG.error("Unable to adapt ResourceResolver to AssetManager");
                return;
            }
            LOG.debug("javascriptFolderPath: {}", config.dynamicJavascriptFolderPath());

            String content = getContent(requestPath, resourceResolver)
                    .replace("<script>", "")
                    .replace("</script>", "");
            LOG.debug("content: {}", content);

            Resource javascriptAssetFolder = createOrGetJavascriptFolder(resourceResolver, config.dynamicJavascriptFolderPath(), folderName);
            if(javascriptAssetFolder != null){
                Asset createdAsset = assetManager.createOrUpdateAsset(javascriptAssetFolder.getPath() + "/" + jsFileName,
                        new BinaryImpl(content.getBytes()),
                        "application/javascript", true);
                addPropertiesToAsset(path, resourceResolver, createdAsset, componentProperties);
                LOG.debug("Asset was created: {} with file name: {}", config.dynamicJavascriptFolderPath(), jsFileName);
            } else {
                LOG.error("Could not find or create javascript folder for {}", jsFileName);
            }
        } catch (IOException | AssetException | ServletException | RepositoryException e) {
            LOG.error("Error while creating js file: {}", e.getMessage());
        }
    }

    private void addPropertiesToAsset(String expFragmentResourcePath, ResourceResolver resourceResolver, Asset createdAsset, ValueMap componentProperties) throws RepositoryException, PersistenceException {
        Resource efResource = resourceResolver.getResource(expFragmentResourcePath);
        boolean authorInstance = BooleanUtils.toBooleanDefaultIfNull(componentProperties.get("authorinstance", Boolean.class), false);
        if(createdAsset != null){
            String metadataPath = globalConfigService.getConfig().pathSeparator() + JCR_CONTENT + globalConfigService.getConfig().pathSeparator() + METADATA;
            Resource resource = resourceResolver.getResource(createdAsset.getPath() + metadataPath);
            Node metadataNode = resource.adaptTo(Node.class);
            if(metadataNode != null){
                clearConfigForPageRestrict(metadataNode);
                JcrUtil.setProperty(metadataNode, "authorinstance", authorInstance);
                if(efResource.getChild("pagesRestrict") != null){
                    JcrUtil.copy(efResource.getChild("pagesRestrict").adaptTo(Node.class), metadataNode, "pagesRestrict");
                }
            }
            resourceResolver.commit();
        }
    }

    private void clearConfigForPageRestrict(Node metadata){
        try {
            if(metadata.hasNode("pagesRestrict")){
                metadata.getNode("pagesRestrict").remove();
            }
        } catch (RepositoryException e) {
            LOG.error("Problem when trying to remove PageRestrict from asset: {}", e.getMessage());
        }
    }

    private String getContent(String requestPath, ResourceResolver resourceResolver) throws ServletException, IOException {
        LOG.debug("JavascriptToFileService: Inside getContent");
        LOG.debug("Fetching content [HTTP]: {}", requestPath);
        HttpServletRequest req = requestResponseFactory.createRequest(org.apache.sling.api.servlets.HttpConstants.METHOD_GET, requestPath);
        WCMMode.DISABLED.toRequest(req);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpServletResponse resp = requestResponseFactory.createResponse(out);
        requestProcessor.processRequest(req, resp, resourceResolver);
        return out.toString();
    }

    private Resource createOrGetJavascriptFolder(ResourceResolver resourceResolver, String javascriptFolder, String folderName){
        Resource javascriptFolderRes = resourceResolver.getResource(javascriptFolder);
        Resource folderRes = resourceResolver.getResource(javascriptFolder + folderName);
        if(folderRes == null){
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put(JcrConstants.JCR_PRIMARYTYPE, JcrResourceConstants.NT_SLING_FOLDER);
            try {
                folderRes = resourceResolver.create(javascriptFolderRes, folderName, valueMap);
            } catch (PersistenceException e) {
                LOG.error("Error creating or getting the JS folder: {}", e.getMessage());
            }
        }
        return folderRes;
    }
}
