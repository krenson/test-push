package com.leforemhe.aem.site.core.services;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.leforemhe.aem.site.core.models.Constants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import java.util.List;

@Component(
        service = ImageService.class,
        immediate = true
)
public class ImageService {
    public String getImageRendition(String imagePath, ResourceResolver resourceResolver) {
        Resource damResource = resourceResolver.resolve(imagePath);
        if (damResource != null) {
            Asset damAsset =  damResource.adaptTo(Asset.class);
            if (damAsset != null) {
                List<Rendition> renditions = damAsset.getRenditions();
                if (!renditions.isEmpty()) {
                    for (Rendition rendition : renditions) {
                        if (rendition.getName().contains(Constants.IMAGE_RENDITION_MARKER)) {
                            return rendition.getPath();
                        }
                    }
                }
            }
        }
        return imagePath + Constants.FALLBACK_RENDITION;
    }
}
