package com.leforemhe.aem.site.core.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {RemarqueLayoutColumnModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/RemarqueLayoutTest.json"
)
@SuppressWarnings("WeakerAccess")
public class RemarqueLayoutColumnModelTest extends AbstractModelTest {

    @Test
    public void getEmptyColumn() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutFirstRowEmptyColumn/firstrow/column_3");
        RemarqueLayoutColumnModel columnModel = context.request().getResource().adaptTo(RemarqueLayoutColumnModel.class);
        assertNotNull(columnModel);

        String expectedPath = "/content/demo-page/jcr:content/remarqueLayoutFirstRowEmptyColumn/firstrow/column_3";
        String expectedName = "column_3";
        int expectedSize = 0;
        assertEquals(expectedPath, columnModel.getPath());
        assertEquals(expectedName, columnModel.getName());
        assertTrue(columnModel.isEmpty());
        assertEquals(expectedSize,columnModel.getItems().size());
    }

    @Test
    public void getNotEmptyColumn() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutFirstRowEmptyColumn/firstrow/column_1");
        RemarqueLayoutColumnModel columnModel = context.request().getResource().adaptTo(RemarqueLayoutColumnModel.class);
        assertNotNull(columnModel);

        String expectedPath = "/content/demo-page/jcr:content/remarqueLayoutFirstRowEmptyColumn/firstrow/column_1";
        String expectedName = "column_1";
        int expectedSize = 2;
        assertEquals(expectedPath, columnModel.getPath());
        assertEquals(expectedName, columnModel.getName());
        assertFalse(columnModel.isEmpty());
        assertEquals(expectedSize,columnModel.getItems().size());
    }
}
