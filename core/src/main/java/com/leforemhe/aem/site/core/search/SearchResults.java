package com.leforemhe.aem.site.core.search;

import com.leforemhe.aem.site.core.models.utils.FilterModel;
import org.osgi.annotation.versioning.ProviderType;
import java.util.List;

@ProviderType
public interface SearchResults {
    List<SearchResult> getResults();
    List<SearchResultsPagination> getPagination();
    String getResultTotal();
    String getFallbackImage();
    String getOneResultLabel();
    String getMultipleResultLabel();
    String getNoResultText();
    List<FilterModel> getTagNamespaces();
}
