package com.leforemhe.aem.site.core.search.impl;

import com.day.cq.tagging.TagManager;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.ModelUtils;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.search.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leforemhe.aem.site.core.services.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = SearchResult.class,
        resourceType = "cq:Page"
)
public class PageSearchResultImpl implements SearchResult {
    private static final String PAGE_EXTENSION = ".html";
    private static final String NBSP_HTML = "&nbsp;";

    @Self
    private Resource resource;

    @Inject
    private ResourceResolver resourceResolver;

    @Inject
    private ImageService imageService;

    private List<JobTag> jobTags;

    @PostConstruct
    protected void intialize() {
        this.page = resourceResolver.adaptTo(PageManager.class).getContainingPage(resource);
        this.jobTags = resolveTags(getTagIds());
    }

    private Page page;

    private List<String> excerpts = new ArrayList<String>();

    public ContentType getContentType() {
        return ContentType.PAGE;
    }

    public String getPath() {
        return this.page.getPath();
    }

    public String getURL() {
        return getPath() + PAGE_EXTENSION;
    }

    public String getTitle() {
        return StringUtils.defaultIfBlank(StringUtils.defaultIfBlank(page.getPageTitle(), page.getTitle()), page.getName());
    }

    public String getJobId() {
        return page.getProperties().get(Constants.CLE_METIER).toString();
    }

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String getDescription() {
        String description = "";
        description = page.getDescription();
        return description;
    }

    @Override
    public List<String> getTagIds() {
        final List<String> tagIds = new ArrayList<String>();

        for (Tag tag : page.getTags()) {
            tagIds.add(tag.getTagID());
        }

        return tagIds;
    }

    @Override
    public void setExcerpts(Collection<String> excerpts) {
        for (String excerpt : excerpts) {
            // Funny clean-up require as getExcerpt() can sometimes inject &nbsp; into the excerpt text..
            excerpt = StringUtils.replace(excerpt, NBSP_HTML, "");
            if (StringUtils.isNotBlank(excerpt)) {
                this.excerpts.add(StringUtils.trim(excerpt));
            }
        }
    }

    @Override
    public List<JobTag> getJobTags() {
        return this.jobTags;
    }

    @Override
    public String getVanityPath() {
        return ModelUtils.getVanityOfPageIfExists(getPath(), resourceResolver);
    }

    @Override
    public String getFeaturedImage() {
        String featuredImageFileReference = ModelUtils.getFeaturedImageOfPage(getPath(), resourceResolver);
        if(!StringUtils.isEmpty(featuredImageFileReference)) {
            return imageService.getImageRendition(featuredImageFileReference, resourceResolver);
        }
        // TODO : Replace StringUtils.EMPTY here with fallback image for tuile
        return StringUtils.isEmpty(featuredImageFileReference) ? featuredImageFileReference : StringUtils.EMPTY;
    }

    private List<JobTag> resolveTags(List<String> tagIds) {
        List<JobTag> tags = new ArrayList<>();
        if (resourceResolver != null) {
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag resolvedTag = tagManager.resolve(tagId);
                    if (resolvedTag != null && Constants.ALLOWED_NAMESPACES.contains(resolvedTag.getNamespace().getName())) {
                        tags.add(new JobTag(resolvedTag));
                    }
                }
            }
        }
        return tags;
    }
}
