package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models = {EncadreBleuCharronModel.class, Text.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/EncadreBleuCharronModel.json"
)
public class EncadreBleuCharronModelTest extends AbstractModelTest {

    @Test
    void getModel() {
        final String expectedTitle = "Titre";
        final String expectedText = "<p>Text</p>";
        context.currentResource("/content/demo-page/jcr:content/encadrebleucharron");
        EncadreBleuCharronModel model = context.request().getResource().adaptTo(EncadreBleuCharronModel.class);
        assertNotNull(model);
        assertEquals(expectedTitle, model.getTitle());
        assertEquals(expectedText, model.getText());
        assertTrue(model.isRichText());
    }
}
