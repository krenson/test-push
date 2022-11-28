package com.leforemhe.aem.site.core.models.horizonemploi;

import com.adobe.cq.wcm.core.components.models.Teaser;
import org.osgi.annotation.versioning.ProviderType;

import java.util.Collection;

@ProviderType
public interface JobTeaserList {
    Collection<Teaser> getPages();
}
