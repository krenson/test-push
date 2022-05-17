package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface TextModel extends Text {
    public String getBackgroundCssClass();
}
