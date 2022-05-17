package com.leforemhe.aem.site.core.servlet;

import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.google.gson.Gson;
import com.leforemhe.aem.site.core.models.pojo.Abbreviation;
import com.leforemhe.aem.site.core.services.RteAbbreviationConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;

/**
 * Servlet to fetch the configuration of the Abbreviations in a JSON format
 */
@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_RESOURCE_TYPES + "=" + GlobalConfig.APPNAME + "/rte-plugins/abbreviation",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_EXTENSIONS + "=json",
                SLING_SERVLET_SELECTORS + "=leforemhe"
        })
public class RteAbbreviationsServlet extends SlingSafeMethodsServlet {

    @Reference
    private transient RteAbbreviationConfigService abbreviationConfigService;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        Map<String, List<Abbreviation>> items = new HashMap<>();

        for (String abbreviationItem : abbreviationConfigService.getConfig().abbreviations()) {
            String definition = abbreviationItem.contains(":") ? abbreviationItem.substring(abbreviationItem.indexOf(':') + 1) : abbreviationItem;
            String language = abbreviationItem.contains(":") ? abbreviationItem.substring(0, abbreviationItem.indexOf(':')).toLowerCase() : "fr";
            String word = definition.substring(0, definition.indexOf('='));
            String abbr = definition.substring(definition.indexOf('=') + 1);
            Abbreviation abbreviation = new Abbreviation(word, abbr);
            if(!items.containsKey(language)){
                items.put(language, new ArrayList<>());
            }
            items.get(language).add(abbreviation);

        }
        String json = gson.toJson(items);

        response.setStatus(200);
        response.addHeader("Content-type", "application/json");
        response.getWriter().print(json);
    }
}
