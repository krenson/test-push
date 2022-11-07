package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface SearchResults {
    List<SearchResult> getResults();

    List<SearchResultsPagination> getPagination();

    String getResultTotal();

    String getResultCounterLabel();

    String getResultCounterLabelZeroOrOne();
}
