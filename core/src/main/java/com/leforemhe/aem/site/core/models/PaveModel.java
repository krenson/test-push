package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.designer.Style;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.inject.Named;

/**
 * Sling model of a Pavé component.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PaveModel {
    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    @Default(values = "")
    private String title;

    @ValueMapValue
    @Default(values = "")
    private String description;

    @ValueMapValue
    @Default(values = "")
    private String accroche;

    @ValueMapValue(name = "fileReference")
    private String imageSrc;

    @ChildResource
    @Named("reference")
    private NavigationItemModel reference;

    @ScriptVariable
    private Style currentStyle;

    public String getTitle() {
        return title;
    }

    public NavigationItemModel getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public String getAccroche() {
        return accroche;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getColor() { return (String) currentStyle.getOrDefault("color", "arrowLink"); }
}
