package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Sling model of Banniere carrousel component.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BanniereCarrouselModel {
    private final String title;
    private final String description;
    private final String image;
    private final String imageAlt;
    private final String body;
    private final NavigationItemModel reference;

    /**
     * Public constructor taking the fields as parameter.
     * Eases the mocking process for the tests.
     * @param title title
     * @param description description
     * @param image image
     * @param imageAlt imageAlt
     * @param body body
     * @param reference reference
     */
    @Inject
    public BanniereCarrouselModel(@ValueMapValue(name="title") String title,
                                  @ValueMapValue(name="description") String description,
                                  @ValueMapValue(name="fileReference") String image,
                                  @ValueMapValue(name="descImage") String imageAlt,
                                  @ValueMapValue(name="body") String body,
                                  @ChildResource @Named("reference") NavigationItemModel reference) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.imageAlt = imageAlt;
        this.body = body;
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public String getBody() {
        return body;
    }

    public NavigationItemModel getReference() {
        return reference;
    }
}
