package com.leforemhe.aem.site.core.models.horizonemploi;

import com.adobe.cq.wcm.core.components.models.Accordion;
import com.leforemhe.aem.site.core.models.cfmodels.Activity;

import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface Activities extends Accordion {
    List<Activity> getActivities();
}
