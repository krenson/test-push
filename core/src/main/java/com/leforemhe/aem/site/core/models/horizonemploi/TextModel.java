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

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextModel {

    @Inject
    private Page currentPage;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @ValueMapValue
    private String contentFragmentModel;

    @ValueMapValue
    private String selectedJobElement;

    @Self
    private SlingHttpServletRequest request;

    private Job job;

    public String getText() {
        String text = StringUtils.EMPTY;
        if (getContentFragmentModelType(contentFragmentModel).equals(Job.CONTENT_FRAGMENT_MODEL_TYPE) && !isInExperienceFragment()) {
            if (this.job == null) {
                String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
                this.job = contentFragmentUtilService.getJobFromJobID(cleMetier, request);
            }
            if (job != null) {
                text = getSingleTextValueFromElementJob(selectedJobElement);
            }
        }
        return text;
    }

    public String[] getTextList() {
        String[] text = {};
        if (getContentFragmentModelType(contentFragmentModel).equals(Job.CONTENT_FRAGMENT_MODEL_TYPE)  && !isInExperienceFragment()) {
            if (this.job == null) {
                String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
                this.job = contentFragmentUtilService.getJobFromJobID(cleMetier, request);
            }
            if (job != null) {
                text = getMultiTextValueFromElementJob(selectedJobElement);
            }
        }
        return text;
    }

    private String getSingleTextValueFromElementJob(String element) {
        return this.job.getSingleTextValueFromElement(element);
    }

    private String[] getMultiTextValueFromElementJob(String element) {
        return this.job.getMultiTextValueFromElement(element);
    }

    private String getContentFragmentModelType(String contentFragmentModel) {
        if (contentFragmentModel == null) {
            return StringUtils.EMPTY;
        }
        // Other cases coming (Activity)
        switch (contentFragmentModel) {
            case Job.CONTENT_FRAGMENT_MODEL_TYPE: return contentFragmentModel;
            default: return StringUtils.EMPTY;
        }
    }

    private Boolean isInExperienceFragment () {
       return currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
    }
}

