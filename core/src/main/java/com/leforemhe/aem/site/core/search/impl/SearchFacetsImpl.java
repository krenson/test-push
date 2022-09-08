package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.search.SearchFacets;
import com.leforemhe.aem.site.core.search.predicates.PredicateGroup;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchFacets.class
)
public class SearchFacetsImpl implements SearchFacets {
    private static final Logger log = LoggerFactory.getLogger(SearchFacetsImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private PredicateResolver predicateResolver;

    public List<PredicateGroup> getPredicateGroups() {
        return predicateResolver.getPredicateGroups(request);
    }
}