package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.leforemhe.aem.site.core.models.horizonemploi.LinkModel;

import javax.inject.Inject;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, adapters = {
        LinkModel.class}, resourceType = LinkModelImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModelImpl implements LinkModel {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/linkicon";

    @Inject
    private Page currentPage;

    @SlingObject
    private Resource currentResource;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String iconFileReference;

    @ValueMapValue
    private String popupReference;

    private Job job;

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

    @Override
    public String popupReference() {
        return popupReference;
    }

    @Override
    public int getGeneratedId() {
        return currentResource.getPath().hashCode();
    }

    @Override
    public String getJobTitle() {
        String jobTitle = StringUtils.EMPTY;
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if (this.job == null && !inExperienceFragment) {
            String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
            job = contentFragmentUtilService.getJobFromJobID(cleMetier);
            jobTitle = job.getTitle();
        }
        return jobTitle;
    }
}
