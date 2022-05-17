package com.leforemhe.aem.site.core.services;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.WCMMode;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * HTML Content Service
 */
@Component(
        service = HTMLContentService.class,
        immediate = true
)
public class HTMLContentService {

    private static final Logger LOG = LoggerFactory.getLogger(HTMLContentService.class);

    @Reference
    private RequestResponseFactory requestResponseFactory;
    @Reference
    private SlingRequestProcessor requestProcessor;

    /**
     * Get the the HTML content of a specified path
     * @param path Path of the HTML to fetch
     * @param resourceResolver Resource resolver used to fetch HTML
     * @return The HTML of the specified path
     */
    public String getHTMLContent(String path, ResourceResolver resourceResolver) {
        try {
            LOG.debug("Inside getHTMLContent");
            LOG.debug("Fetching content [HTTP]: {}", path);
            HttpServletRequest req = requestResponseFactory.createRequest(HttpConstants.METHOD_GET, path);
            WCMMode.DISABLED.toRequest(req);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HttpServletResponse resp = requestResponseFactory.createResponse(out);
            requestProcessor.processRequest(req, resp, resourceResolver);
            return out.toString();
        } catch (ServletException | IOException exception) {
            LOG.error("Error while fetching {}: {}", path, exception.getMessage());
        }
        return "";
    }
}
