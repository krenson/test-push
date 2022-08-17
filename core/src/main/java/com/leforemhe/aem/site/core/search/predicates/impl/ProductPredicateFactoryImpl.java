package com.leforemhe.aem.site.core.search.predicates.impl;

import com.leforemhe.aem.site.core.search.predicates.PredicateFactory;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        service = AbstractTagPredicateFactory.class
)
public class ProductPredicateFactoryImpl extends AbstractTagPredicateFactory implements PredicateFactory {

    public static final String TAG_NAMESPACE = "product";
    public static final String REQUEST_PARAM = "product";
    public static final int GROUP_ID = 2000;
    public static final String PROPERTY_PATH = "jcr:content/cq:tags";

    @Override
    public String getTitle() {
        return "Products";
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
