package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ForModel(
        models = {ReferenceMultiComboModel.class, List.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/ReferenceMultiComboTest.json"
)
@SuppressWarnings("WeakerAccess")
public class ReferenceMultiComboModelTests extends AbstractModelTest {
    private static final String LIST_HTML_ID = "htmlid";

    @Mock
    private List resourceSuperType;

    public void setup() {
        lenient().when(resourceSuperType.getId()).thenReturn(LIST_HTML_ID);
    }

    @Test
    public void testTitle(){
        context.currentResource("/content/demo-page/jcr:content/referencemulticombo");
        ReferenceMultiComboModel referenceMultiComboModel = context.request().getResource().adaptTo(ReferenceMultiComboModel.class);
        assertNotNull(referenceMultiComboModel);
        assertEquals("Title", referenceMultiComboModel.getTitle());
    }

    @Test
    public void testDescription(){
        context.currentResource("/content/demo-page/jcr:content/referencemulticombo");
        ReferenceMultiComboModel referenceMultiComboModel = context.request().getResource().adaptTo(ReferenceMultiComboModel.class);
        assertNotNull(referenceMultiComboModel);
        assertEquals("Description", referenceMultiComboModel.getDescription());
    }

    @Test
    public void testId(){
        ReferenceMultiComboModel referenceMultiComboModel = new ReferenceMultiComboModel(resourceSuperType);
        assertNotNull(referenceMultiComboModel);
        assertEquals(LIST_HTML_ID, referenceMultiComboModel.getId());
        verify(resourceSuperType, times(1)).getId();
    }

    @Test
    public void testGetReferences(){
        ReferenceMultiComboModel referenceMultiComboModel = new ReferenceMultiComboModel(resourceSuperType);
        assertNotNull(referenceMultiComboModel);
        referenceMultiComboModel.getReferences();
        verify(resourceSuperType, times(1)).getListItems();
    }

    @Test
    public void testGetReferencesEmpty(){
        context.currentResource("/content/demo-page/jcr:content/referencemulticomboempty");
        ReferenceMultiComboModel referenceMultiComboModel = context.request().getResource().adaptTo(ReferenceMultiComboModel.class);
        assertNotNull(referenceMultiComboModel);
        assertNotNull(referenceMultiComboModel.getReferences());
        assertTrue(referenceMultiComboModel.getReferences().isEmpty());
    }
}
