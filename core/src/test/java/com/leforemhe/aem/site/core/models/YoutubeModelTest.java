package com.leforemhe.aem.site.core.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {YoutubeModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/YoutubeModelTests.json"
)
@SuppressWarnings("WeakerAccess")
public class YoutubeModelTest extends AbstractModelTest {
    private static final String PAGE_PATH = "/content/demo-page";

    @Test
    public void getVideoIDTest(){
        final String expected = "video-id";
        context.currentResource(PAGE_PATH+"/jcr:content/youtube-video");
        YoutubeModel youtubeModel = context.request().getResource().adaptTo(YoutubeModel.class);
        assertNotNull(youtubeModel);
        assertEquals(expected, youtubeModel.getVideoID());
    }

    @Test
    public void getPlayListIDTest(){
        final String expected = "playlist-id";
        context.currentResource(PAGE_PATH+"/jcr:content/youtube-playlist");
        YoutubeModel youtubeModel = context.request().getResource().adaptTo(YoutubeModel.class);
        assertNotNull(youtubeModel);
        assertEquals(expected, youtubeModel.getPlaylistID());
    }
}
