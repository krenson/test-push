package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {ExportPHPPropertiesModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/ExportPHPPropertiesModelTest.json"
)
public class ExportPHPPropertiesModelTest extends AbstractModelTest{

    @Test
    void getWithFilename() {
        context.currentResource("/content/demo-page/header/experiencefragment/page1/jcr:content");
        Resource exportResource = context.request().getResource();
        ExportPHPPropertiesModel model = exportResource.adaptTo(ExportPHPPropertiesModel.class);
        assertNotNull(model);
        assertEquals("folder", model.getFolder());
    }

    @Test
    void getWithoutFilename() {
        context.currentResource("/content/demo-page/header/experiencefragment/page2/jcr:content");
        Resource exportResource = context.request().getResource();
        ExportPHPPropertiesModel model = exportResource.adaptTo(ExportPHPPropertiesModel.class);
        assertNotNull(model);
        assertEquals("folder", model.getFolder());
    }
}
