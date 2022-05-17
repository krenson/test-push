package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.AbstractAEMTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserServiceTest extends AbstractAEMTest {

    private static String ORIGIN = "https://aem.test.com";

    private static final String IMG_RELATIVE_PATH = "<img src=\"/path/to/my/image.png\">";
    private static final String IMG_ABSOLUTE_PATH = "<img src=\"https://aem.test.com/path/to/my/image.png\">";

    private static final String CSS_RELATIVE_PATH = "<link rel=\"stylesheet\" href=\"/path/to/css.css\"/>";
    private static final String CSS_ABSOLUTE_PATH = "<link rel=\"stylesheet\" href=\"https://aem.test.com/path/to/css.css\"/>";

    private static final String JS_RELATIVE_PATH = "<script src=\"/etc.clientlibs/path/to/javavascript.js>\">";
    private static final String JS_ABSOLUTE_PATH = "<script src=\"https://aem.test.com/etc.clientlibs/path/to/javavascript.js>\">";

    private static final String LINK_RELATIVE_PATH = "<a href=\"/path/to/page.html\">";
    private static final String LINK_ABSOLUTE_PATH = "<a href=\"https://aem.test.com/path/to/page.html\">";

    private static final String HTML_RELATIVE_PATH = "<html>\n" +
            "<head>\n" +
            CSS_RELATIVE_PATH+"\n" +
            "</head>\n" +
            "<body>\n" +
            "<div>\n" +
            IMG_RELATIVE_PATH+"\n" +
            LINK_RELATIVE_PATH+"\n" +
            "</div>\n" +
            JS_RELATIVE_PATH +"\n" +
            "</body>\n" +
            "</html>";

    private ParserService parserService;

    @Override
    public void setup() {
        parserService = context.registerInjectActivateService(new ParserService());
    }

    @Test
    void parseCSS() {
        assertEquals(CSS_ABSOLUTE_PATH, parserService.parseAbsolutePathClientLibsCSS(CSS_RELATIVE_PATH,ORIGIN));
    }

    @Test
    void parseJS() {
        assertEquals(JS_ABSOLUTE_PATH, parserService.parseAbsolutePathClientLibsJavascript(JS_RELATIVE_PATH, ORIGIN));
    }

    @Test
    void parseImages() {
        assertEquals(IMG_ABSOLUTE_PATH, parserService.parseAbsolutePathImg(IMG_RELATIVE_PATH, ORIGIN));
    }

    @Test
    void parseLinks() {
        assertEquals(LINK_ABSOLUTE_PATH, parserService.parseAbsolutePathLink(LINK_RELATIVE_PATH, ORIGIN));
    }

}
