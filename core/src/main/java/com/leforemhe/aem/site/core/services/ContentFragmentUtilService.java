package com.leforemhe.aem.site.core.services;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.leforemhe.aem.site.core.models.pojo.ParseHTML;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.htmlparser.util.ParserException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Parser service to Parse HTML
 */
@Component(service = ContentFragmentUtilService.class)
public class ContentFragmentUtilService {

    public static final String ELEMENT_TITLE = "titre";
    public static final String METIER_MODEL_PATH = "/content/dam/leforemhe/fr/metiers";


    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private GlobalConfigService globalConfigService;

    private static final Logger LOG = LoggerFactory.getLogger(ContentFragmentUtilService.class);

    /**
     * Parse relative path of CSS on absolute path
     *
     * @param contentFragmentId    String content
     * @param contentFragmentModel type of content fragment to be returned
     * @param contentList          elements that needs to be returned
     * @return contentFragmentData Map
     */
    public Map<String, Object> getContentFromContentFragment(String contentFragmentId, String contentFragmentModel, String[] contentList) {
        ResourceResolver resourceResolver = ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser());

        Map<String, Object> contentFragmentData;

        Iterator<Resource> damContentFragments = resourceResolver.getResource(contentFragmentModel).listChildren();
        List<ContentFragment> listActivities = getContentFragmentsWithKeyProfession(damContentFragments, contentFragmentId);

        contentFragmentData = getDataFromListContentFragments(contentList, listActivities);

        return contentFragmentData;
    }

    private List<ContentFragment> getContentFragmentsWithKeyProfession(Iterator<Resource> initialList, String keyProfession) {
        List<ContentFragment> contentFragments = new ArrayList<ContentFragment>();

        if (initialList != null) {
            do {

                Resource resource = initialList.next();
                if (resource.getResourceType().equals("dam:Asset")) {
                    ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);

                    Iterator<ContentElement> elementsIt = contentFragment.getElements();
                    do {
                        ContentElement contentElement = elementsIt.next();
                        if (contentElement.getName().equals("codeMetier") && contentElement.getValue().getValue().equals(keyProfession)) {
                            contentFragments.add(contentFragment);
                        }
                    } while (elementsIt.hasNext());
                }

            } while (initialList.hasNext());
        }
        return contentFragments;
    }

    private Map<String, Object> getDataFromListContentFragments(String[] contentList, List<ContentFragment> listCF) {
        Map<String, Object> contentFragmentData = new HashMap<>();

        for (ContentFragment contentFragmentActivity : listCF) {
            for (String key : contentList) {
                contentFragmentData.put(key,contentFragmentActivity.getElement(key).getValue().getValue());
            }
        }

        return contentFragmentData;
    }

}
