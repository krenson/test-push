package com.leforemhe.aem.site.core.models.horizonemploi;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.cfmodels.Activity;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
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

    private Job job;

    public String getText() {
        String text = StringUtils.EMPTY;
        if (this.job == null) {
            String cleMetier = currentPage.getProperties().get("clemetier").toString();
            this.job = contentFragmentUtilService.getJobFromJobID(cleMetier);
        }
        if (contentFragmentModel == null) {
            return StringUtils.EMPTY;
        }
        if (contentFragmentModel.equals("job")) {
            text = getTextValueFromElementJob(selectedJobElement);
        }
        return text;
    }

    private String getTextValueFromElementJob(String element) {
        return this.job.getTextValueFromElement(element);
    }
}

