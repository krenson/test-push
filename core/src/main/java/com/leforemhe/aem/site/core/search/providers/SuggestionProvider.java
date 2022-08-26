package com.leforemhe.aem.site.core.search.providers;

import org.osgi.annotation.versioning.ProviderType;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import java.util.List;

@ProviderType
public interface SuggestionProvider {
    List<String> suggest(ResourceResolver resourceResolver, String path, String nodeType, String term, int limit) throws RepositoryException;
}
