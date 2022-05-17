package com.leforemhe.aem.site.core.models.pojo;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserException;

/**
 * Model to parse HTML
 */
public class ParseHTML {

    private static final String LINK_ELEMENT = "LINK";
    private static final String A_ELEMENT = "A";
    private static final String SCRIPT_ELEMENT = "SCRIPT";
    private static final String IMG_ELEMENT = "IMG";
    private static final String SVG_IMAGE_ELEMENT = "IMAGE";
    private static final String XLINK_HREF = "xlink:href";
    private static final String HREF = "href";

    private String content;

    private String externalResourceURI;

    /**
     * Build a parser based on the content to parse and on the external resource URI to add on relative path
     * @param content HTML Content
     * @param externalResourceURI external resource URI domain
     */
    public ParseHTML(String content, String externalResourceURI) {
        this.content = content;
        this.externalResourceURI = externalResourceURI;
    }

    /**
     * Parse the HTML content with relative path of CSS parsed as absolute path
     * @return HTML content with relative path of CSS parsed as absolute path
     * @throws ParserException if something is wrong while parsing (htmlparser library exception)
     */
    public String parseAbsolutePathCSS() throws ParserException {
        Lexer lexer = new Lexer(new Page(content));
        StringBuilder parsedContent = new StringBuilder();
        Node node;
        while ((node = lexer.nextNode()) != null) {
            if (node instanceof TagNode && isAStylesheet((TagNode)node)) {
                TagNode tagNode = (TagNode) node;
                if(isHrefPathRelative(tagNode)) {
                    tagNode.setAttribute(HREF, String.format("%s%s", externalResourceURI, tagNode.getAttribute(HREF)));
                }
                parsedContent.append(tagNode.toHtml());
            } else {
                parsedContent.append(node.toHtml());

            }

        }
        content = parsedContent.toString();
        return parsedContent.toString();

    }

    /**
     * Parse the HTML content with relative path of JS parsed as absolute path
     * @return HTML content with relative path of JS parsed as absolute path
     * @throws ParserException if something is wrong while parsing (htmlparser library exception)
     */
    public String parseAbsolutePathJS() throws ParserException {
        Lexer lexer = new Lexer(new Page(content));
        StringBuilder parsedContent = new StringBuilder();
        Node node;
        while ((node = lexer.nextNode()) != null) {
            if (node instanceof TagNode && isAScript((TagNode)node)) {
                TagNode tagNode = (TagNode) node;
                if(isSrcPathRelative(tagNode)) {
                    tagNode.setAttribute("src", String.format("%s%s", externalResourceURI, tagNode.getAttribute("src")));
                }
                parsedContent.append(tagNode.toHtml());
            } else {
                parsedContent.append(node.toHtml());
            }
        }
        content = parsedContent.toString();
        return parsedContent.toString();
    }

    /**
     * Parse the HTML content with relative path of links parsed as absolute path
     * @return HTML content with relative path of links parsed as absolute path
     * @throws ParserException if something is wrong while parsing (htmlparser library exception)
     */
    public String parseAbsolutePathLink() throws ParserException {
        Lexer lexer = new Lexer(new Page(content));
        StringBuilder parsedContent = new StringBuilder();
        Node node;
        while ((node = lexer.nextNode()) != null) {
            if (node instanceof TagNode && isALink((TagNode)node)) {
                TagNode tagNode = (TagNode) node;
                if(isHrefPathRelative(tagNode)) {
                    tagNode.setAttribute(HREF, String.format("%s%s", externalResourceURI, tagNode.getAttribute(HREF)));
                }
                parsedContent.append(tagNode.toHtml());
            } else {
                parsedContent.append(node.toHtml());

            }
        }
        content = parsedContent.toString();
        return parsedContent.toString();
    }

    /**
     * Parse the HTML content with relative path of images parsed as absolute path
     * @return HTML content with relative path of images parsed as absolute path
     * @throws ParserException if something is wrong while parsing (htmlparser library exception)
     */
    public String parseAbsolutePathImages() throws ParserException {
        Lexer lexer = new Lexer(new Page(content));
        StringBuilder parsedContent = new StringBuilder();
        Node node;
        while ((node = lexer.nextNode()) != null) {
            if (node instanceof TagNode && isAImage((TagNode) node)) {
                TagNode tagNode = (TagNode) node;
                if (isSrcPathRelative(tagNode)) {
                    tagNode.setAttribute("src", String.format("%s%s", externalResourceURI, tagNode.getAttribute("src")));
                }
                parsedContent.append(tagNode.toHtml());
            } else if (node instanceof TagNode && isASVGImage((TagNode) node)) {
                TagNode tagNode = (TagNode) node;
                if (isXlinkHrefPathRelative(tagNode)) {
                    tagNode.setAttribute(XLINK_HREF, String.format("%s%s", externalResourceURI, tagNode.getAttribute(XLINK_HREF)));
                }
                parsedContent.append(tagNode.toHtml());
            } else {
                parsedContent.append(node.toHtml());
            }

        }
        content = parsedContent.toString();
        return parsedContent.toString();
    }

    public String getResult() {
        return content;
    }

    private boolean isXlinkHrefPathRelative(TagNode node) {
        if(node.getAttribute(XLINK_HREF) == null) {
            return false;
        }
        return node.getAttribute(XLINK_HREF).startsWith("/");
    }

    private boolean isHrefPathRelative(TagNode node) {
        if(node.getAttribute(HREF) == null) {
            return false;
        }
        return node.getAttribute(HREF).startsWith("/");
    }

    private boolean isSrcPathRelative(TagNode node) {
        if(node.getAttribute("src") == null) {
            return false;
        }
        return node.getAttribute("src").startsWith("/");
    }

    private boolean isALink(TagNode node){
        return A_ELEMENT.equals(node.getTagName()) || (LINK_ELEMENT.equals(node.getTagName()) && !isAStylesheet(node));
    }

    private boolean isAScript(TagNode node) {
        return SCRIPT_ELEMENT.equals(node.getTagName());
    }

    private boolean isAStylesheet(TagNode node) {
        return LINK_ELEMENT.equals(node.getTagName()) && "stylesheet".equals(node.getAttribute("rel"));
    }

    private boolean isAImage(TagNode node) {
        return IMG_ELEMENT.equals(node.getTagName());
    }

    private boolean isASVGImage(TagNode node) {
        return SVG_IMAGE_ELEMENT.equals(node.getTagName());
    }
}
