package com.leforemhe.aem.site.core.search.predicates.impl;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.leforemhe.aem.site.core.search.predicates.PredicateFactory;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import com.leforemhe.aem.site.core.services.SearchConfigService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        service = PredicateFactory.class
)
public class MetierPredicateFactoryImpl extends AbstractTagPredicateFactory implements PredicateFactory {

    public static final String TAG_NAMESPACE = "he-metier";
    public static final String REQUEST_PARAM = "tags";
    public static final int GROUP_ID = 3000;
    public static final String PROPERTY_PATH = "jcr:content/cq:tags";

    @Reference
    private SearchConfigService searchConfigService;

    @Override
    public String getTitle(SlingHttpServletRequest request) {
        TagManager tagManager = request.getResourceResolver().adaptTo(TagManager.class);
        Tag tag = Arrays.stream(tagManager.getNamespaces()).filter(namespaces -> namespaces.getTagID().contains(TAG_NAMESPACE)).findAny().get();
        return tag.getTitle();
    }

    @Override
    public String getName() {
        return REQUEST_PARAM;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = super.getRequestPredicate(GROUP_ID, request.getParameterValues(REQUEST_PARAM), PROPERTY_PATH);
        // Overrides params as needed

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        final List<PredicateOption> options = super.getPredicateOptions(request, TAG_NAMESPACE);

        for (final PredicateOption option : options) {
            option.setActive(ArrayUtils.contains(request.getParameterValues(REQUEST_PARAM), option.getValue()));
        }

        return options;
    }
}
