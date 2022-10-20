package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.models.horizonemploi.JobTeaserModel;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = JobTeaserModel.class,
        resourceType = JobTeaserModelImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JobTeaserModelImpl implements JobTeaserModel {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/teaser";

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private Page currentPage;

    @SlingObject
    private Resource currentResource;

    @Self
    @Via(type = ResourceSuperType.class)
    private Teaser teaser;

    private Job job;

    @Override
    public Resource getImageResource() {
        return teaser.getImageResource();
    }

    @Override
    public String getPretitle() {
        return teaser.getPretitle();
    }

    @Override
    public String getTitle() {
        return teaser.getTitle();
    }

    @Override
    public String getDescription() {
        return teaser.getDescription();
    }

    @Override
    public List<ListItem> getActions() {
        return teaser.getActions();
    }

    @Override
    public String getId() {
        return teaser.getId();
    }

    @Override
    public ComponentData getData() {
        return teaser.getData();
    }

    @Override
    public Link getLink() {
        return teaser.getLink();
    }

    @Override
    public List<JobTag> getTags() {
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if (getLink() == null) {
            if (this.job == null && !inExperienceFragment) {
                String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
                job = contentFragmentUtilService.getJobFromJobID(cleMetier);
            }
        } else {
            if (this.job == null && !inExperienceFragment) {
                Page linkedResourcePage = currentResource.getResourceResolver().getResource(getLink().getURL()).adaptTo(Page.class);
                String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(linkedResourcePage);
                job = contentFragmentUtilService.getJobFromJobID(cleMetier);
            }
        }
        return job.getLabels();
    }
}
