package com.leforemhe.aem.site.core.search.predicates;

import org.osgi.annotation.versioning.ProviderType;

import java.util.List;

@ProviderType
public interface PredicateGroup {
    String getName();

    String getTitle();

    List<PredicateOption> getOptions();
}
