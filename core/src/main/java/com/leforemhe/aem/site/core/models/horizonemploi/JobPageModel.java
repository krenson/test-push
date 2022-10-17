package com.leforemhe.aem.site.core.models.horizonemploi;


import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface JobPageModel extends Page {
    String getFeaturedImage();
    List<JobTag> getJobTags();
}
