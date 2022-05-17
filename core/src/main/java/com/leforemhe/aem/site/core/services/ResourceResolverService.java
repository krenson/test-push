package com.leforemhe.aem.site.core.services;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Collections;

/**
 * OSGI service to get a resource resolver
 */
@Component(
        service = ResourceResolverService.class,
        immediate = true
)
public class ResourceResolverService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceResolverService.class);

    /**
     * Get a Resource Resolver from a session object
     * @param session Session from where to get Resource Resolver
     * @return Resource resolver from the specified Session
     */
    public ResourceResolver getResourceResolverFromSession(Session session) {
        ResourceResolver resolver = null;
        try {
            resolver = resourceResolverFactory.getResourceResolver(Collections.singletonMap(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, session));
        }
        catch (LoginException e) {
            LOG.error("Error while getting session's resource resolver: {}", e.getMessage());
        }
        return resolver;
    }
}
