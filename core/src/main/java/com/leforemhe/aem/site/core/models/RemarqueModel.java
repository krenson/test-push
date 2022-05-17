package com.leforemhe.aem.site.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Named;

/**
 * Sling model of the Remarque Component
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RemarqueModel {
    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = JcrConstants.JCR_TITLE)
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue(name = "fileReference")
    private String imageSrc;

    @ChildResource
    @Named("reference")
    private NavigationItemModel reference;

    @ValueMapValue
    private String color;

    public String getTitle() {
        return title;
    }

    public NavigationItemModel getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getColor() {
        return color;
    }
}
