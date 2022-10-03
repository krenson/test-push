package com.leforemhe.aem.site.core.search.impl;

import com.leforemhe.aem.site.core.search.Suggestions;
import com.leforemhe.aem.site.core.search.predicates.impl.PathsPredicateFactoryImpl;
import com.leforemhe.aem.site.core.search.providers.SuggestionProvider;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.day.cq.wcm.api.NameConstants;
import com.leforemhe.aem.site.core.services.SearchConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = Suggestions.class,
        resourceType = "leforemhe/components/site/search/suggestions"
)
@Exporter(
        name = "jackson",
        selector = "suggestions",
        extensions = "json"
)
public class SuggestionsImpl implements Suggestions {
    private static final Logger log = LoggerFactory.getLogger(SuggestionsImpl.class);

    private static final String PN_SUGGESTIONS_LIMIT = "suggestionsLimit";

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private SearchConfigService searchConfigService;

    @Inject
    private ResourceResolver resourceResolver;

    @Inject
    private SuggestionProvider suggestionProvider;

    @Inject
    private Resource resource;

    List<String> suggestions = Collections.emptyList();

    private long timeTaken = -1;

    @PostConstruct
    private void initModel() {
        final long start = System.currentTimeMillis();

        try {
            // This is required due to a bug in Sling Model Exporter; where 2 Sing Model Exporters cannot bind to the same resourceType
            final String searchPath = new ComponentInheritanceValueMap(resource).getInherited(PathsPredicateFactoryImpl.PN_SEARCH_PATHS, searchConfigService.getConfig().suggestionsPath());
            suggestions = suggestionProvider.suggest(resourceResolver, searchPath,
                                                NameConstants.NT_PAGE, getSearchTerm(),
                                                resource.getValueMap().get(PN_SUGGESTIONS_LIMIT, searchConfigService.getConfig().amountOfSuggestions()), searchConfigService.getConfig().suggestionsPath());
        } catch (RepositoryException e) {
            log.error("Could not collect suggestions for search term [ {} ]", getSearchTerm());
        }

        timeTaken = System.currentTimeMillis() - start;
    }

    public List<String> getSuggestions() {
       return suggestions;
    }

    @Override
    public String getSearchTerm() {
        return request.getParameter("q");
    }

    @Override
    public long getTimeTaken() {
        return timeTaken;
    }
}
