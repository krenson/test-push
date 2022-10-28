package com.leforemhe.aem.site.core.listeners;

import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.utils.EventUtils;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
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

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;

@Component(service = EventListener.class, immediate = true)
public class PageChangedListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(PageChangedListener.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private SlingRepository repository;

    @Reference
    private ContentFragmentUtilService contentFragmentUtilService;

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
                // Not deleting this in case we ever want to swap to listeners again (Replaced with workflow)
                //session.getWorkspace().getObservationManager().addEventListener(this, Event.PROPERTY_CHANGED, globalConfigService.getConfig().contentPath(), true, null, null, false);
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
            if (EventUtils.getEventNodeType(event).equals(Constants.CQ_PAGECONTENT)) {
                Node pageNode = resolver.getResource(event.getPath()).getParent().adaptTo(Node.class);
                if (pageNode.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getValue().toString().equals(Constants.JOB_PAGE_RESOURCE_TYPE)) {
                    do {
                        String[] eventPathParts = event.getPath().split("/");
                        int lastIndex = eventPathParts.length - 1;
                        if (eventPathParts[lastIndex].equals(Constants.CLE_METIER)) {
                            EventUtils.handleJobPageEvent(pageNode, contentFragmentUtilService);
                            session.save();
                            return;
                        }
                        events.nextEvent();
                    } while (events.hasNext());
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred", e);
        }
    }

}