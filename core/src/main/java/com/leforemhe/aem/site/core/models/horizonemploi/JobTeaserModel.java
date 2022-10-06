package com.leforemhe.aem.site.core.models.horizonemploi;

import com.adobe.cq.wcm.core.components.models.Teaser;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface JobTeaserModel extends Teaser {
    List<JobTag> getTags();
}
