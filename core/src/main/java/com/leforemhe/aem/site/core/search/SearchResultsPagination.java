package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SearchResultsPagination {
    String getLabel();
    long getOffset();
    boolean isActive();
    boolean isDisabled();
}
