package com.leforemhe.aem.site.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
@SuppressWarnings("WeakerAccess")
public class ImageLogoModelTest extends AbstractModelTest {

    private final AemContext ctx = new AemContext();

    ImageLogoModel logo;

    public void setup() {
        ctx.load().json("/com/leforemhe/aem/site/core/models/ImageLogoTest.json", "/content");
        ctx.addModelsForClasses(ImageLogoModelImpl.class);
        ctx.currentResource("/content/jcr:content/image").adaptTo(ImageLogoModel.class);
        logo = ctx.request().adaptTo(ImageLogoModel.class);
    }

    @Test
    public void getImageSrcDesktopTest(){
        String expectedFileReferenceDesktop = "/content/dam/leforemhe/image.png";

        assertNotNull(logo);
        assertEquals(expectedFileReferenceDesktop, logo.getImageSrcDesktop());
    }

    @Test
    public void getImageSrcMobileTest(){
        String expectedFileReferenceMobile = "/content/dam/leforemhe/image2.png";

        assertNotNull(logo);
        assertEquals(expectedFileReferenceMobile, logo.getImageSrcMobile());
    }

}
