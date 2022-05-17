package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.models.pojo.ParseHTML;
import org.htmlparser.util.ParserException;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser service to Parse HTML
 */
@Component(
        service = ParserService.class,
        immediate = true
)
public class ParserService {

    private static final Logger LOG = LoggerFactory.getLogger(ParserService.class);

    /**
     * Parse relative path of CSS on absolute path
     * @param html HTML content
     * @param uri Domain URI to parse on relative path
     * @return HTML with CSS relative path parsed as absolute path
     */
    public String parseAbsolutePathClientLibsCSS(String html, String uri) {
        ParseHTML parser = new ParseHTML(html, uri);
        String content = html;
        try {
            content = parser.parseAbsolutePathCSS();
        } catch (ParserException e) {
            LOG.error("Something went wrong while parsing CSS absolute path: {}", e.getMessage());
        }
        return content;
    }

    /**
     * Parse relative path of Javascript as absolute path
     * @param html HTML content
     * @param uri Domain URI to parse on relative path
     * @return HTML with Javascript relative path parsed as absolute path
     */
    public String parseAbsolutePathClientLibsJavascript(String html, String uri) {
        ParseHTML parser = new ParseHTML(html, uri);
        String content = html;
        try {
            content = parser.parseAbsolutePathJS();
        } catch (ParserException e) {
            LOG.error("Something went wrong while parsing JS absolute path: {}", e.getMessage());
        }
        return content;
    }

    /**
     * Parse relative path of links as absolute path
     * @param html HTML content
     * @param uri Domain URI to parse on relative path
     * @return HTML with links relative path parsed as absolute path
     */
    public String parseAbsolutePathLink(String html, String uri) {
        ParseHTML parser = new ParseHTML(html, uri);
        String content = html;
        try {
            content = parser.parseAbsolutePathLink();
        } catch (ParserException e) {
            LOG.error("Something went wrong while parsing links absolute path: {}", e.getMessage());
        }
        return content;
    }

    /**
     * Parse relative path of images as absolute path
     * @param html HTML content
     * @param uri Domain URI to parse on relative path
     * @return HTML with links relative path parsed as absolute path
     */
    public String parseAbsolutePathImg(String html, String uri) {
        ParseHTML parser = new ParseHTML(html, uri);
        String content = html;
        try {
            content = parser.parseAbsolutePathImages();
        } catch (ParserException e) {
            LOG.error("Something went wrong while parsing images absolute path: {}", e.getMessage());
        }
        return content;
    }

}
