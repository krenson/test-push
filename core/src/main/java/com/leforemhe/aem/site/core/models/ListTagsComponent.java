package com.leforemhe.aem.site.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.leforemhe.aem.site.core.models.utils.FilterModel;
import com.leforemhe.aem.site.core.models.utils.TagUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ListTagsComponent.RESOURCE_TYPE)
public class ListTagsComponent {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/listtags";

    @Inject
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String tagNamespace;

    @ValueMapValue
    private String internalLinkReference;

    @ValueMapValue
    private String ctaLabel;

    private ArrayList<FilterModel> tagsList = new ArrayList<>();

    @PostConstruct
    public void init() {
        ResourceResolver resourceResolver = request.getResourceResolver();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagNamespace != null && tagManager != null) {
            Iterator<Tag> parentTagIterator = tagManager.resolve(tagNamespace).listChildren();
            while (parentTagIterator.hasNext()) {
                Tag firstLevelParentTag = parentTagIterator.next();
                tagsList = TagUtils.getParentAndChildTags(firstLevelParentTag.listChildren(), firstLevelParentTag, tagsList);
            }
        }
    }

    public ArrayList<FilterModel> getTagsList() {
        return tagsList;
    }

    public String getInternalLinkReference() {
        return  ModelUtils.getVanityOfPageIfExists(internalLinkReference, request.getResourceResolver());
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getTagNamespace() {
        return tagNamespace;
    }
}
