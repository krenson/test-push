package com.leforemhe.aem.site.core.services;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

@Component(
        service = ImageService.class,
        immediate = true
)
public class ImageService {
    public String getImageRendition(String imagePath) {
        return imagePath + "/jcr:content/renditions/cq5dam.web.1280.1280.jpeg";
    }
}
