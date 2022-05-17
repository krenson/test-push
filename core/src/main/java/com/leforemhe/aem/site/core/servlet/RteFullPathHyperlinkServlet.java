package com.leforemhe.aem.site.core.servlet;

import com.google.gson.Gson;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import com.leforemhe.aem.site.core.services.RteHyperlinkService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;

/**
 * Servlet to fetch the full path url from the vanity path of a page.
 */
@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_PATHS + "=/bin/fullPath",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_EXTENSIONS + "=json",
        })
public class RteFullPathHyperlinkServlet extends SlingSafeMethodsServlet {

    @Reference
    private transient GlobalConfigService globalConfigService;

    public static final String FULL_PATH_PARAMETER = "fullPath";
    public static final String VANITY_PATH_PARAMETER = "vanityPath";


    @Reference
    private transient RteHyperlinkService vanityHyperlinkService;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws IOException {
        Map<String, String> responseParam = new HashMap<>();

        if (request.getParameter(VANITY_PATH_PARAMETER) != null && !request.getParameter(VANITY_PATH_PARAMETER).equals("undefined")) {
            String fullPath = vanityHyperlinkService.getFullPath(request.getParameter(VANITY_PATH_PARAMETER));
            if (fullPath.startsWith(globalConfigService.getConfig().contentPath())) {
                // Internal link -> remove extension
                fullPath = removeHtmlExtension(fullPath);
            }
            responseParam.put(FULL_PATH_PARAMETER, fullPath);

            response.setStatus(200);
        } else {
            responseParam.put("info", "No vanity path parameter found in request!");
            responseParam.put(FULL_PATH_PARAMETER, request.getParameter(VANITY_PATH_PARAMETER));
            response.setStatus(200);
        }

        response.addHeader("Content-type", "application/json");
        response.getWriter().print(new Gson().toJson(responseParam));
    }

    private String removeHtmlExtension(String fullPath) {
        if (fullPath.endsWith(".html")) {
            return fullPath.substring(0, fullPath.length() - 5);
        }
        return fullPath;
    }
}
