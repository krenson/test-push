package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.models.Teaser;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.leforemhe.aem.site.core.models.horizonemploi.JobTeaserList;
import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class, adapters = JobTeaserList.class, resourceType = JobTeaserListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JobTeaserListImpl implements JobTeaserList {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/jobteaserlist";

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private SearchProvider searchProvider;

    @Inject
    private ResourceResolver resourceResolver;

    private List<SearchResult> searchResults = Collections.EMPTY_LIST;
    private List<Teaser> teasers = new ArrayList<Teaser>();

    @Override
    public Collection<Teaser> getPages() {
        String tags = request.getParameter("tags");
        final Map<String, String> searchPredicates = new HashMap<>();
        searchPredicates.put("type", "cq:Page");
        searchPredicates.put("path", "/content/leforemhe/fr/infos-metiers/metiers");
        if(tags != null) {
            addTags(tags,searchPredicates);
            searchPredicates.put("p.limit", "-1");
        } else {
            searchPredicates.put("p.limit", "10");
        }
        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        searchResults = searchProvider.buildSearchResults(result, null);
        Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
        if (externalizer != null) {
            searchResults.forEach(searchResult -> {
                Page page = resourceResolver.resolve(searchResult.getPath()).adaptTo(Page.class);
                String externalizedUrl = externalizer.publishLink(resourceResolver, page.getPath()) + ".html";
                Resource imageResource = resourceResolver.getResource(searchResult.getPath() + "/jcr:content/cq:featuredimage");
                teasers.add(new TeaserListItemImpl(page, imageResource, externalizedUrl));
            });
        }
        return teasers;
    }

    private void addTags(String tags, Map<String, String> searchPredicates) {
        int index = 1;
        for (String tag : tags.split(",")) {
            index++;
            searchPredicates.put("group." + index + "_property", "jcr:content/cq:tags");
            searchPredicates.put("group." + index + "_property.value", tag);
        }
    }
}