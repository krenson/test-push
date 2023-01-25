package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.models.Teaser;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.ModelUtils;
import com.leforemhe.aem.site.core.models.horizonemploi.JobTeaserList;
import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import com.leforemhe.aem.site.core.services.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    @Inject
    private ImageService imageService;

    @ValueMapValue
    private String fallbackImage;

    @ValueMapValue
    private String searchPath;

    private List<SearchResult> searchResults = Collections.EMPTY_LIST;
    private List<Teaser> teasers = new ArrayList<Teaser>();
    private Resource imageResource;

    @Override
    public Collection<Teaser> getPages() {
        String tags = request.getParameter("tags");
        final Map<String, String> searchPredicates = new HashMap<>();
        searchPredicates.put("type", "cq:Page");
        searchPredicates.put("path", searchPath);
        searchPredicates.put("group.p.or", "true");
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
                String url = ModelUtils.getVanityOfPageIfExists(page.getPath(), resourceResolver);
                String featuredImageFileReference = ModelUtils.getFeaturedImageOfPage(page.getPath(), resourceResolver);
                if(!StringUtils.isEmpty(featuredImageFileReference)) {
                    imageResource = resourceResolver.getResource(imageService.getImageRendition(featuredImageFileReference, resourceResolver));
                } else {
                    imageResource = resourceResolver.getResource(fallbackImage);
                }
                teasers.add(new TeaserListItemImpl(page, imageResource, url, imageService, fallbackImage));
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
