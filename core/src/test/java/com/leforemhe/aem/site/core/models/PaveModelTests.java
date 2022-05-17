package com.leforemhe.aem.site.core.models;

import com.adobe.cq.sightly.WCMBindings;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingBindings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ForModel(
        models = {PaveModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/PaveTest.json"
)
@SuppressWarnings("WeakerAccess")
class PaveModelTests extends AbstractModelTest {

    @Mock
    private Style style;

    public void setup() {
        context.registerService(ResourceResolver.class);
        context.registerService(PageManager.class);

        //Initiating design for pave-internal
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        SlingBindings slingBindings = new SlingBindings();
        slingBindings.put(WCMBindings.CURRENT_STYLE, style);
        context.request().setAttribute(SlingBindings.class.getName(), slingBindings);
    }

    @Test
    void getTitleTest() {
        final String expected = "Pavé";
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        PaveModel paveModel = context.request().getResource().adaptTo(PaveModel.class);
        assertNotNull(paveModel);
        assertEquals(expected, paveModel.getTitle());

    }

    @Test
    void getDescriptionTest() {
        final String expected = "Description";
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        PaveModel paveModel = context.request().getResource().adaptTo(PaveModel.class);
        assertNotNull(paveModel);
        assertEquals(expected, paveModel.getDescription());

    }

    @Test
    void getAccrocheTest() {
        final String expected = "Accroche";
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        PaveModel paveModel = context.request().getResource().adaptTo(PaveModel.class);
        assertNotNull(paveModel);
        assertEquals(expected, paveModel.getAccroche());

    }

    @Test
    void getImageSrcTest() {
        String expectedFileReference = "/content/dam/leforemhe/image.png";
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        PaveModel paveModel = context.request().getResource().adaptTo(PaveModel.class);
        assertNotNull(paveModel);
        assertEquals(expectedFileReference, paveModel.getImageSrc());
    }

    @Test
    void getEmptyColor() {
        lenient().when(style.getOrDefault("color", "arrowLink")).thenReturn("arrowLink");
        String expectedColor = "arrowLink";
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        PaveModel paveModel = context.request().adaptTo(PaveModel.class);
        if (paveModel != null) {
            assertEquals(expectedColor, paveModel.getColor());
        }
    }

    @Test
    void getNonEmptyColor() {
        lenient().when(style.getOrDefault("color", "arrowLink")).thenReturn("BtBleuPrusse34495E");
        String expectedColor = "BtBleuPrusse34495E";
        context.currentResource("/content/demo-page/jcr:content/pave-internal");
        PaveModel paveModel = context.request().adaptTo(PaveModel.class);
        if (paveModel != null) {
            assertEquals(expectedColor, paveModel.getColor());
        }
    }
}
