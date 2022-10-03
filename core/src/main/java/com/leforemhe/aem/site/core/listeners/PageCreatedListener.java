package com.leforemhe.aem.site.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.utils.EventUtils;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EventListener.class, immediate = true)
public class PageCreatedListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(PageCreatedListener.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private ContentFragmentUtilService contentFragmentUtilService;

    @Reference
    private SlingRepository repository;

    @Reference
    private GlobalConfigService globalConfigService;

    private Session session;
    private ResourceResolver resolver;

    @Activate
    protected void activate(ComponentContext componentContext) {
        LOG.info("Activating the observation");
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(ResourceResolverFactory.SUBSERVICE, globalConfigService.getConfig().systemUser());
            resolver = resolverFactory.getServiceResourceResolver(params);
            session = resolver.adaptTo(Session.class);
            if (session != null) {
                LOG.info("Session created");
                session.getWorkspace().getObservationManager().addEventListener(this, Event.NODE_ADDED, globalConfigService.getConfig().contentPath(), true, null, null, false);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Deactivate
    protected void deactivate() {
        if (session != null) {
            session.logout();
        }
    }

    @Override
    public void onEvent(EventIterator events) {
        try {
            Event event = events.nextEvent();
            if (EventUtils.getEventNodeType(event).equals(Constants.CQ_PAGE)) {
                while (events.hasNext()) {
                    event = events.nextEvent();
                    String eventPath = event.getPath();
                    Node pageNode = resolver.getResource(eventPath).adaptTo(Node.class);
                    if (pageNode.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getValue().toString().equals(Constants.JOB_PAGE_RESOURCE_TYPE)) {
                        EventUtils.handleJobPageEvent(pageNode, contentFragmentUtilService);
                        session.save();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred", e);
        }
    }

}