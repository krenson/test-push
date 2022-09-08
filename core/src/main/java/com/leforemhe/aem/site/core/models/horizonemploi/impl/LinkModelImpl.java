package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.leforemhe.aem.site.core.models.horizonemploi.LinkModel;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        LinkModel.class }, resourceType = LinkModelImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModelImpl implements LinkModel {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/linkicon";

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String iconFileReference;

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getIcon() {
        return this.iconFileReference;
    }

    @Override
    public boolean isConfigured() {
        return StringUtils.isNotEmpty(title);
    }

}