package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ForModel(
        models = {NavigationImageModel.class, NavigationImageItemModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/NavigationImageItemTest.json"
)
@SuppressWarnings("WeakerAccess")
class NavigationImageItemModelTest extends AbstractModelTest {

    private static final GlobalConfig globalConfig = mock(GlobalConfig.class);
    private static final GlobalConfigService globalConfigService = mock(GlobalConfigService.class);

    private static final String PAGE_TITLE = "Demo Page";
    private static final String PAGE_DESCRIPTION = "Demo Page: a page to test the features.";

    @BeforeAll
    static void beforeAll() {
        when(globalConfigService.getConfig()).thenReturn(globalConfig);
        when(globalConfig.assetsPath()).thenReturn("/content/dam/leforemhe/fr");
    }

    @Test
    void testNavigationModelItems(){
        context.currentResource("/content/demo-page/jcr:content/navigation");
        Resource resource = context.request().getResource();
        NavigationImageModel navigationModel = resource.adaptTo(NavigationImageModel.class);
        assertNotNull(navigationModel);
        List<NavigationImageItemModel> navigationItems = navigationModel.getItems();
        for (int i = 0; i < navigationItems.size(); i++) {
            testItemLinkType(navigationItems.get(i), i);
            testItemLinkReference(navigationItems.get(i), i);
            testItemLabel(navigationItems.get(i), i);
            testItemTitle(navigationItems.get(i), i);
            testItemDescription(navigationItems.get(i), i);
            testGetChildPages(navigationItems.get(i), i);
        }
    }

    @Test
    void testDocumentNavigationItem() throws IllegalAccessException {
        final String expectedLinkReference = "/content/dam/asset.jpg",
                expectedLabel = "Document";
        context.currentResource("/content/demo-page/jcr:content/documentNavigation");
        Resource resource = context.request().getResource();
        NavigationImageModel navigationModel = resource.adaptTo(NavigationImageModel.class);
        assertNotNull(navigationModel);
        List<NavigationImageItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationImageItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertTrue(navigationItemModel.isDocument());
        //
        FieldUtils.writeField(navigationItemModel, "globalConfigService", globalConfigService, true);
        assertEquals(expectedLinkReference, navigationItemModel.getLinkReference());
        assertEquals(expectedLabel, navigationItemModel.getLabel());
    }

    @Test
    void testNoLinkNavigationItem(){
        final String expectedLinkReference = "/content/dam/asset.jpg",
                expectedLabel = "Document";
        context.currentResource("/content/demo-page/jcr:content/noLink");
        Resource resource = context.request().getResource();
        NavigationImageModel navigationModel = resource.adaptTo(NavigationImageModel.class);
        assertNotNull(navigationModel);
        List<NavigationImageItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationImageItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertFalse(navigationItemModel.isLink());
    }

    @Test
    void testInternalLinkWithLabel(){
        final String expectedLabel = "Custom label";
        context.currentResource("/content/demo-page/jcr:content/internal-with-label");
        Resource resource = context.request().getResource();
        NavigationImageModel navigationModel = resource.adaptTo(NavigationImageModel.class);
        assertNotNull(navigationModel);
        List<NavigationImageItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationImageItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertEquals(expectedLabel, navigationItemModel.getLabel());
    }

    @Test
    void testInternalLinkWithEmptyLabel(){
        context.currentResource("/content/demo-page/jcr:content/internal-with-empty-label");
        Resource resource = context.request().getResource();
        NavigationImageModel navigationModel = resource.adaptTo(NavigationImageModel.class);
        assertNotNull(navigationModel);
        List<NavigationImageItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationImageItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertEquals(PAGE_TITLE, navigationItemModel.getLabel());
    }

    private void testItemLinkType(NavigationImageItemModel navigationItemModel, int index) {
        assertEquals(index<2, navigationItemModel.isInternal());
        assertTrue(navigationItemModel.isLink());
    }

    private void testItemLinkReference(NavigationImageItemModel navigationItemModel, int index) {
        assertNotNull( navigationItemModel.getLinkReference());
        assertEquals(index>=2, navigationItemModel.getLinkReference().startsWith("http"));
    }

    private void testItemLabel(NavigationImageItemModel navigationItemModel, int index) {
        assertNotNull(navigationItemModel.getLabel());
        if(index<2) {
            assertEquals(PAGE_TITLE, navigationItemModel.getLabel());
        }
    }

    private void testItemDescription(NavigationImageItemModel navigationItemModel, int index) {
        assertNotNull(navigationItemModel.getDescription());
        if(index<2) {
            assertEquals(PAGE_DESCRIPTION, navigationItemModel.getDescription());
        }
    }

    private void testItemTitle(NavigationImageItemModel navigationItemModel, int index) {
        assertNotNull(navigationItemModel.getTitle());
        if(index<2) {
            assertEquals(PAGE_TITLE, navigationItemModel.getTitle());
        }
    }

    private void testGetChildPages(NavigationImageItemModel navigationItemModel, int index) {
        List<Page> childPages = navigationItemModel.getChildPages();
        assertNotNull(childPages);
        if(index<1) {
            assertEquals(3, childPages.size());
        }
    }
}
