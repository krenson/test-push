package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.services.RteHyperlinkService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ForModel(
        models = {LogoModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/LogoTest.json"
)
@SuppressWarnings("WeakerAccess")
public class LogoModelTest extends AbstractModelTest {

    @Mock
    RteHyperlinkService hyperlinkService;

    LogoModel logo;

    public void setup() {
        lenient().when(hyperlinkService.getVanityPathIfNotAsset(anyString())).thenReturn("/index.html");
        context.registerService(hyperlinkService);
        context.addModelsForClasses(LogoModel.class);
        logo = context.currentResource("/content/demo-page/jcr:content/logo").adaptTo(LogoModel.class);
    }

    @Test
    public void getImageSrc(){
        String expectedFileReferenceDesktop = "/content/dam/leforemhe/image.png";

        assertNotNull(logo);
        assertEquals(expectedFileReferenceDesktop, logo.getImageSrc());
    }

    @Test
    public void getLinkTest(){
        String expectedInternalLink = "/index.html";

        assertNotNull(logo);
        assertEquals(expectedInternalLink, logo.getLink());
    }
}
