package com.leforemhe.aem.site.core.servlet;

import com.google.gson.Gson;
import com.leforemhe.aem.site.core.config.GlobalConfig;
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
 * Servlet to fetch the vanity url of a full path page url.
 */
@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_RESOURCE_TYPES + "=" + GlobalConfig.APPNAME + "/components/site/servlet",
                SLING_SERVLET_RESOURCE_TYPES + "=" + GlobalConfig.APPNAME + "/components/site/page",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_EXTENSIONS + "=json",
                SLING_SERVLET_SELECTORS + "=leforemhe",
        })
public class RteVanityHyperlinkServlet extends SlingSafeMethodsServlet {

    protected static final String[] RESOURCE_TYPES = {GlobalConfig.APPNAME + "/components/site/servlet", GlobalConfig.APPNAME + "/components/site/page"};
    public static final String FULL_PATH_PARAMETER = "fullPath";
    public static final String VANITY_PATH_PARAMETER = "vanityPath";

    @Reference
    private transient RteHyperlinkService vanityHyperlinkService;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws IOException {
        String vanityPath = null;
        Map<String, String> responseParam = new HashMap<>();
        if(request.getParameter("type").equals("asset")){
            vanityPath = vanityHyperlinkService.getVanityPathAsset(request.getParameter("fullPath"));
        } else {
            var parentResource = request.getResource().getParent();
            if (parentResource != null) {
                vanityPath = vanityHyperlinkService.getVanityPathIfNotAsset(parentResource.getPath());
            }
        }
        if(vanityPath != null){
            responseParam.put(VANITY_PATH_PARAMETER, vanityPath);
            response.setStatus(200);
        } else {
            responseParam.put("error", "No resource found in request!");
            response.setStatus(412);
        }
        response.addHeader("Content-type", "application/json");
        response.getWriter().print(new Gson().toJson(responseParam));
    }

}
