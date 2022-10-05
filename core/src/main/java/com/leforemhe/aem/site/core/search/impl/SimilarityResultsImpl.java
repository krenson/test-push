package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SimilarityResults;
import com.leforemhe.aem.site.core.search.providers.SimilarityProvider;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SimilarityResults.class
)
public class SimilarityResultsImpl implements SimilarityResults {
    private static final Logger log = LoggerFactory.getLogger(SimilarityResultsImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private ModelFactory modelFactory;

    @Inject
    private ResourceResolver resourceResolver;

    @Inject
    private SimilarityProvider similarityProvider;

    private long start;

    @PostConstruct
    private void initModel() {
        this.start = System.currentTimeMillis();
    }


    public List<SearchResult> getResults() {
    	PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = pageManager.getContainingPage(request.getResource());
        if (page != null) {
            try {
                return similarityProvider.findSimilar(request.getResourceResolver(), page.getPath());
            } catch (RepositoryException e) {
                log.error("Could not find similar pages to [ {} ] due to Repository Exception", page.getPath(), e);
            }
        }

        return Collections.emptyList();
    }

}
