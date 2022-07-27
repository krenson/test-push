package com.leforemhe.aem.site.core.models;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Accordion;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.pojo.ContentFragmentActivites;
import com.leforemhe.aem.site.core.services.ContentFragmentConfigService;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = { AccordionModelCF.class, ComponentExporter.class},
        resourceType = AccordionModelCFImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class AccordionModelCFImpl implements AccordionModelCF {

    static final String RESOURCE_TYPE = "leforemhe/components/site/contentfragments/accordion";

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private ContentFragmentConfigService contentFragmentConfigService;

    @Inject
    private Page currentPage;

    @Self
    @Via(type = ResourceSuperType.class)
    private Accordion accordion;

    public List<ContentFragmentActivites> getActivities() {
        String cleMetier = currentPage.getProperties().get("clemetier").toString();
        List<ContentFragment> contentFragments = contentFragmentUtilService.getContentFragmentsByIdAndModel(cleMetier, contentFragmentConfigService.getConfig().modelActivitesPath());
        return contentFragmentUtilService.convertLisContentFragmentToActivites(contentFragments);
    }

}
