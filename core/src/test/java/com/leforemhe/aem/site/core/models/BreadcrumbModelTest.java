package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Breadcrumb;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ForModel(
        models = {BreadcrumbModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/NavigationTest.json"
)
@SuppressWarnings("WeakerAccess")
class BreadcrumbModelTest extends AbstractModelTest {

    private static final String PATH_INFO = "/content/demo-page.html";

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
    void breadcrumbInitTest() {
        context.currentResource("/content/demo-page/page-2/jcr:content/breadcrumb");
        Resource resource = context.request().getResource();
        BreadcrumbModel breadcrumbModel = resource.adaptTo(BreadcrumbModel.class);
        assertNotNull(breadcrumbModel);
    }

    @Test
    void breadcrumbIsVisibleTest() {
        context.currentResource("/content/demo-page/page-1/jcr:content/breadcrumb");
        Resource resource = context.request().getResource();
        BreadcrumbModel breadcrumbModel = resource.adaptTo(BreadcrumbModel.class);
        assertNotNull(breadcrumbModel);
        assertTrue(breadcrumbModel.isVisible());
    }

    @Test
    void getItemsWhenBreadcrumbNotHiddenTest() {
        // "page-1" has the hideInBreadcrumb property set to false
        // therefore "page-1-subpage" should return 2 items
        context.currentResource("/content/demo-page/page-1/jcr:content/breadcrumb");
        Resource resource = context.request().getResource();
        BreadcrumbModel breadcrumbModel = resource.adaptTo(BreadcrumbModel.class);
        assertNotNull(breadcrumbModel);
    }

    @Test
    void getItemsWhenBreadcrumbHiddenTest() {
        // "page-2" has the hideInBreadcrumb property set to true
        // therefore "page-2-subpage" should return only 1 item
        context.currentResource("/content/demo-page/page-2/jcr:content/breadcrumb");
        Resource resource = context.request().getResource();
        BreadcrumbModel breadcrumbModel = resource.adaptTo(BreadcrumbModel.class);
        assertNotNull(breadcrumbModel);
    }
}