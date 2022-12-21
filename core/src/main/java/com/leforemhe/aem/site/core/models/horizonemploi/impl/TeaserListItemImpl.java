package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.models.horizonemploi.TeaserListItem;
import com.leforemhe.aem.site.core.services.ImageService;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeaserListItemImpl implements TeaserListItem {
    private final String title;
    private final String name;
    private Link link;
    private final String description;
    private final Resource imageResource;
    private final List<JobTag> teaserTags;
    private ImageService imageService;
    private String fallBackImage;

    private List<JobTag> jobTags = new ArrayList<>();

    public TeaserListItemImpl(Page page, Resource imageResource, String url, ImageService imageService, String fallbackImage) {
        Tag[] tags = page.getTags();
        this.title = page.getTitle();
        this.name = page.getName();
        this.description = page.getDescription();
        this.imageResource = imageResource;
        this.link = new TeaserListItemLinkImpl(page, url);
        for (Tag tag: tags) {
            if(Constants.ALLOWED_NAMESPACES.contains(tag.getNamespace().getName())) {
                jobTags.add(new JobTag(tag));
            }
        }
        this.teaserTags = jobTags;
        this.imageService = imageService;
        this.fallBackImage = fallbackImage;
    }

    @Override
    public boolean isActionsEnabled() {
        return TeaserListItem.super.isActionsEnabled();
    }

    @Override
    public List<ListItem> getActions() {
        return TeaserListItem.super.getActions();
    }

    @Override
    public @Nullable Link getLink() {
        return this.link;
    }

    @Override
    public Resource getImageResource() {
        return this.imageResource;
    }

    public String getImageResourcePath() {
        if (getImageResource().getValueMap().get("fileReference") != null) {
            return imageService.getImageRendition(getImageResource().getValueMap().get("fileReference").toString(), imageResource.getResourceResolver());
        } else {
            return imageService.getImageRendition(fallBackImage, imageResource.getResourceResolver());
        }
    }

    @Override
    public boolean isImageLinkHidden() {
        return TeaserListItem.super.isImageLinkHidden();
    }

    @Override
    public String getPretitle() {
        return TeaserListItem.super.getPretitle();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean isTitleLinkHidden() {
        return TeaserListItem.super.isTitleLinkHidden();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getTitleType() {
        return TeaserListItem.super.getTitleType();
    }

    @Override
    public List<JobTag> getTags() {
        return this.teaserTags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, name, link, description, imageResource, teaserTags, jobTags);
    }
}
