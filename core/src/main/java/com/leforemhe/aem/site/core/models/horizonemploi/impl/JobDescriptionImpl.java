package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.models.Text;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.horizonemploi.JobDescription;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        JobDescription.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = JobDescriptionImpl.RESOURCE_TYPE)
public class JobDescriptionImpl implements JobDescription {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/contentfragments/text";

    @Self
    @Via(type = ResourceSuperType.class)
    private Text text;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private Page currentPage;

    private Job job;

    @Override
    public String getText() {
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if (this.job == null && !inExperienceFragment) {
            String cleMetier = currentPage.getProperties().get(Constants.CLE_METIER).toString();
            job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        return job.getDescription();
    }

}
