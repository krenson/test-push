package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SearchResults;
import com.leforemhe.aem.site.core.search.SearchResultsContentFragment;
import com.leforemhe.aem.site.core.search.SearchResultsPagination;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import com.leforemhe.aem.site.core.search.predicates.impl.FullltextPredicateFactoryImpl;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Collections;
import java.util.List;
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
    private long timeTaken = -1;
    private String totalResults;
    private int index = 0;

    @PostConstruct
    private void initModel() {
        log.debug("Inside initModel");
        final long start = System.currentTimeMillis();
        String query = request.getParameter("q");
        final Map<String, String> searchPredicates = predicateResolver.getRequestPredicates(request);
        List<String> cleMetierList = searchResultsContentFragment.getContentFragmentsCleMetier(query);
        log.debug("Search parameter q={}", query);
        searchPredicates.put("type", "cq:Page");
        searchPredicates.put("path", "/content/leforemhe");
        if(!cleMetierList.isEmpty()) {
            searchPredicates.put("property", "jcr:content/clemetier");
            for (String cleMetier : cleMetierList) {
                searchPredicates.put("property." + index++ + "_value", cleMetier);
            }
        }
        else {
            searchPredicates.put("group.p.or", "true");
            searchPredicates.put("group.1_fulltext", query);
            searchPredicates.put("group.1_fulltext", "jcr:content");
        }

        if (isSearchable()) {
            com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
            pagination = searchProvider.buildPagination(result, "Previous", "Next");
            searchResults = searchProvider.buildSearchResults(result);
            totalResults = computeTotalMatches(result);
            timeTaken = result.getExecutionTimeMillis();
        }
    }

    private boolean isSearchable() {
        log.debug("Inside isSearchable");
        return StringUtils.isNotBlank(getSearchTerm())
            || new ComponentInheritanceValueMap(request.getResource()).getInherited("allowBlankFulltext", false);
    }

    private String computeTotalMatches(com.day.cq.search.result.SearchResult result) {
        log.debug("Inside computeTotalMatches");

    	String totalResults = String.valueOf(result.getTotalMatches());

    	//Returning a String with '+' character in the case guessTotal is being used
    	if(result.hasMore()) {
    		totalResults += "+";
    	}
    	return totalResults;
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
    public String getSearchTerm() {
        log.debug("Inside getSearchTerm");

        return request.getParameter(FullltextPredicateFactoryImpl.REQUEST_PARAM);
    }

    @Override
    public long getTimeTaken() {
        log.debug("Inside getTimeTaken");

        return timeTaken;
    }

	@Override
	public String getResultTotal() {
        log.debug("Inside getResultTotal");

        return totalResults;
	}
}
