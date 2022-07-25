package com.leforemhe.aem.site.core.services;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.sun.security.auth.module.LdapLoginModule;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Content fragment utility service
 */
@Component(service = ContentFragmentUtilService.class)
public class ContentFragmentUtilService {

    public static final String ELEMENT_TITLE = "titre";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private GlobalConfigService globalConfigService;

    private static final Logger LOG = LoggerFactory.getLogger(ContentFragmentUtilService.class);

    /**
     * Get asked content data from content fragment by ID
     *
     * @param contentFragmentId    String content
     * @param contentFragmentModel type of content fragment to be returned
     * @param contentList          elements that needs to be returned
     * @return contentFragmentData Map
     */
    public Map<String, Object> getContentFromContentFragment(String contentFragmentId, String contentFragmentModel, String[] contentList) {
        ResourceResolver resourceResolver = ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser());
        Map<String, Object> contentFragmentData;
        LOG.debug("Inside get content from content fragment for ID {}.", contentFragmentId);
        LOG.debug("System user: {}", globalConfigService.getConfig().systemUser());
        LOG.debug("Resource resolver factory {}.", resolverFactory);
        LOG.debug("Resource resolver {}.", resourceResolver);
        LOG.debug("Content fragment model {}.", contentFragmentModel);
        Iterator<Resource> damContentFragments = resourceResolver.getResource(contentFragmentModel).listChildren();
        LOG.debug("Parent content fragment folder: {}.", damContentFragments);
        List<ContentFragment> listActivities = getContentFragmentsWithKeyProfession(damContentFragments, contentFragmentId);


        contentFragmentData = getDataFromContentFragment(contentList, listActivities.get(0));

        return contentFragmentData;
    }

    private List<ContentFragment> getContentFragmentsWithKeyProfession(Iterator<Resource> initialList, String keyProfession) {
        List<ContentFragment> contentFragments = new ArrayList<ContentFragment>();
        LOG.debug("Inside get content from content fragment with ID.");


        if (initialList != null) {
            do {

                Resource resource = initialList.next();
                if (resource.getResourceType().equals("dam:Asset")) {
                    LOG.debug("Content fragment found in metier folder");

                    ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);

                    Iterator<ContentElement> elementsIt = contentFragment.getElements();
                    do {
                        ContentElement contentElement = elementsIt.next();
                        if (contentElement.getName().equals("codeMetier") && contentElement.getValue().getValue().equals(keyProfession)) {
                            LOG.debug("Content fragment found in metier folder with same key");
                            contentFragments.add(contentFragment);
                        }
                    } while (elementsIt.hasNext());
                }

            } while (initialList.hasNext());
        }
        return contentFragments;
    }

    private Map<String, Object> getDataFromContentFragment(String[] contentList, ContentFragment contentFragment) {
        Map<String, Object> contentFragmentData = new HashMap<>();

            for (String key : contentList) {
                LOG.debug("Getting {} from contentfragment from element {}", key, contentFragment.getElement(key));
                contentFragmentData.put(key, contentFragment.getElement(key).getValue().getValue());
            }

        return contentFragmentData;
    }
}
