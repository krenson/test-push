package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.models.ModelUtils;
import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SearchResults;
import com.leforemhe.aem.site.core.search.SearchResultsContentFragment;
import com.leforemhe.aem.site.core.search.SearchResultsPagination;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchResults.class,
        resourceType = "leforemhe/components/site/search",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
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
    @ValueMapValue
    private boolean showResults;
    @ValueMapValue
    private String searchResultPage;

    private List<SearchResult> searchResults = Collections.EMPTY_LIST;
    private List<SearchResultsPagination> pagination = Collections.EMPTY_LIST;
    private String totalResults;
    private int index = 0;

    @PostConstruct
    private void initModel() {
        log.debug("Inside initModel");
        List<String> cleMetierList = new ArrayList<>();
        String query = request.getParameter("q");
        String orCheckbox = request.getParameter("or");
        String limit = request.getParameter("limit");
        final Map<String, String> searchPredicates = new HashMap<>();
        if (query != null) {
            cleMetierList = searchResultsContentFragment.getContentFragmentsCleMetier(query, orCheckbox);
            if (cleMetierList.isEmpty()) {
                searchPredicates.put("group.1_fulltext", query);
            }
        }
        searchPredicates.put("type", com.day.cq.wcm.api.NameConstants.NT_PAGE);
        if (limit == null || !limit.equals("no-limit")) {
            searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "limit"));
        }
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "guessTotal"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "useExcerpt"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "searchPaths"));
        addClemetiers(cleMetierList, searchPredicates);
        if (request.getParameter("tags") != null) {
            addTags(request.getParameter("tags"), searchPredicates);
        }

        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        pagination = searchProvider.buildPagination(result, "Previous", "Next");
        searchResults = searchProvider.buildSearchResults(result, cleMetierList);
        totalResults = computeTotalMatches(result);
    }

    public boolean getShowResults() {
        return this.showResults;
    }

    public String getAction() {
        if (showResults) {
            return StringUtils.EMPTY;
        } else {
            return ModelUtils.getVanityOfPageIfExists(this.searchResultPage, resourceResolver);
        }
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

    private void addTags(String tags, Map<String, String> searchPredicates) {
        int index = 1;
        for (String tag : tags.split(",")) {
            index++;
            searchPredicates.put(index +"_property", "jcr:content/cq:tags");
            searchPredicates.put(index + "_property.value", tag);
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
