package com.leforemhe.aem.site.core.models;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ForModel(
        models = {RemarqueLayoutFirstRowModel.class, RemarqueLayoutSecondRowModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/RemarqueLayoutTest.json"
)
@SuppressWarnings("WeakerAccess")
public class RemarqueLayoutSecondRowModelTest extends AbstractModelTest {

    @Test
    public void getLayoutWithEmptyRows() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutEmpty/secondrow");
        RemarqueLayoutSecondRowModel secondRowModel = context.request().getResource().adaptTo(RemarqueLayoutSecondRowModel.class);
        assertNotNull(secondRowModel);
        assertNotNull(secondRowModel.getItems());
        assertTrue(secondRowModel.getItems().isEmpty());
        assertNotNull(secondRowModel.getGroups());
        assertTrue(secondRowModel.getGroups().isEmpty());
    }

    @Test
    public void getLayoutWithFullSecondRow() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutFilledSecondRow/secondrow");
        RemarqueLayoutSecondRowModel secondRowModel = context.request().getResource().adaptTo(RemarqueLayoutSecondRowModel.class);
        assertNotNull(secondRowModel);
        assertNotNull(secondRowModel.getItems());
        assertFalse(secondRowModel.getItems().isEmpty());
        assertNotNull(secondRowModel.getGroups());
        assertFalse(secondRowModel.getGroups().isEmpty());
        assertTrue(secondRowModel.isLastContainerFull());

        String[] expectedEFPath = new String[]{
                "/content/demo-page/jcr:content/remarqueLayoutFilledSecondRow/secondrow/experiencefragment",
                "/content/demo-page/jcr:content/remarqueLayoutFilledSecondRow/secondrow/experiencefragment1",
                "/content/demo-page/jcr:content/remarqueLayoutFilledSecondRow/secondrow/experiencefragment2",
                "/content/demo-page/jcr:content/remarqueLayoutFilledSecondRow/secondrow/experiencefragment3"
        };
        int i = 0;
        for (List<RemarqueLayoutItemModel> group : secondRowModel.getGroups()) {
            for (RemarqueLayoutItemModel item : group) {
                assertEquals(item, secondRowModel.getItems().get(i));
                i++;
            }
        }

        i = 0;
        for (RemarqueLayoutItemModel item : secondRowModel.getItems()) {
            assertEquals(item.getPath(), expectedEFPath[i]);
            i++;
        }
    }

    @Test
    public void getLayoutWithNotFullSecondRow() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutNotFilledSecondRow/secondrow");
        RemarqueLayoutSecondRowModel secondRowModel = context.request().getResource().adaptTo(RemarqueLayoutSecondRowModel.class);
        assertNotNull(secondRowModel);
        assertNotNull(secondRowModel.getItems());
        assertFalse(secondRowModel.getItems().isEmpty());
        assertNotNull(secondRowModel.getGroups());
        assertFalse(secondRowModel.getGroups().isEmpty());
        assertFalse(secondRowModel.isLastContainerFull());

        String[] expectedEFPath = new String[]{
                "/content/demo-page/jcr:content/remarqueLayoutNotFilledSecondRow/secondrow/experiencefragment",
                "/content/demo-page/jcr:content/remarqueLayoutNotFilledSecondRow/secondrow/experiencefragment1",
                "/content/demo-page/jcr:content/remarqueLayoutNotFilledSecondRow/secondrow/experiencefragment2"
        };
        int i = 0;
        for (List<RemarqueLayoutItemModel> group : secondRowModel.getGroups()) {
            for (RemarqueLayoutItemModel item : group) {
                assertEquals(item, secondRowModel.getItems().get(i));
                i++;
            }
        }

        i = 0;
        for (RemarqueLayoutItemModel item : secondRowModel.getItems()) {
            assertEquals(item.getPath(), expectedEFPath[i]);
            i++;
        }
    }
}
