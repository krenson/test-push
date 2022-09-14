package com.leforemhe.aem.site.core.models.horizonemploi;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PopupComponent {
    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Page currentPage;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @ValueMapValue
    private LinkModel linkModel;

    @SlingObject
    private Resource currentResource;

    private Job job;

    private String title;
    private String icon;

    public LinkModel getLinkModel() {
        return linkModel;
    }

    public String getTitle() {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource linkModel = resourceResolver.resolve(currentResource.getPath() + "/linkModel");
        title = linkModel.getValueMap().get("title").toString();
        return title;
    }

    public String getIcon() {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource linkModel = resourceResolver.resolve(currentResource.getPath() + "/linkModel");
        icon = linkModel.getValueMap().get("iconFileReference").toString();
        return icon;
    }

    public String getJobTitle() {
        if (this.job == null) {
            String cleMetier = currentPage.getProperties().get("clemetier").toString();
            job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        return job.getTitle();
    }
}
