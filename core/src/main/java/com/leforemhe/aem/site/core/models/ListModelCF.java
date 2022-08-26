package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.List;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ListModelCF extends List {
    String[] getSynonymes();
}
