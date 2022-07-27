package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Accordion;
import com.leforemhe.aem.site.core.models.pojo.ContentFragmentActivites;
import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface AccordionModelCF extends Accordion {
    List<ContentFragmentActivites> getActivities();
}
