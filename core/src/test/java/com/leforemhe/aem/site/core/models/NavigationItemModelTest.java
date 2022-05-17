package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ForModel(
        models = {NavigationModel.class, NavigationItemModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/NavigationTest.json"
)
@SuppressWarnings("WeakerAccess")
class NavigationItemModelTest extends AbstractModelTest {

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
        NavigationModel navigationModel = resource.adaptTo(NavigationModel.class);
        assertNotNull(navigationModel);
        List<NavigationItemModel> navigationItems = navigationModel.getItems();
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
                expectedLabel = "Dummy image";
        context.currentResource("/content/demo-page/jcr:content/documentNavigation");
        Resource resource = context.request().getResource();
        NavigationModel navigationModel = resource.adaptTo(NavigationModel.class);
        assertNotNull(navigationModel);
        List<NavigationItemModel> navigationItems = navigationModel.getItems();
        assertEquals(4, navigationItems.size());
        NavigationItemModel navigationItemModel = navigationItems.get(0);
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
        NavigationModel navigationModel = resource.adaptTo(NavigationModel.class);
        assertNotNull(navigationModel);
        List<NavigationItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertFalse(navigationItemModel.isLink());
    }

    @Test
    void testInternalLinkWithLabel(){
        final String expectedLabel = "Custom label";
        context.currentResource("/content/demo-page/jcr:content/internal-with-label");
        Resource resource = context.request().getResource();
        NavigationModel navigationModel = resource.adaptTo(NavigationModel.class);
        assertNotNull(navigationModel);
        List<NavigationItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertEquals(expectedLabel, navigationItemModel.getLabel());
    }

    @Test
    void testInternalLinkWithEmptyLabel(){
        context.currentResource("/content/demo-page/jcr:content/internal-with-empty-label");
        Resource resource = context.request().getResource();
        NavigationModel navigationModel = resource.adaptTo(NavigationModel.class);
        assertNotNull(navigationModel);
        List<NavigationItemModel> navigationItems = navigationModel.getItems();
        assertEquals(1, navigationItems.size());
        NavigationItemModel navigationItemModel = navigationItems.get(0);
        assertNotNull(navigationItemModel);
        assertEquals(PAGE_TITLE, navigationItemModel.getLabel());
    }

    @Test
    void documentLabelTest() {
        context.currentResource("/content/demo-page/jcr:content/documentNavigation");
        context.create().asset("/content/dam/asset.jpg", "/com/leforemhe/aem/site/core/test-resources/image.jpg", "image/JPEG");
        context.create().asset("/content/dam/asset.pdf", "/com/leforemhe/aem/site/core/test-resources/ImportantDocument.pdf", "application/pdf");
        context.create().asset("/content/dam/asset.xls", "/com/leforemhe/aem/site/core/test-resources/ImportantDocument.xls", "application/vnd.ms-excel");
        context.create().asset("/content/dam/asset.docx", "/com/leforemhe/aem/site/core/test-resources/Document.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        Resource resource = context.request().getResource();
        NavigationModel navigationModel = resource.adaptTo(NavigationModel.class);
        List<NavigationItemModel> navigationItems = navigationModel.getItems();
        NavigationItemModel navigationItemModelImage = navigationItems.get(0);
        NavigationItemModel navigationItemModelPdf = navigationItems.get(1);
        NavigationItemModel navigationItemModelXls = navigationItems.get(2);
        NavigationItemModel navigationItemModelDocx = navigationItems.get(3);

        testImageDocument(navigationItemModelImage);
        testPdfDocument(navigationItemModelPdf);
        testXlsDocument(navigationItemModelXls);
        testDocxDocument(navigationItemModelDocx);
    }

    private void testImageDocument(NavigationItemModel navigationItemModel) {
        assertEquals("Dummy image (JPG - 178.4 ko)", navigationItemModel.getDocumentLabel());
    }

    private void testPdfDocument(NavigationItemModel navigationItemModel) {
        assertEquals("Dummy pdf document (PDF - 1.4 Mo)", navigationItemModel.getDocumentLabel());
    }

    private void testXlsDocument(NavigationItemModel navigationItemModel) {
        assertEquals("Dummy xls document (XLS - 1.7 Mo)", navigationItemModel.getDocumentLabel());
    }

    private void testDocxDocument(NavigationItemModel navigationItemModel) {
        assertEquals("Dummy docx document (DOCX - 2 Mo)", navigationItemModel.getDocumentLabel());
    }

    private void testItemLinkType(NavigationItemModel navigationItemModel, int index) {
        assertEquals(index<2, navigationItemModel.isInternal());
        assertTrue(navigationItemModel.isLink());
    }

    private void testItemLinkReference(NavigationItemModel navigationItemModel, int index) {
        assertNotNull( navigationItemModel.getLinkReference());
        assertEquals(index>=2, navigationItemModel.getLinkReference().startsWith("http"));
    }

    private void testItemLabel(NavigationItemModel navigationItemModel, int index) {
        assertNotNull(navigationItemModel.getLabel());
        if(index<2) {
            assertEquals(PAGE_TITLE, navigationItemModel.getLabel());
        }
    }

    private void testItemDescription(NavigationItemModel navigationItemModel, int index) {
        assertNotNull(navigationItemModel.getDescription());
        if(index<2) {
            assertEquals(PAGE_DESCRIPTION, navigationItemModel.getDescription());
        }
    }

    private void testItemTitle(NavigationItemModel navigationItemModel, int index) {
        assertNotNull(navigationItemModel.getTitle());
        if(index<2) {
            assertEquals(PAGE_TITLE, navigationItemModel.getTitle());
        }
    }

    private void testGetChildPages(NavigationItemModel navigationItemModel, int index) {
        List<Page> childPages = navigationItemModel.getChildPages();
        assertNotNull(childPages);
        if(index<1) {
            assertEquals(3, childPages.size());
        }
    }

}
