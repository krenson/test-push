package com.leforemhe.aem.site.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.leforemhe.aem.site.core.models.utils.AEMCheckbox;
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

    private Map<AEMCheckbox, ArrayList<AEMCheckbox>> tagsList = new HashMap<>();

    @PostConstruct
    public void init() {
        ResourceResolver resourceResolver = request.getResourceResolver();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagNamespace != null && tagManager != null) {
            Iterator<Tag> parentTagIterator = tagManager.resolve(tagNamespace).listChildren();
            while (parentTagIterator.hasNext()) {
                Tag firstLevelParentTag = parentTagIterator.next();
                ArrayList<AEMCheckbox> childTags = new ArrayList<>();
                Iterator<Tag> childrenTagIterator = firstLevelParentTag.listChildren();
                while (childrenTagIterator.hasNext()) {
                    Tag childTag = childrenTagIterator.next();
                    childTags.add(new AEMCheckbox(childTag.getTitle(), childTag.getTagID()));
                }
                tagsList.put(new AEMCheckbox(firstLevelParentTag.getTitle(), firstLevelParentTag.getTagID()), childTags);
            }
        }
    }

    public Map<AEMCheckbox, ArrayList<AEMCheckbox>> getTagsList() {
        return tagsList;
    }

    public String getInternalLinkReference() {
        return internalLinkReference;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getTagNamespace() {
        return tagNamespace;
    }
}
