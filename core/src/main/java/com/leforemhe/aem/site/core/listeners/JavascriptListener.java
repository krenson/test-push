package com.leforemhe.aem.site.core.listeners;

import com.leforemhe.aem.site.core.config.JavascriptConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.services.DynamicJavascriptConfigService;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import com.leforemhe.aem.site.core.services.JavascriptToFileService;
import com.leforemhe.aem.site.core.services.ServiceUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Event Listener to handle Dynamic Javascript changes
 */
@Component(
        immediate = true,
        service = EventListener.class
)
public class JavascriptListener implements EventListener {
    private static final String ENVIRONMENT = "environment";

    private static final Logger LOG = LoggerFactory.getLogger(JavascriptListener.class);

    @Reference
    private SlingRepository repository;
    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private JavascriptToFileService javascriptToFileService;
    @Reference
    private DynamicJavascriptConfigService dynamicJavascriptConfigService;
    @Reference
    private GlobalConfigService globalConfigService;

    private GlobalConfig globalConfig;

    private final Map<String, Instant> nodeModifications = new HashMap<>();

    // Added annotation to avoid a "false positive" during scanning code
    // https://experienceleaguecommunities.adobe.com/t5/adobe-experience-manager/aem-6-5-approach-to-avoid-cloud-manager-aem-rule-for-working/td-p/310236
    @SuppressWarnings({"AEM Rules:AEM-3","AEM Rules:AEM-7"})
    private Session adminSession = null;

    /**
     * Adding the event listener on activation
     * @param context Component context
     */
    @Activate
    public void activate(ComponentContext context) {
        LOG.debug("Inside activate");
        globalConfig = globalConfigService.getConfig();
        JavascriptConfig javascriptConfig = dynamicJavascriptConfigService.getConfig();

        LOG.debug("Activating JavascriptListener.class");
        try{
            LOG.debug("Running OSGi configuration: {}", globalConfigService.getConfig().appName());
            LOG.debug("System user: {}", globalConfig.systemUser());
            LOG.debug("Path to listen: {}", javascriptConfig.pathToListen());
            if(adminSession == null){
                adminSession = repository.loginService(globalConfig.systemUser(), null);
            }
            LOG.debug("Admin session permission (ACTION_READ): {}", adminSession.hasPermission(javascriptConfig.pathToListen(), Session.ACTION_READ));
            LOG.debug("Admin session permission (ACTION_ADD_NODE): {}", adminSession.hasPermission(javascriptConfig.pathToListen(), Session.ACTION_ADD_NODE));
            adminSession.getWorkspace().getObservationManager().addEventListener(
                    this,
                    Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED,
                    javascriptConfig.pathToListen(),
                    true,null, null,false);
            LOG.debug("JavascriptListener.class was activated!");
        }catch (RepositoryException e){
            LOG.error("Failed to activate JavascriptListener: {}", e.getMessage());
        }
    }

    /**
     * Deactivating the event listener on activation
     */
    @Deactivate
    public void deactivate() {
        LOG.debug("Inside deactivate");
        if (adminSession != null) {
            adminSession.logout();
        }
        LOG.debug("JavascriptListener.class was deactivated!");
    }

    @Override
    public void onEvent(EventIterator eventIterator) {
        LOG.debug("Inside onEvent");
        try (ResourceResolver resourceResolver = ServiceUtils.getResourceResolver(resolverFactory, globalConfig.systemUser())){
            while (eventIterator.hasNext()){
                if (resourceResolver == null) {
                    throw new RepositoryException("ServiceUtils.getResourceResolver() method return null");
                }
                Event event = eventIterator.nextEvent();
                String propertyPath = event.getPath();
                LOG.debug("PropertyPath: {}", propertyPath);
                String componentPath = ServiceUtils.getComponentPath(propertyPath);
                LOG.debug("Get componentPath: {}", componentPath);
                Resource componentResource = Objects.requireNonNull(resourceResolver).getResource(componentPath);
                ValueMap componentProperties = Objects.requireNonNull(componentResource).getValueMap();
                String folderName = componentProperties.get("filename", String.class);
                LOG.debug("Component resource {} with {} properties", componentResource.getName(), componentProperties.size());
                LOG.debug("Properties: {}", componentProperties);
                Date updateDate = (Date)componentProperties
                        .getOrDefault(com.day.cq.commons.jcr.JcrConstants.JCR_LASTMODIFIED,
                                Date.from(Instant.now().minusSeconds(86400)));
                LOG.debug("Date: {}", updateDate.getTime());
                String environment = componentProperties.get(ENVIRONMENT, String.class);
                LOG.debug("A javascript event has been triggered with environment {}!", environment);
                if(!nodeModifications.containsKey(componentPath)
                        || updateDate.toInstant().isAfter(nodeModifications.get(componentPath))){
                    nodeModifications.put(componentPath, updateDate.toInstant());
                    javascriptToFileService.createJsFile(folderName, componentPath, componentProperties, resourceResolver);
                }else{
                    LOG.debug("Configured environment ({}) does not match run mode. File creation was aborted.", environment);
                }
            }
        } catch(RepositoryException e){
            LOG.error("Error while treating events: {}", e.getMessage());
        }
    }
}
