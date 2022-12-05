package com.leforemhe.aem.site.core.models.horizonemploi;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface LinkModel {
    String getTitle();
    String getDescription();
    String getIcon();
    String popupReference();
    String getJobTitle();
    int getGeneratedId();
    boolean isConfigured();
    Boolean isInternalLink();
    String getLinkReference();
}
