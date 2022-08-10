package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import com.adobe.cq.wcm.style.ComponentStyleInfo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leforemhe.aem.site.core.services.ContentFragmentConfigService;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.inject.Inject;
import java.util.Map;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {TextModelCF.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = TextModelCFImpl.RESOURCE_TYPE)
public class TextModelCFImpl implements TextModelCF {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/contentfragments/text";

    @Self
    @Via(type = ResourceSuperType.class)
    private Text text;

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private ContentFragmentConfigService contentFragmentConfigService;

    @Inject
    private Page currentPage;

    @Override
    public String getText() {

        String cleMetier = currentPage.getProperties().get("clemetier").toString();
        String[] contentElements = {ContentFragmentUtilService.ELEMENT_ACCROCHE};

        Map<String, Object> dataFromCF = contentFragmentUtilService.getContentFromContentFragment(cleMetier, contentFragmentConfigService.getConfig().modelMetierPath(), contentElements);

        return dataFromCF.get(contentElements[0]).toString();

    }

}
