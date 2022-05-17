package com.leforemhe.aem.site.core.services;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.leforemhe.aem.site.core.models.ModelUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Rich Text Editor Configuration Service for returning the Vanity URL of Hyperlinks
 */
@Component(
        immediate = true,
        service = RteHyperlinkService.class
)
public class RteHyperlinkService {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder builder;

    @Reference
    private GlobalConfigService globalConfigService;

    private static final Logger LOG = LoggerFactory.getLogger(RteHyperlinkService.class);

    /**
     * Get the vanity path of the resource at the given path if it is a page and its vanity url is configured.
     * Otherwise return the same given path. (Assets should not return a vanity path, because otherwise when moving
     * the asset the hyperlink will be broken.
     *
     * @param path The given path to get the vanity path from.
     * @return The vanity path if the given path is the path of a page (and it is configured), otherwise the given path.
     */
    public String getVanityPathIfNotAsset(@NotNull String path) {
        if (ModelUtils.isAssetLinkFullUrl(path, globalConfigService.getConfig().assetsPath())) {
            // Return same path (not vanity path)
            return path;
        }
        Page page = null;
        if (path != null) {
            page = getPageForPath(path);
        }
        if (page != null) {
            String vanity = page.getProperties().get(NameConstants.PN_SLING_VANITY_PATH, String.class);
            if (vanity == null) {
                LOG.debug("No vanity url configured for page on path: {}", path);
                return path;
            }
            return vanity;
        }

        LOG.debug("Page or asset not found on path {}", path);
        return path;
    }

    public String getFullPath(@NotNull String vanityPath) {
        var resourceResolver = ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser());
        if (resourceResolver != null) {
            var session = resourceResolver.adaptTo(Session.class);
            String FullPathFromVanityPath = getFullPathFromVanityPath(vanityPath, session, resourceResolver);
            resourceResolver.close();
            return FullPathFromVanityPath;
        }

        LOG.debug("Could not resolve '{}' system user", globalConfigService.getConfig().systemUser());
        return vanityPath;
    }

    /**
     * Get page for given path
     *
     * @param path path to get the page from
     * @return page for given path
     */
    private Page getPageForPath(String path) {
        var resourceResolver = Objects.requireNonNull(ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser()));
        var pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = Objects.requireNonNull(pageManager).getPage(path);
        return page;
    }

    /**
     * Get the full path of the given vanity path if exists, otherwise null.
     * If multiple options are possible, only the first hit is returned.
     *
     * @param vanityPath The vanity path of the full path of the page to return.
     * @param session    The session of the service user
     * @return full path of given vanity path if exists, otherwise null.
     */
    private String getFullPathFromVanityPath(String vanityPath, Session session, ResourceResolver resourceResolver) {
        // create query description as hash map (simplest way, same as form post)
        Map<String, String> map = new HashMap<>();

        // create query description as hash map (simplest way, same as form post)
        map.put("path", "/content/leforemhe/fr");
        map.put("type", NameConstants.NT_PAGE);
        map.put("1_property", "jcr:content/sling:vanityPath");
        map.put("1_property.value", vanityPath);

        var query = builder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();
        
        if(result.getHits().isEmpty()){
            map.replace("path", "/content/dam");
            map.replace("type", "dam:Asset");
            query = builder.createQuery(PredicateGroup.create(map), session);
            result = query.getResult();
        }
        try {
            return result.getHits().get(0).getPath();
        } catch (IndexOutOfBoundsException | RepositoryException e1) {            
            String fullPath = ModelUtils.getFullUrlForAssetLink(vanityPath, globalConfigService.getConfig().assetsPath());
            var asset = resourceResolver.getResource(fullPath);
            if (asset != null) {
                return fullPath;
            } else {
                LOG.error("No full path url found for vanity url: {}", vanityPath);
                return vanityPath;
            }
        }
    }

    public String getVanityPathAsset(String path) {
        var resourceResolver = Objects.requireNonNull(ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser()));
        Resource asset = resourceResolver.getResource(path);
        if(asset != null){
            return StringUtils.defaultIfEmpty(asset.getChild("jcr:content").getValueMap().get("sling:vanityPath", String.class), path);
        }
        return path;
    }
}
