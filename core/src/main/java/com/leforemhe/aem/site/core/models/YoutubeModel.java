package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling model for the youtube component.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class YoutubeModel {
    @ValueMapValue
    private String videoID;
    @ValueMapValue
    private String playlistID;

    public String getVideoID() {
        return videoID;
    }

    public String getPlaylistID() {
        return playlistID;
    }
}
