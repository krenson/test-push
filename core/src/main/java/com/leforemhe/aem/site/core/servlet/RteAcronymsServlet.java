package com.leforemhe.aem.site.core.servlet;

import com.google.gson.Gson;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.models.pojo.Acronym;
import com.leforemhe.aem.site.core.services.RteAcronymConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_SELECTORS;

/**
 * Servlet to fetch the configuration of the Acronyms in a JSON format
 */
@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_RESOURCE_TYPES + "=" + GlobalConfig.APPNAME + "/rte-plugins/acronym",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_EXTENSIONS + "=json",
                SLING_SERVLET_SELECTORS + "=leforemhe"
        })
public class RteAcronymsServlet extends SlingSafeMethodsServlet {

    @Reference
    private transient RteAcronymConfigService acronymConfigService;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws ServletException, IOException {
        List<Acronym> acronyms = new ArrayList<>();
        for(String acronymString : acronymConfigService.getConfig().acronyms()) {
            String text = acronymString.substring(0, acronymString.indexOf('='));
            String acronym = acronymString.substring(acronymString.indexOf('=')+1);
            acronyms.add(new Acronym(text, acronym));
        }
        Gson gson = new Gson();
        String json = gson.toJson(acronyms);

        response.setStatus(200);
        response.addHeader("Content-type", "application/json");
        response.getWriter().print(json);
    }
}
