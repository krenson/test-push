package com.leforemhe.aem.site.core.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {JsConfigModel.class, JsConfigModel.PageRestriction.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/DynamicJsModel.json"
)
class JsConfigModelTest extends AbstractModelTest{

    @Test
    void getFileNameTest(){
        final String expected = "javascript1";
        context.currentResource("/content/demo-page/ef/js/js-restricted-excluded/jcr:content");
        JsConfigModel jsConfigModel = context.request().getResource().adaptTo(JsConfigModel.class);
        assertNotNull(jsConfigModel);
        assertEquals(expected, jsConfigModel.getFilename());
    }

    @Test
    void getEnvironmentTest(){
        final String expected = "test";
        context.currentResource("/content/demo-page/ef/js/js-restricted-excluded/jcr:content");
        JsConfigModel jsConfigModel = context.request().getResource().adaptTo(JsConfigModel.class);
        assertNotNull(jsConfigModel);
        assertEquals(expected, jsConfigModel.getEnvironment());
    }

    @Test
    void getPagesRestrictTest(){
        context.currentResource("/content/demo-page/ef/js/js-restricted-excluded/jcr:content");
        JsConfigModel jsConfigModel = context.request().getResource().adaptTo(JsConfigModel.class);
        assertNotNull(jsConfigModel);
        assertEquals(1, jsConfigModel.getPagesRestrict().size());
    }

    @Test
    void getPagesRestrictReturnsEmptyListWhenNull(){
        context.currentResource("/content/demo-page/ef/js/js1/jcr:content");
        JsConfigModel jsConfigModel = context.request().adaptTo(JsConfigModel.class);
        assertNotNull(jsConfigModel);
        assertEquals(Collections.emptyList(), jsConfigModel.getPagesRestrict());
    }

    @Test
    void includeChildrenTest(){
        context.currentResource("/content/demo-page/ef/js/js-restricted-children/jcr:content");
        JsConfigModel jsConfigModel = context.request().getResource().adaptTo(JsConfigModel.class);
        assertNotNull(jsConfigModel);
        assertTrue(jsConfigModel.getPagesRestrict().stream().findFirst()
                .orElseGet(Assertions::fail)
                .includeChildren());

        context.currentResource("/content/demo-page/ef/js/js-restricted-excluded/jcr:content");
        jsConfigModel = context.request().getResource().adaptTo(JsConfigModel.class);
        assertNotNull(jsConfigModel);
        assertFalse(jsConfigModel.getPagesRestrict().stream().findFirst()
                .orElseGet(Assertions::fail)
                .includeChildren());
    }
}
