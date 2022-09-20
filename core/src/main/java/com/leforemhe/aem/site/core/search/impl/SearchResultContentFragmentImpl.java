package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SearchResultsContentFragment;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchResultsContentFragment.class,
        resourceType = "leforemhe/components/site/search"
)
@Exporter(
        name = "jackson",
        selector = "results",
        extensions = "json",
        options = {
                @ExporterOption(name = "SerializationFeature.WRITE_NULL_MAP_VALUES", value = "true"),
                @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
        }
)
public class SearchResultContentFragmentImpl implements SearchResultsContentFragment {
    private static final Logger log = LoggerFactory.getLogger(SearchResultContentFragmentImpl.class);

    @Self
    private SlingHttpServletRequest request;
    @OSGiService
    private PredicateResolver predicateResolver;
    @Inject
    private ResourceResolver resourceResolver;
    @Inject
    private SearchProvider searchProvider;

    private List<SearchResult> searchResults = Collections.EMPTY_LIST;


    @PostConstruct
    private void initModel() {
        final Map<String, String> searchPredicates = predicateResolver.getRequestPredicates(request);
        log.debug("Search parameter q={}", request.getParameter("q"));
        searchPredicates.put("type", "dam:Asset");
        searchPredicates.put("path", "/content/dam/leforemhe");
        searchPredicates.put("property_1", "jcr:content/data/cq:model");
        searchPredicates.put("property_1.value", "/conf/leforemhe/settings/dam/cfm/models/metier");
        searchPredicates.put("group.1_fulltext", request.getParameter("q"));

        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        searchResults = searchProvider.buildSearchResults(result);
        for (SearchResult searchResult : searchResults) {
            searchResult.getPath();
            }
        }

    public List<SearchResult> getResults() {
        log.debug("Inside getResults");

        return searchResults;
    }
    }
