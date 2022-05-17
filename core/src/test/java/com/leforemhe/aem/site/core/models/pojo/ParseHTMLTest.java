package com.leforemhe.aem.site.core.models.pojo;

import org.htmlparser.util.ParserException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParseHTMLTest {

    private static final String ORIGIN = "https://aem.test.com";

    private static final String IMG_RELATIVE_PATH = "<img src=\"/path/to/my/image.png\">";
    private static final String IMG_ABSOLUTE_PATH = "<img src=\"https://aem.test.com/path/to/my/image.png\">";
    private static final String IMG_NON_RELATIVE_PATH = "<img src=\"https://website.com/path/to/my/image.png\">";
    private static final String IMG_NO_SRC = "<img>";

    private static final String SVG_IMAGE_RELATIVE_PATH = "<image xlink:href=\"/path/to/my/image.svg\">";
    private static final String SVG_IMAGE_ABSOLUTE_PATH = "<image xlink:href=\"https://aem.test.com/path/to/my/image.svg\">";
    private static final String SVG_IMAGE_NON_RELATIVE_PATH = "<image xlink:href=\"https://website.com/path/to/my/image.svg\">";
    private static final String SVG_IMAGE_NO_XLINK_HREF = "<image>";

    private static final String CSS_RELATIVE_PATH = "<link rel=\"stylesheet\" href=\"/path/to/css.css\"/>";
    private static final String CSS_ABSOLUTE_PATH = "<link rel=\"stylesheet\" href=\"https://aem.test.com/path/to/css.css\"/>";
    private static final String CSS_NON_RELATIVE_PATH = "<link rel=\"stylesheet\" href=\"https://website.com/path/to/css.css\"/>";
    private static final String CSS_NO_HREF = "<link rel=\"stylesheet\">";

    private static final String JS_RELATIVE_PATH = "<script src=\"/etc.clientlibs/path/to/javavascript.js>\">";
    private static final String JS_ABSOLUTE_PATH = "<script src=\"https://aem.test.com/etc.clientlibs/path/to/javavascript.js>\">";
    private static final String JS_NON_RELATIVE_PATH = "<script src=\"https://website.com/path/to/javavascript.js>\">";

    private static final String LINK_RELATIVE_PATH = "<a href=\"/path/to/page.html\">";
    private static final String LINK_ABSOLUTE_PATH = "<a href=\"https://aem.test.com/path/to/page.html\">";
    private static final String LINK_NON_RELATIVE_PATH = "<a href=\"https://website.com/path/to/page.html\">";

    private static final String AMP_LINK_RELATIVE_PATH = "<link rel=\"amphtml\" href=\"/path/to/page.amp.html\"/>";
    private static final String AMP_LINK_ABSOLUTE_PATH = "<link rel=\"amphtml\" href=\"https://aem.test.com/path/to/page.amp.html\"/>";
    private static final String AMP_LINK_NON_RELATIVE_PATH = "<link rel=\"amphtml\" href=\"https://website.com/path/to/page.amp.html\"/>";

    private static final String HTML_RELATIVE_PATH = "<html>\n" +
            "<head>\n" +
            AMP_LINK_RELATIVE_PATH+"\n" +
            CSS_RELATIVE_PATH+"\n" +
            "</head>\n" +
            "<body>\n" +
            "<div>\n" +
            IMG_RELATIVE_PATH+"\n" +
            "<svg>\n"+
            SVG_IMAGE_RELATIVE_PATH+"\n" +
            "</svg>\n" +
            LINK_RELATIVE_PATH+"\n" +
            "</div>\n" +
            JS_RELATIVE_PATH +"\n" +
            JS_NON_RELATIVE_PATH + "\n" +
            "</body>\n" +
            "</html>";

    private static final String HTML_ABSOLUTE_PATH = "<html>\n" +
            "<head>\n" +
            AMP_LINK_ABSOLUTE_PATH+"\n" +
            CSS_ABSOLUTE_PATH+"\n" +
            "</head>\n" +
            "<body>\n" +
            "<div>\n" +
            IMG_ABSOLUTE_PATH+"\n" +
            "<svg>\n"+
            SVG_IMAGE_ABSOLUTE_PATH+"\n" +
            "</svg>\n" +
            LINK_ABSOLUTE_PATH+"\n" +
            "</div>\n" +
            JS_ABSOLUTE_PATH +"\n" +
            JS_NON_RELATIVE_PATH + "\n" +
            "</body>\n" +
            "</html>";

    private static final String FOOTER_JAVASCRIPTS = JS_RELATIVE_PATH + "\n" +
            JS_NON_RELATIVE_PATH + "\n";

    @Test
    void parseImagePath() throws ParserException {
        ParseHTML parser = new ParseHTML(IMG_RELATIVE_PATH, ORIGIN);
        assertEquals(IMG_ABSOLUTE_PATH, parser.parseAbsolutePathImages());
    }

    @Test
    void parseSVGImagePath() throws ParserException {
        ParseHTML parser = new ParseHTML(SVG_IMAGE_RELATIVE_PATH, ORIGIN);
        assertEquals(SVG_IMAGE_ABSOLUTE_PATH, parser.parseAbsolutePathImages());
    }

    @Test
    void parseCSSPath() throws ParserException {
        ParseHTML parser = new ParseHTML(CSS_RELATIVE_PATH, ORIGIN);
        assertEquals(CSS_ABSOLUTE_PATH, parser.parseAbsolutePathCSS());
    }

    @Test
    void parseJSPath() throws ParserException {
        ParseHTML parser = new ParseHTML(JS_RELATIVE_PATH, ORIGIN);
        assertEquals(JS_ABSOLUTE_PATH, parser.parseAbsolutePathJS());
    }

    @Test
    void parseLinkPath() throws ParserException {
        ParseHTML parser = new ParseHTML(LINK_RELATIVE_PATH, ORIGIN);
        assertEquals(LINK_ABSOLUTE_PATH, parser.parseAbsolutePathLink());
    }

    @Test
    void parseNonCSSLinkPath() throws ParserException {
        ParseHTML parser = new ParseHTML(AMP_LINK_RELATIVE_PATH, ORIGIN);
        assertEquals(AMP_LINK_ABSOLUTE_PATH, parser.parseAbsolutePathLink());
    }

    @Test
    void parseCompleteHTML() throws ParserException {
        ParseHTML parser = new ParseHTML(HTML_RELATIVE_PATH, ORIGIN);
        parser.parseAbsolutePathLink();
        parser.parseAbsolutePathImages();
        parser.parseAbsolutePathCSS();
        parser.parseAbsolutePathJS();
        assertEquals(HTML_ABSOLUTE_PATH, parser.getResult());
    }

    @Test
    void dontParseImagePath() throws ParserException {
        ParseHTML parser = new ParseHTML(IMG_NON_RELATIVE_PATH, ORIGIN);
        assertEquals(IMG_NON_RELATIVE_PATH, parser.parseAbsolutePathImages());
    }

    @Test
    void dontParseSVGImagePath() throws ParserException {
        ParseHTML parser = new ParseHTML(SVG_IMAGE_NON_RELATIVE_PATH, ORIGIN);
        assertEquals(SVG_IMAGE_NON_RELATIVE_PATH, parser.parseAbsolutePathImages());
    }

    @Test
    void dontParseCSSPath() throws ParserException {
        ParseHTML parser = new ParseHTML(CSS_NON_RELATIVE_PATH, ORIGIN);
        assertEquals(CSS_NON_RELATIVE_PATH, parser.parseAbsolutePathCSS());
    }

    @Test
    void dontParseJSPath() throws ParserException {
        ParseHTML parser = new ParseHTML(JS_NON_RELATIVE_PATH, ORIGIN);
        assertEquals(JS_NON_RELATIVE_PATH, parser.parseAbsolutePathJS());
    }

    @Test
    void dontParseLinkPath() throws ParserException {
        ParseHTML parser = new ParseHTML(LINK_NON_RELATIVE_PATH, ORIGIN);
        assertEquals(LINK_NON_RELATIVE_PATH, parser.parseAbsolutePathLink());
    }

    @Test
    void dontParseNonCSSLinkPath() throws ParserException {
        ParseHTML parser = new ParseHTML(AMP_LINK_NON_RELATIVE_PATH, ORIGIN);
        assertEquals(AMP_LINK_NON_RELATIVE_PATH, parser.parseAbsolutePathLink());
    }

    @Test
    void noImagePath() throws ParserException {
        ParseHTML parser = new ParseHTML(IMG_NO_SRC, ORIGIN);
        assertEquals(IMG_NO_SRC, parser.parseAbsolutePathImages());
    }

    @Test
    void noSVGImagePath() throws ParserException {
        ParseHTML parser = new ParseHTML(SVG_IMAGE_NO_XLINK_HREF, ORIGIN);
        assertEquals(SVG_IMAGE_NO_XLINK_HREF, parser.parseAbsolutePathImages());
    }

    @Test
    void noCSSPath() throws ParserException {
        ParseHTML parser = new ParseHTML(CSS_NO_HREF, ORIGIN);
        assertEquals(CSS_NO_HREF, parser.parseAbsolutePathCSS());
    }

}
