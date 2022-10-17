package com.leforemhe.aem.site.core.search.predicates.impl;

import com.leforemhe.aem.site.core.search.predicates.PredicateFactory;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        service = PredicateFactory.class
)
public class LimitPredicateFactoryImpl implements PredicateFactory {
    private static final Logger log = LoggerFactory.getLogger(LimitPredicateFactoryImpl.class);

    public static final String PN_LIMIT = "limit";
    public static final int DEFAULT_LIMIT = 10;

    @Override
    public String getTitle(SlingHttpServletRequest request) {
        return null;
    }

    @Override
    public String getName() {
        return PN_LIMIT;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final int limit = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_LIMIT, DEFAULT_LIMIT);

        params.put("p.limit", String.valueOf(limit));

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
