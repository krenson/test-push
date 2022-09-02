package com.leforemhe.aem.site.core.models.horizonemploi;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

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

    private Job job;

    public List<JobTag> getTagsCF() {
        if (this.job == null) {
            String cleMetier = currentPage.getProperties().get("clemetier").toString();
            this.job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        return job.getLabels();
    }
}
