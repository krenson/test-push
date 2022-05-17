package com.leforemhe.aem.site.core.services;

import com.day.cq.dam.api.AssetManager;
import com.leforemhe.aem.site.core.models.pojo.BinaryImpl;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;

/**
 * Service to export content to SFTP server
 */
@Component(
        service = ExportAssetsService.class,
        immediate = true
)
public class ExportAssetsService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportAssetsService.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private GlobalConfigService globalConfigService;

    /**
     * Export using directly a Export Configuration
     * The purpose of this method is expose the EF via http
     * @param content Content to export
     * @param filename Filename of the content
     * @param apiPath API path to copy the content
     */
    public void exportFileFromConfig(String content, String filename, String apiPath) {

        LOG.debug("Inside exportFileFromConfig");
        try (ResourceResolver resourceResolver = ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser())){
            if (resourceResolver == null) {
                throw new RepositoryException("ServiceUtils.getResourceResolver() method return null");
            }
            AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
            if(assetManager==null){
                LOG.error("Unable to adapt ResourceResolver to AssetManager");
                return;
            }
            String filePath = apiPath + '/' + filename;
            LOG.debug("filePath: {}", filePath);
            assetManager.createOrUpdateAsset(filePath,
                    new BinaryImpl(content.getBytes()),
                    "text/html", true);
            LOG.debug("Asset was created: {}", filePath);
        }
        catch(RepositoryException e){
            LOG.error("Error while treating events: {}", e.getMessage());
        }
    }
}