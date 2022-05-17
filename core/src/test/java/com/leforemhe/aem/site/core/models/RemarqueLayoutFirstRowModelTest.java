package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import javax.jcr.RepositoryException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {RemarqueLayoutFirstRowModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/RemarqueLayoutTest.json"
)
@SuppressWarnings("WeakerAccess")
public class RemarqueLayoutFirstRowModelTest extends AbstractModelTest {

    @Test
    public void getLayoutWithEmptyRows() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutEmpty/firstrow");
        RemarqueLayoutFirstRowModel firstRowModel = context.request().getResource().adaptTo(RemarqueLayoutFirstRowModel.class);
        assertNotNull(firstRowModel);
        assertNotNull(firstRowModel.getColumns());
        assertTrue(firstRowModel.getColumns().isEmpty());
        assertNotNull(firstRowModel.getGroups());
        assertTrue(firstRowModel.getGroups().isEmpty());

    }

    @Test
    public void getLayoutWithFullFirstRow() {

        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutFilledFirstRow/firstrow");
        RemarqueLayoutFirstRowModel firstRowModel = context.request().getResource().adaptTo(RemarqueLayoutFirstRowModel.class);
        assertNotNull(firstRowModel);
        assertNotNull(firstRowModel.getColumns());
        assertFalse(firstRowModel.getColumns().isEmpty());
        assertNotNull(firstRowModel.getGroups());
        assertFalse(firstRowModel.getGroups().isEmpty());
        assertTrue(firstRowModel.isLastContainerFull());

        int i = 0;
        for (List<RemarqueLayoutColumnModel> group : firstRowModel.getGroups()) {
            for (RemarqueLayoutColumnModel column : group) {
                assertEquals(column, firstRowModel.getColumns().get(i));
                i++;
            }
        }

        String[][] expectedEFPath = new String[][]{
                {"/content/demo-page/jcr:content/remarqueLayoutFilledFirstRow/firstrow/column_0/experiencefragment"},
                {"/content/demo-page/jcr:content/remarqueLayoutFilledFirstRow/firstrow/column_1/experiencefragment", "/content/demo-page/jcr:content/remarqueLayoutFilledFirstRow/firstrow/column_1/experiencefragment2"},
                {"/content/demo-page/jcr:content/remarqueLayoutFilledFirstRow/firstrow/column_2/experiencefragment"},
                {"/content/demo-page/jcr:content/remarqueLayoutFilledFirstRow/firstrow/column_3/experiencefragment"}
        };

        i = 0;
        for (RemarqueLayoutColumnModel column : firstRowModel.getColumns()) {
            int j = 0;
            for (RemarqueLayoutItemModel item : column.getItems()) {
                assertEquals(item.getPath(), expectedEFPath[i][j]);
                j++;
            }
            i++;
        }
    }

    @Test
    public void getLayoutWithNotFullFirstRow() {

        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow");
        RemarqueLayoutFirstRowModel firstRowModel = context.request().getResource().adaptTo(RemarqueLayoutFirstRowModel.class);
        assertNotNull(firstRowModel);
        assertNotNull(firstRowModel.getColumns());
        assertFalse(firstRowModel.getColumns().isEmpty());
        assertNotNull(firstRowModel.getGroups());
        assertFalse(firstRowModel.getGroups().isEmpty());
        assertFalse(firstRowModel.isLastContainerFull());

        int i = 0;
        for (List<RemarqueLayoutColumnModel> group : firstRowModel.getGroups()) {
            for (RemarqueLayoutColumnModel column : group) {
                assertEquals(column, firstRowModel.getColumns().get(i));
                i++;
            }
        }

        String[][] expectedEFPath = new String[][]{
                {"/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow/column_0/experiencefragment"},
                {"/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow/column_1/experiencefragment", "/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow/column_1/experiencefragment2"},
                {"/content/demo-page/jcr:content/remarqueLayoutNotFilledFirstRow/firstrow/column_2/experiencefragment"},
        };

        i = 0;
        for (RemarqueLayoutColumnModel column : firstRowModel.getColumns()) {
            int j = 0;
            for (RemarqueLayoutItemModel item : column.getItems()) {
                assertEquals(item.getPath(), expectedEFPath[i][j]);
                j++;
            }
            i++;
        }

    }

    @Test
    public void getLayoutWithEmptyColumn() {
        context.currentResource("/content/demo-page/jcr:content/remarqueLayoutFirstRowEmptyColumn/firstrow");
        RemarqueLayoutFirstRowModel firstRowModel = context.request().getResource().adaptTo(RemarqueLayoutFirstRowModel.class);
        assertNotNull(firstRowModel);
        assertNotNull(firstRowModel.getColumns());
        assertFalse(firstRowModel.getColumns().isEmpty());
        assertNotNull(firstRowModel.getGroups());
        assertFalse(firstRowModel.getGroups().isEmpty());
        assertFalse(firstRowModel.isLastContainerFull());

        Resource resource = context.request().getResource();
        assertNotNull(resource);
        Resource column = resource.getChild("column_3");
        assertNull(column);
    }
}
