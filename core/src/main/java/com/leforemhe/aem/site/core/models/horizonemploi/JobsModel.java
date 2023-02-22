package com.leforemhe.aem.site.core.models.horizonemploi;

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
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JobsModel {

    @Inject
    private Page currentPage;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @ValueMapValue
    private String selectedJobElement;

    @ValueMapValue
    private String placeholderEmptyJobsList;

    @Self
    private SlingHttpServletRequest request;

    private Job job;
    private List<Job> jobs;

    public List<Job> getJobs() {
        if (jobs == null) {
            jobs = new ArrayList<>();
            boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
            if (this.job == null && !inExperienceFragment) {
                String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
                this.job = contentFragmentUtilService.getJobFromJobID(cleMetier, true, true, request);
                if (job != null) {
                    jobs = job.getJobsFromElement(selectedJobElement);
                }
            }
            return jobs;
        } else {
            return jobs;
        }
    }

    public String getPlaceholderEmptyJobsList() {
        return placeholderEmptyJobsList;
    }

}