package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.models.Accordion;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Activity;
import com.leforemhe.aem.site.core.models.horizonemploi.Activities;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import java.util.List;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Activities.class, resourceType = ActivitiesImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ActivitiesImpl implements Activities {

    static final String RESOURCE_TYPE = "leforemhe/components/site/contentfragments/activities";

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private Page currentPage;

    @Self
    @Via(type = ResourceSuperType.class)
    private Accordion accordion;

    private List<Activity> activities;

    public List<Activity> getActivities() {
        boolean inExperienceFragment = currentPage.getContentResource().getResourceType().equalsIgnoreCase(Constants.EF_RESOURCE_TYPE);
        if(activities == null && !inExperienceFragment){
            String cleMetier = contentFragmentUtilService.getCleMeteirFromPage(currentPage);
            this.activities = !StringUtils.isEmpty(cleMetier) ? contentFragmentUtilService.getActivitiesFromJobID(cleMetier) : null;
        }
        return this.activities;
    }

}
