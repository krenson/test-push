package com.leforemhe.aem.site.core.search.providers;

import org.osgi.annotation.versioning.ProviderType;
import com.leforemhe.aem.site.core.search.SearchResultsPagination;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.search.result.SearchResult;

import java.util.List;
import java.util.Map;

@ProviderType
public interface SearchProvider {
	SearchResult search(ResourceResolver resourceResolver, Map<String, String> predicates);

	List<SearchResultsPagination> buildPagination(SearchResult result, String previousLabel, String nextLabel);

    List<com.leforemhe.aem.site.core.search.SearchResult> buildSearchResults(SearchResult result, List<String> cleMetierList, Boolean nolimit);

}
