package com.leforemhe.aem.site.core.search;

import org.osgi.annotation.versioning.ProviderType;

import javax.jcr.RepositoryException;
import java.util.List;

@ProviderType
public interface SimilarityResults {
    List<SearchResult> getResults() throws RepositoryException;
}
