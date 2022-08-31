package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.horizonemploi.JobTitle;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { JobTitle.class,
        ComponentExporter.class }, resourceType = JobTitleImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class JobTitleImpl implements JobTitle {

    static final String RESOURCE_TYPE = "leforemhe/components/site/contentfragments/title";

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private Page currentPage;

    @Self
    @Via(type = ResourceSuperType.class)
    private Title title;

    private Job job;

    @Override
    public String getText() {
        if (job == null) {
            String cleMetier = currentPage.getProperties().get("clemetier").toString();
            this.job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        return job.getTitle();
    }

}
