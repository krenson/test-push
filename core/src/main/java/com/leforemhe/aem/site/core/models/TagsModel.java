package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.pojo.Tag;
import com.leforemhe.aem.site.core.services.ContentFragmentConfigService;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sling model for the etiquette component.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagsModel {

    @Inject
    private ContentFragmentUtilService contentFragmentUtilService;

    @Inject
    private ContentFragmentConfigService contentFragmentConfigService;

    @Inject
    private Page currentPage;

    public List<Tag> getTagsCF(){
        List<Tag> tags;

        String cleMetier = currentPage.getProperties().get("clemetier").toString();
        String[] contentElements = {ContentFragmentUtilService.ELEMENT_ETIQUETTES};

        Map<String, Object> dataFromCF = contentFragmentUtilService.getContentFromContentFragment(cleMetier, contentFragmentConfigService.getConfig().modelMetierPath(), contentElements);

        tags = contentFragmentUtilService.convertListTagsNamesToListTags((String[]) dataFromCF.get(contentElements[0]));

        return tags;

    }
}
