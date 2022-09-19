package com.leforemhe.aem.site.core.search.impl;

import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.leforemhe.aem.site.core.search.SearchResults;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchResults.class,
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
public class SearchResultContentFragment {
    private static final Logger log = LoggerFactory.getLogger(SearchResultsImpl.class);

    @Self
    private SlingHttpServletRequest request;
    @OSGiService
    private PredicateResolver predicateResolver;
    @Inject
    private ResourceResolver resourceResolver;
    @Inject
    private SearchProvider searchProvider;

    @PostConstruct
    private void initModel() {
        final Map<String, String> searchPredicates = predicateResolver.getRequestPredicates(request);

        log.debug("Search parameter q={}", request.getParameter("q"));
        searchPredicates.put("path", "/content/dam/leforemhe");
        searchPredicates.put("type", "dam:Asset");
        searchPredicates.put("property", "jcr:content/contentFragment");
        searchPredicates.put("property:value", "jcr:content/contentFragment");

        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        //pagination = searchProvider.buildPagination(result, "Previous", "Next");
        //searchResults = searchProvider.buildSearchResults(result);
        //totalResults = computeTotalMatches(result);
        //timeTaken = result.getExecutionTimeMillis();
        }
    }
