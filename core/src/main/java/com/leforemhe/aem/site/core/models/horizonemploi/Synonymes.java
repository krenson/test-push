package com.leforemhe.aem.site.core.models.horizonemploi;

import com.adobe.cq.wcm.core.components.models.List;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Synonymes extends List {
    String[] getSynonymes();
}
