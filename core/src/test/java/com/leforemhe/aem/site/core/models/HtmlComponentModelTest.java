package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {HtmlComponentModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/HtmlComponentTests.json"
)
@SuppressWarnings("WeakerAccess")
public class HtmlComponentModelTest extends AbstractModelTest {

    private static final String PAGE_PATH = "/content/demo-page";

    @Test
    public void getHtmlTest(){
        final String expected = "<p>&nbsp;</p>\n<div class=\"searchBandJobsCV\"><div class=\"rechercheProfil\"><p>Repérez vos futurs collaborateurs!</p>\n<div><a class=\"btBleu2196F3\" title=\"Chercher un candidat parmis les profils encodés sur le site\" href=\"/entreprises/chercher-un-candidat.html\">Chercher un candidat</a><img alt=\"loupe candidat\" src=\"/content/dam/leforemhe/fr/loupe-candidat.png\"></div>\n</div>\n</div>\n<p>&nbsp;</p>";
        context.currentResource("/content/demo-page/jcr:content/htmlcomponent");
        Resource resource = context.request().getResource();
        HtmlComponentModel htmlComponentModel = resource.adaptTo(HtmlComponentModel.class);
        assertNotNull(htmlComponentModel);
        assertEquals(expected, htmlComponentModel.getHtml());
    }
}
