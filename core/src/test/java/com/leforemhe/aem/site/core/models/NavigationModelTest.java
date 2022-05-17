package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ForModel(
        models={NavigationModel.class, NavigationItemModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/NavigationTest.json"
)
class NavigationModelTest extends AbstractModelTest {

    @Mock
    private PageManager pageManager;

    @Mock
    private NavigationModel navigationModel;

    public void setup() {

        lenient().when(modelFactory.getModelFromWrappedRequest(eq(context.request()),
                any(Resource.class),
                eq(NavigationModel.class))).thenReturn(navigationModel);

        lenient().when(pageManager.getPage(any(String.class)))
                .thenReturn(mock(Page.class));

        context.registerService(ResourceResolver.class);
        context.registerService(PageManager.class);
    }

    @Test
    void testGetItems() {
        context.currentResource("/content/demo-page/jcr:content/navigation");

        NavigationModel navigationModel = context.request().getResource().adaptTo(NavigationModel.class);
        List<NavigationItemModel> links = Objects.requireNonNull(navigationModel).getItems();
        assertNotNull(links);
        links.forEach(Assertions::assertNotNull);
        assertFalse(links.isEmpty());
    }

    @Test
    void testGetItemsNotConfigured() {
        context.currentResource("/content/demo-page/page-2/jcr:content/navigation");

        NavigationModel navigationModel = context.request().getResource().adaptTo(NavigationModel.class);
        List<NavigationItemModel> links = Objects.requireNonNull(navigationModel).getItems();
        assertNotNull(links);
        assertTrue(links.isEmpty());
    }
}
