package com.leforemhe.aem.site.core.models.horizonemploi;

import org.apache.sling.api.resource.Resource;
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
}
