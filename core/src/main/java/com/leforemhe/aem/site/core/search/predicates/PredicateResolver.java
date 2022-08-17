package com.leforemhe.aem.site.core.search.predicates;

import org.osgi.annotation.versioning.ProviderType;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

@ProviderType
public interface PredicateResolver {
    Map<String, String> getRequestPredicates(SlingHttpServletRequest request);

    List<PredicateGroup> getPredicateGroups(SlingHttpServletRequest request);
}
