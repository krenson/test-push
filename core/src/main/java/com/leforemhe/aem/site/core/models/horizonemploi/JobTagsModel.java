package com.leforemhe.aem.site.core.models.horizonemploi;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;

import java.util.List;

/**
 * Sling model for the etiquette component.
 */
@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JobTagsModel {

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private Page currentPage;

    @Self
    private SlingHttpServletRequest request;

    private Job job;

    public List<JobTag> getTagsCF() {
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if (this.job == null && !inExperienceFragment) {
            String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
            this.job = contentFragmentUtilService.getJobFromJobID(cleMetier, request);
        }
        return job.getLabels();
    }
}
