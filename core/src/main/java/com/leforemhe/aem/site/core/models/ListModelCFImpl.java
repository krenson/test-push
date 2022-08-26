package com.leforemhe.aem.site.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = { ListModelCF.class, ComponentExporter.class},
        resourceType = ListModelCFImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ListModelCFImpl implements ListModelCF {

    static final String RESOURCE_TYPE = "leforemhe/components/site/contentfragments/list";

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private ContentFragmentConfigService contentFragmentConfigService;

    @Inject
    private Page currentPage;

    @Self
    @Via(type = ResourceSuperType.class)
    private List list;

    public String[] getSynonymes() {
        String cleMetier = currentPage.getProperties().get("clemetier").toString();
        String[] contentElements = {ContentFragmentUtilService.ELEMENT_SYNONYMES};

        Map<String, Object> dataFromCF = contentFragmentUtilService.getContentFromContentFragment(cleMetier, contentFragmentConfigService.getConfig().modelMetierPath(), contentElements);

        return (String[]) dataFromCF.get(contentElements[0]);
    }

}
