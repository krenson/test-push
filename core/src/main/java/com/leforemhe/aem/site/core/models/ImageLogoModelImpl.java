package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {ImageLogoModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ImageLogoModelImpl.RESOURCE_TYPE)
public class ImageLogoModelImpl implements ImageLogoModel {
    public static final String RESOURCE_TYPE = "leforemhev1/components/site/header/image";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    private Image image;

    @ValueMapValue(name = "fileReferenceDesktop")
    private String imageSrcDesktop;

    @ValueMapValue(name = "fileReferenceMobile")
    private String imageSrcMobile;

    @Override
    public String getImageSrcDesktop() { return this.imageSrcDesktop; }

    @Override
    public String getImageSrcMobile() {
        return this.imageSrcMobile;
    }

    @Override
    public Link getImageLink() {
        return this.image.getImageLink();
    }
}
