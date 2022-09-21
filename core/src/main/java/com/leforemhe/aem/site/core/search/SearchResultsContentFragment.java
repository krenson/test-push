package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface SearchResultsContentFragment {
    List<String> getContentFragmentsCleMetier(String queryParameter);
}
