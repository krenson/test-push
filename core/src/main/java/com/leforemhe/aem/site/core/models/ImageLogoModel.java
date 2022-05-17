package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Image;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ImageLogoModel extends Image {
    public String getImageSrcDesktop();
    public String getImageSrcMobile();
}
