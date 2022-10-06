package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SearchResults;
import com.leforemhe.aem.site.core.search.SearchResultsContentFragment;
import com.leforemhe.aem.site.core.search.SearchResultsPagination;
import com.leforemhe.aem.site.core.search.predicates.PredicateGroup;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.*;

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
public class SearchResultsImpl implements SearchResults {
    private static final Logger log = LoggerFactory.getLogger(SearchResultsImpl.class);

    @Self
    private SlingHttpServletRequest request;
    @OSGiService
    private PredicateResolver predicateResolver;
    @Inject
    private ResourceResolver resourceResolver;
    @Inject
    private SearchProvider searchProvider;
    @Self
    private SearchResultsContentFragment searchResultsContentFragment;

    private List<SearchResult> searchResults = Collections.EMPTY_LIST;
    private List<SearchResultsPagination> pagination = Collections.EMPTY_LIST;
    private String totalResults;
    private int index = 0;

    @PostConstruct
    private void initModel() {
        log.debug("Inside initModel");
        boolean activeFacets = false;
        final long start = System.currentTimeMillis();
        String query = request.getParameter("q");
        String limit = request.getParameter("limit");
        final Map<String, String> searchPredicates = new HashMap<>();
        List<String> cleMetierList = searchResultsContentFragment.getContentFragmentsCleMetier(query);
        if (cleMetierList.size() == 0 && query != null) {
            searchPredicates.put("group.1_fulltext", query);
        }
        searchPredicates.put("type", com.day.cq.wcm.api.NameConstants.NT_PAGE);
        if (limit == null || !limit.equals("no-limit")) {
            searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "limit"));
        }
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "guessTotal"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "useExcerpt"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "searchPaths"));
        addClemetiers(cleMetierList, searchPredicates);
        addTags(predicateResolver.getPredicateGroup(request, "tags"), searchPredicates);

        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        pagination = searchProvider.buildPagination(result, "Previous", "Next");
        searchResults = searchProvider.buildSearchResults(result);
        totalResults = computeTotalMatches(result);
    }

    private String computeTotalMatches(com.day.cq.search.result.SearchResult result) {
        log.debug("Inside computeTotalMatches");

        String totalResults = String.valueOf(result.getTotalMatches());

        //Returning a String with '+' character in the case guessTotal is being used
        if (result.hasMore()) {
            totalResults += "+";
        }
        return totalResults;
    }

    private void addClemetiers(List<String> cleMetierList, Map<String, String> searchPredicates) {
        if (!cleMetierList.isEmpty()) {
            searchPredicates.put("1_property", "jcr:content/clemetier");
            for (String cleMetier : cleMetierList) {
                searchPredicates.put("1_property." + index++ + "_value", cleMetier);
            }
        }
    }

    private void addTags(PredicateGroup tagPredicates, Map<String, String> searchPredicates) {
        if (tagPredicates != null) {
            for (PredicateOption options : tagPredicates.getOptions()) {
                if (options.isActive()) {
                    searchPredicates.put("2_property", "jcr:content/cq:tags");
                    searchPredicates.put("2_property.value", options.getValue());
                }
            }
        }
    }


    @Override
    public List<SearchResult> getResults() {
        log.debug("Inside getResults");

        return searchResults;
    }

    @JsonIgnore
    @Override
    public List<SearchResultsPagination> getPagination() {
        log.debug("Inside getPagination");

        return pagination;
    }

    @Override
    public String getResultTotal() {
        log.debug("Inside getResultTotal");

        return totalResults;
    }
}
