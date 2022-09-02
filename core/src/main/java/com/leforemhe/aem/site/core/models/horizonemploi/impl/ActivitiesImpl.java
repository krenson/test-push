package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Accordion;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.cfmodels.Activity;
import com.leforemhe.aem.site.core.models.horizonemploi.Activities;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import java.util.List;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { Activities.class,
        ComponentExporter.class }, resourceType = ActivitiesImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
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
        if(activities == null){
            String cleMetier = currentPage.getProperties().get("clemetier").toString();
            this.activities = contentFragmentUtilService.getActivitiesFromJobID(cleMetier);
        }
        return this.activities;
    }

}
