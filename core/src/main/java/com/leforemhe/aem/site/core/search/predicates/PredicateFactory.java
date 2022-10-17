package com.leforemhe.aem.site.core.search.predicates;

import org.osgi.annotation.versioning.ConsumerType;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

@ConsumerType
public interface PredicateFactory {
    Map<String, String> getRequestPredicate(SlingHttpServletRequest request);

    List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request);

    String getTitle(SlingHttpServletRequest request);

    String getName();
}
