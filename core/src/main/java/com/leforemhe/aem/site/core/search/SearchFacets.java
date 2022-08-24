package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;
import com.leforemhe.aem.site.core.search.predicates.PredicateGroup;

import java.util.List;

@ProviderType
public interface SearchFacets {
    List<PredicateGroup> getPredicateGroups();
}
