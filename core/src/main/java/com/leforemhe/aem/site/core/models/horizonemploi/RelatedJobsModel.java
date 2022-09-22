package com.leforemhe.aem.site.core.models.horizonemploi;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RelatedJobsModel {

    @Inject
    private Page currentPage;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    private Job job;

    public List<Job> getRelatedJobs() {
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if (this.job == null && !inExperienceFragment) {
            String cleMetier = currentPage.getProperties().get(Constants.CLE_METIER).toString();
            this.job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        return job.getRelatedJobs();
    }

}