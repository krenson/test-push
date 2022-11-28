package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.horizonemploi.TeaserListItemLink;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TeaserListItemLinkImpl implements TeaserListItemLink {
    public static final String HTML_EXTENSION = ".html";
    private Map<String, String> htmlAttributes =  new HashMap<String, String>();
    private boolean isValid = false;
    private final String url;
    private final String mappedUrl;
    private final String externalizedUrl;
    private final Object reference;

    public TeaserListItemLinkImpl(Page page, String externalizedUrl) {
        htmlAttributes.put("href", page.getPath() + HTML_EXTENSION);
        if(page != null && StringUtils.isNotBlank(page.getPath())) {
            this.isValid = true;
        }
        this.url = page.getPath() + HTML_EXTENSION;
        this.mappedUrl = page.getPath() + HTML_EXTENSION;
        this.reference = page;
        this.externalizedUrl = externalizedUrl + HTML_EXTENSION;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public @Nullable String getURL() {
        return url;
    }

    @Override
    public @Nullable String getMappedURL() {
        return mappedUrl;
    }

    @Override
    public @Nullable String getExternalizedURL() {
        return externalizedUrl;
    }

    @Nullable
    @Override
    public Object getReference() {
        return reference;
    }

    @Override
    public @NotNull Map<String, String> getHtmlAttributes() {
        return htmlAttributes;
    }
}
