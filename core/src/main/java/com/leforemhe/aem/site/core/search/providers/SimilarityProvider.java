package com.leforemhe.aem.site.core.search.providers;

import org.osgi.annotation.versioning.ProviderType;
import com.leforemhe.aem.site.core.search.SearchResult;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import java.util.List;

@ProviderType
public interface SimilarityProvider {
    List<SearchResult> findSimilar(ResourceResolver resourceResolver, String similarToPath) throws RepositoryException;
}
