package com.leforemhe.aem.site.core.models.cfmodels;

import org.apache.sling.api.resource.Resource;

import com.day.cq.tagging.Tag;

public class JobTag {
    private String title;
    private String backgroundColor;

    public JobTag(Tag tag) {
        this.title = tag.getTitle();
        Resource resource = tag.adaptTo(Resource.class);
        if (resource != null) {
            this.backgroundColor = resource.getValueMap().get("backgroundColor", "#ffff00");
        } else {
            this.backgroundColor = "#ffff00";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
