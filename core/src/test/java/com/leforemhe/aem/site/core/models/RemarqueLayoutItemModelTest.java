package com.leforemhe.aem.site.core.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {RemarqueLayoutItemModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/RemarqueLayoutTest.json"
)
@SuppressWarnings("WeakerAccess")
public class RemarqueLayoutItemModelTest extends AbstractModelTest{

    @Test
    public void getItem() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow/column_0/experiencefragment");
        RemarqueLayoutItemModel itemModel = new RemarqueLayoutItemModel(context.request().getResource());
        assertNotNull(itemModel);

        String expectedPath = "/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow/column_0/experiencefragment";
        assertEquals(expectedPath, itemModel.getPath());
    }
}
