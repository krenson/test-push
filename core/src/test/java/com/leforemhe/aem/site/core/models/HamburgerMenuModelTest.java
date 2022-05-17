package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ForModel(
        models = {NavigationModel.class, NavigationItemModel.class, HamburgerMenuModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/NavigationTest.json"
)
@SuppressWarnings("WeakerAccess")
public class HamburgerMenuModelTest extends AbstractModelTest {

    private static final String PATH_INFO = "/content/demo-page.html";
    private static final String PAGE_TITLE = "Demo Page";
    private static final String PAGE_DESCRIPTION = "Demo Page: a page to test the features.";

    @Mock
    private SlingHttpServletRequest request;

    public void setup() {
        context.registerInjectActivateService(new GlobalConfigService(),
                "appName", "leforemhe",
                "unplacedPagesNodeName", "pages-non-places"
                );
        lenient().when(request.getPathInfo()).thenReturn(PATH_INFO);
    }

    @Test
    public void getFirstLevelPrefixTest() {
        String expected = "Accueil ";
        context.currentResource("/content/demo-page/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        assertEquals(expected, hamburgerMenuModel.getFirstLevelPrefix());
    }

    @Test
    public void getFirstLevelPrefixNotSetTest() {
        String expected = "";
        context.currentResource("/content/demo-page/page-1/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        assertEquals(expected, hamburgerMenuModel.getFirstLevelPrefix());
    }

    @Test
    public void isNavigationReadyTest() {
        context.currentResource("/content/demo-page/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        assertTrue(hamburgerMenuModel.isNavigationReady());
    }

    @Test
    public void isNavigationReadyNoNavigationTest() {
        context.currentResource("/content/demo-page/page-1/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        assertFalse(hamburgerMenuModel.isNavigationReady());
    }

    @Test
    public void getActiveNavigationItemsTest() {
        context.currentResource("/content/demo-page/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        hamburgerMenuModel.getNavigationItems().forEach(n->n.injectRequest(request));
        NavigationItemModel activeNavigationItem = hamburgerMenuModel.getActiveNavigationItem();
        assertNotNull(activeNavigationItem);
    }

    @Test
    public void getActiveNavigationItemsWhenNoNavigationTest() {
        context.currentResource("/content/demo-page/page-2/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        NavigationItemModel activeNavigationItem = hamburgerMenuModel.getActiveNavigationItem();
        assertNull(activeNavigationItem);
    }

    @Test
    public void getInactiveNavigationItemsTest() {
        context.currentResource("/content/demo-page/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        hamburgerMenuModel.getNavigationItems().forEach(n->n.injectRequest(request));
        List<NavigationItemModel> activeNavigationItem = hamburgerMenuModel.getInactiveNavigationItems();
        assertNotNull(activeNavigationItem);
        assertEquals(2, activeNavigationItem.size());
    }

    @Test
    public void getHamburgerWithEmptyNavigation(){
        context.currentResource("/content/demo-page/page-2/jcr:content/hamburgermenu");
        Resource resource = context.request().getResource();
        HamburgerMenuModel hamburgerMenuModel = resource.adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        List<NavigationItemModel> navItems = hamburgerMenuModel.getNavigationItems();
        assertNotNull(navItems);
        assertTrue(navItems.isEmpty());
    }

    @Test
    public void hamburgerIsVisibleTest(){
        context.currentResource("/content/demo-page/page-2/jcr:content/hamburgermenu");
        context.request().setPathInfo("/content/demo-page/page-2/jcr:content/hamburgermenu");
        HamburgerMenuModel hamburgerMenuModel = context.request().adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        assertFalse(hamburgerMenuModel.isHamburgermenuHiddenOnDesktop());
    }

    @Test
    public void hamburgerIsNotVisibleTest(){
        context.currentResource("/content/demo-page/pages-non-places/page-3/jcr:content/hamburgermenu");
        context.request().setPathInfo("/content/demo-page/pages-non-places/page-3/jcr:content/hamburgermenu");
        HamburgerMenuModel hamburgerMenuModel = context.request().adaptTo(HamburgerMenuModel.class);
        assertNotNull(hamburgerMenuModel);
        assertTrue(hamburgerMenuModel.isHamburgermenuHiddenOnDesktop());
    }
}
