package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {ExportEFPropertiesModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/ExportEFPropertiesModelTest.json"
)
public class ExportEFPropertiesModelTest extends AbstractModelTest{

    @Test
    void getWithFilename() {
        context.currentResource("/content/demo-page/header/experiencefragment/page1/jcr:content");
        Resource exportResource = context.request().getResource();
        ExportEFPropertiesModel model = exportResource.adaptTo(ExportEFPropertiesModel.class);
        assertNotNull(model);
        assertEquals("folder", model.getFolder());
        assertEquals("filename", model.getFilename());
        assertEquals("<p>head</p>", model.getHtmlToPrepend());
        assertEquals("<p>footer</p>", model.getHtmlToAppend());
        assertEquals("<p>navigation</p>", model.getNavigationActivePage());
        assertEquals(true, model.getHideHamburgerMenu());
        assertEquals(true, model.getHideHamburgerMenuFully());
        assertEquals(true, model.getEnableAPI());
        assertEquals(true, model.getEnableSFTP());
    }

    @Test
    void getWithoutFilename() {
        context.currentResource("/content/demo-page/header/experiencefragment/page2/jcr:content");
        Resource exportResource = context.request().getResource();
        ExportEFPropertiesModel model = exportResource.adaptTo(ExportEFPropertiesModel.class);
        assertNotNull(model);
        assertEquals("folder", model.getFolder());
        assertEquals("header.experiencefragment.page2.html", model.getFilename());
        assertEquals("<p>head</p>", model.getHtmlToPrepend());
        assertEquals("<p>footer</p>", model.getHtmlToAppend());
        assertEquals("<p>navigation</p>", model.getNavigationActivePage());
        assertEquals(true, model.getHideHamburgerMenu());
        assertEquals(true, model.getHideHamburgerMenuFully());
        assertEquals(true, model.getEnableAPI());
        assertEquals(true, model.getEnableSFTP());
    }
}
