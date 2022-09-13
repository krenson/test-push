package com.leforemhe.aem.site.core.models.horizonemploi;

import com.adobe.cq.wcm.core.components.models.Button;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ButtonModel extends Button {
    String getUrlLink();
}
