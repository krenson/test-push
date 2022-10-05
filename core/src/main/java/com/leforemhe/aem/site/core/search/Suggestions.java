package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface Suggestions {
    List<String> getSuggestions();

    String getSearchTerm();
}
