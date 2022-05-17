package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.services.DynamicJavascriptConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {DynamicJsModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/DynamicJsModel.json"
)
class DynamicJsModelTest extends AbstractModelTest{

    private void setupValid(){
        context.runMode("test");
        context.registerInjectActivateService(new DynamicJavascriptConfigService(),
                "dynamicJavascriptFolderPath", "/content/demo-page/dam/js",
                "pathToListen", "/content/demo-page/ef/js",
                "systemUser", "datawrite");
    }

    @Test
    public void getSizeTest() {
        setupValid();
        final int expected = 1;
        context.currentResource("/content/demo-page/jcr:content/dynamicjs");
        DynamicJsModel dynamicJsModel = context.request().getResource().adaptTo(DynamicJsModel.class);
        assertNotNull(dynamicJsModel);
        assertEquals(expected, dynamicJsModel.getSize());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "/content/demo-page/jcr:content/dynamicjs-empty,0",
            "/content/demo-page,0",
            "/content/demo-page/jcr:content/dynamicjs-noname,1"
    })
    void getJsAssetPathsReturnsEmptyWhenWronglyConfigured(String path, int configuredJs) {
        setupValid();
        context.currentResource(path);
        DynamicJsModel dynamicJsModel = context.request().getResource().adaptTo(DynamicJsModel.class);
        assertNotNull(dynamicJsModel);
        assertEquals(configuredJs, dynamicJsModel.getSize());
        assertEquals(Collections.emptySet(), dynamicJsModel.getJavascriptFilePaths());
    }

    @Test
    void getJsAssetPathsTest() {
        setupValid();
        context.currentResource("/content/demo-page/jcr:content/dynamicjs");
        DynamicJsModel dynamicJsModel = context.request().getResource().adaptTo(DynamicJsModel.class);
        assertNotNull(dynamicJsModel);
        assertEquals(1, dynamicJsModel.getSize());
    }

    @Test
    void getJsAssetPathsReturnsEmptyListWhenExcludedFromRestrictedPages() {
        setupValid();
        context.currentResource("/content/demo-page/jcr:content/dynamicjs-excluded");
        DynamicJsModel dynamicJsModel = context.request().adaptTo(DynamicJsModel.class);
        assertNotNull(dynamicJsModel);
        assertEquals(1, dynamicJsModel.getSize());
        assertEquals(Collections.emptySet(), dynamicJsModel.getJavascriptFilePaths());
    }

}
