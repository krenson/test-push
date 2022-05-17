package com.leforemhe.aem.site.core.models;

import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
public class RemarqueModelTest extends AbstractModelTest {

    public void setup() {
        context.addModelsForClasses(RemarqueModel.class);
        context.load().json("/com/leforemhe/aem/site/core/models/RemarqueTest.json", "/content/demo-page");

        context.registerService(ModelFactory.class, modelFactory,
                org.osgi.framework.Constants.SERVICE_RANKING, Integer.MAX_VALUE);
    }

    @Test
    public void remarqueGettersTest() {

        String expectedColor = "bleuPrusse34495E";
        String expectedDescription = "remarque description";
        String expectedTitle = "remarque title";
        String expectedFileReference = "/content/dam/leforemhe/image.png";
        boolean isExternalExpected = true;
        String expectedExternalLink = "https://agenda.leforemhe.be/";

        context.currentResource("/content/demo-page/jcr:content/remarque");
        RemarqueModel remarque = context.request().getResource().adaptTo(RemarqueModel.class);

        assertNotNull(remarque);
        assertNotNull(remarque.getReference());
        assertEquals(expectedColor, remarque.getColor());
        assertEquals(expectedTitle, remarque.getTitle());
        assertEquals(expectedFileReference, remarque.getImageSrc());
        assertEquals(isExternalExpected, remarque.getReference().isExternal());
        assertEquals(expectedExternalLink, remarque.getReference().getLinkReference());
        assertEquals(expectedDescription, remarque.getReference().getDescription());
    }

    @Test
    public void getRemarqueWithInternalLink() {

        String expectedColor = "orangeCarotteD8733B";
        String expectedDescription = "remarque description 2";
        String expectedTitle = "remarque title 2";
        String expectedFileReference = "/content/dam/leforemhe/image2.png";
        boolean isExternalExpected = false;
        String expectedExternalLink = "/content/page2";

        context.currentResource("/content/demo-page/jcr:content/remarque2");
        RemarqueModel remarque = context.request().getResource().adaptTo(RemarqueModel.class);

        assertNotNull(remarque);
        assertNotNull(remarque.getReference());
        assertEquals(expectedColor, remarque.getColor());
        assertEquals(expectedTitle, remarque.getTitle());
        assertEquals(expectedFileReference, remarque.getImageSrc());
        assertEquals(isExternalExpected, remarque.getReference().isExternal());
    }
}
