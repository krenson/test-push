package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {NavigationItemModel.class, LinksModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/FooterLinksTest.json"
)
@SuppressWarnings("WeakerAccess")
public class LinksModelTests extends AbstractModelTest {

    private static final String PAGE_PATH = "/content/demo-page";

    @Test
    public void getFooterLinks(){
        context.currentResource(String.format("%s/jcr:content/footer/%s", PAGE_PATH, "links"));
        Resource linksResource = context.request().getResource();
        LinksModel linksModel = linksResource.adaptTo(LinksModel.class);
        assertNotNull(linksModel);
        List<NavigationItemModel> footerLinks = linksModel.getFooterLinks();
        assertNotNull(footerLinks);
        assertFalse(footerLinks.isEmpty());
    }

    @Test
    public void getFooterLinksEmpty(){
        context.currentResource(String.format("%s/jcr:content/footer/%s", PAGE_PATH, "linksFooterEmpty"));
        Resource linksResource = context.request().getResource();
        LinksModel linksModel = linksResource.adaptTo(LinksModel.class);
        assertNotNull(linksModel);
        List<NavigationItemModel> footerLinks = linksModel.getFooterLinks();
        assertNotNull(footerLinks);
        assertTrue(footerLinks.isEmpty());
    }

    @Test
    public void getEmptyList() {
        LinksModel linksModel = new LinksModel();
        assertTrue(linksModel.getFooterLinks().isEmpty());
    }
}
