package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {NavigationModel.class, NavigationItemModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/Banniere-Carrousel.json"
)
@SuppressWarnings("WeakerAccess")
public class BanniereCarrouselModelTest extends AbstractModelTest {

    @Test
    public void getTitleTest(){
        final String EXPECTED = "Banniere title";
        context.currentResource("/content/demo-page/jcr:content/banniere-full");
        Resource banniereResource = context.request().getResource();
        BanniereCarrouselModel banniereCarrouselModel = banniereResource.adaptTo(BanniereCarrouselModel.class);

        assertNotNull(banniereCarrouselModel);
        assertEquals(EXPECTED, banniereCarrouselModel.getTitle());
    }

    @Test
    public void getDescriptionTest(){
        final String EXPECTED = "Banniere description";
        context.currentResource("/content/demo-page/jcr:content/banniere-full");
        Resource banniereResource = context.request().getResource();
        BanniereCarrouselModel banniereCarrouselModel = banniereResource.adaptTo(BanniereCarrouselModel.class);

        assertNotNull(banniereCarrouselModel);
        assertEquals(EXPECTED, banniereCarrouselModel.getDescription());
    }

    @Test
    public void getImageTest(){
        final String EXPECTED = "/content/dam/leforemhe/image.png";
        context.currentResource("/content/demo-page/jcr:content/banniere-full");
        Resource banniereResource = context.request().getResource();
        BanniereCarrouselModel banniereCarrouselModel = banniereResource.adaptTo(BanniereCarrouselModel.class);

        assertNotNull(banniereCarrouselModel);
        assertEquals(EXPECTED, banniereCarrouselModel.getImage());
    }

    @Test
    public void getImageAltTest(){
        final String EXPECTED = "Banniere image alt";
        context.currentResource("/content/demo-page/jcr:content/banniere-full");
        Resource banniereResource = context.request().getResource();
        BanniereCarrouselModel banniereCarrouselModel = banniereResource.adaptTo(BanniereCarrouselModel.class);

        assertNotNull(banniereCarrouselModel);
        assertEquals(EXPECTED, banniereCarrouselModel.getImageAlt());
    }

    @Test
    public void getBodyTest(){
        final String EXPECTED = "<p><h3>Banniere</h3>This is the body of the banner.</p>";
        context.currentResource("/content/demo-page/jcr:content/banniere-full");
        Resource banniereResource = context.request().getResource();
        BanniereCarrouselModel banniereCarrouselModel = banniereResource.adaptTo(BanniereCarrouselModel.class);

        assertNotNull(banniereCarrouselModel);
        assertEquals(EXPECTED, banniereCarrouselModel.getBody());
    }

    @Test
    public void getLinkReference(){
        final String EXPECTED = "Banner reference";
        context.currentResource("/content/demo-page/jcr:content/banniere-full");
        Resource banniereResource = context.request().getResource();
        BanniereCarrouselModel banniereCarrouselModel = banniereResource.adaptTo(BanniereCarrouselModel.class);

        assertNotNull(banniereCarrouselModel);
        NavigationItemModel reference = banniereCarrouselModel.getReference();
        assertNotNull(reference);
        assertEquals(EXPECTED, reference.getLabel());
    }
}
