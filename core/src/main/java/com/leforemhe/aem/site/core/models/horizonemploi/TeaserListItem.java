package com.leforemhe.aem.site.core.models.horizonemploi;

import com.adobe.cq.wcm.core.components.models.Teaser;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import org.osgi.annotation.versioning.ConsumerType;

import java.util.List;

@ConsumerType
public interface TeaserListItem extends Teaser {
    List<JobTag> getTags();
}
