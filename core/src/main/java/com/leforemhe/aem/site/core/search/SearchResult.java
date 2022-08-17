package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;

import java.util.Collection;
import java.util.List;

@ProviderType
public interface SearchResult {
    int DESCRIPTION_MAX_LENGTH = 320;

    String DESCRIPTION_ELLIPSIS = " ... ";

    enum ContentType {
        PAGE,
        ASSET
    }

    ContentType getContentType();

    List<String> getTagIds();

    String getURL();

    String getPath();

    String getTitle();

    String getDescription();

    void setExcerpts(Collection<String> excerpt);
}
