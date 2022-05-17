package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IncludeAideModel {
    @ValueMapValue
    private String helpUrl;

    @ValueMapValue
    private String helpUrlSecured;

    @Inject
    private ResourceResolver resourceResolver;

    public String getHelpUrl() {
        return ModelUtils.getVanityOfPageIfExists(helpUrl, resourceResolver);
    }

    public String getHelpUrlSecured() {
        return ModelUtils.getVanityOfPageIfExists(helpUrlSecured, resourceResolver);
    }
}
