package com.leforemhe.aem.site.core.servlet;

import com.leforemhe.aem.site.core.config.GlobalConfig;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Map;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;

/**
 * Mock Servlet to handle Contact Form
 */
@Component(service = Servlet.class,
        property = {
                SLING_SERVLET_RESOURCE_TYPES + "=" + GlobalConfig.APPNAME + "/components/site/forms/form",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
                SLING_SERVLET_EXTENSIONS + "=img",
                SLING_SERVLET_SELECTORS + "=leforemhe"
        }
)
public class ContactFormServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ContactFormServlet.class);

    @Override
    protected void doPost(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws IOException {
        LOG.debug("Inside doPost");
        LOG.debug("Received contact form request:");
        Map<String, String[]> params = request.getParameterMap();
        params.keySet().forEach(key -> LOG.debug("\t {} : {}", key, "fichier".equals(key) ? "{file}" : params.get(key)));
        response.sendRedirect("/content/leforemhe/fr/pages-non-places/formulaire-confirmation-envoi.html?wcmmode=disabled");
    }
}
