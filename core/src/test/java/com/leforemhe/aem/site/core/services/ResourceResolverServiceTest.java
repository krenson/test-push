package com.leforemhe.aem.site.core.services;

import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import com.leforemhe.aem.site.core.AbstractAEMTest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.jcr.Session;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

public class ResourceResolverServiceTest extends AbstractAEMTest {

    @Mock
    private ResourceResolverFactory factory;
    @Mock
    private Session session;
    @Mock
    ResourceResolver resourceResolver;
    @InjectMocks
    private ResourceResolverService resourceResolverService;

    @Override
    public void setup() throws LoginException {
        lenient().when(factory.getResourceResolver(eq(Collections.singletonMap(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, session)))).thenReturn(resourceResolver);
    }

    @Test
    void getFromSession() {
        assertEquals(resourceResolver, resourceResolverService.getResourceResolverFromSession(session));
    }
}
