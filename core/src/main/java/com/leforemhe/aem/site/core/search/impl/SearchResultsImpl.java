package com.leforemhe.aem.site.core.search.impl;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.leforemhe.aem.site.core.models.ModelUtils;
import com.leforemhe.aem.site.core.models.utils.FilterModel;
import com.leforemhe.aem.site.core.models.utils.TagUtils;
import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SearchResults;
import com.leforemhe.aem.site.core.search.SearchResultsContentFragment;
import com.leforemhe.aem.site.core.search.SearchResultsPagination;
import com.leforemhe.aem.site.core.search.predicates.PredicateResolver;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leforemhe.aem.site.core.services.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchResults.class,
        resourceType = "leforemhe/components/site/search",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = "jackson",
        selector = "results",
        extensions = "json",
        options = {
                @ExporterOption(name = "SerializationFeature.WRITE_NULL_MAP_VALUES", value = "true"),
                @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
        }
)
public class SearchResultsImpl implements SearchResults {
    private static final Logger log = LoggerFactory.getLogger(SearchResultsImpl.class);

    @Inject
    ImageService imageService;

    @Self
    private SlingHttpServletRequest request;
    @OSGiService
    private PredicateResolver predicateResolver;
    @Inject
    private ResourceResolver resourceResolver;
    @Inject
    private SearchProvider searchProvider;
    @Self
    private SearchResultsContentFragment searchResultsContentFragment;
    @ValueMapValue
    private boolean showInputField;
    @ValueMapValue
    private boolean showResults;
    @ValueMapValue
    private String searchResultPage;
    @ValueMapValue
    private String oneResultLabel;
    @ValueMapValue
    private String multipleResultLabel;
    @ValueMapValue
    private String noResultText;
    @ValueMapValue
    private String fileReferenceImage;
    @ChildResource
    private List<TagNamespace> tagNamespaces;

    private List<SearchResult> searchResults = Collections.EMPTY_LIST;
    private List<SearchResultsPagination> pagination = Collections.EMPTY_LIST;
    ArrayList<FilterModel> tagsList = new ArrayList<>();
    private String totalResults;
    private int index = 0;

    @PostConstruct
    private void initModel() {
        log.debug("Inside initModel");
        List<String> cleMetierList = new ArrayList<>();
        String query = request.getParameter("q");
        String orCheckbox = request.getParameter("or");
        final Map<String, String> searchPredicates = new HashMap<>();
        if (query != null) {
            cleMetierList = searchResultsContentFragment.getContentFragmentsCleMetier(query, orCheckbox);
            if (cleMetierList.isEmpty()) {
                searchPredicates.put("group.1_fulltext", query);
            }
        }
        searchPredicates.put("type", com.day.cq.wcm.api.NameConstants.NT_PAGE);
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "limit"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "guessTotal"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "useExcerpt"));
        searchPredicates.putAll(predicateResolver.getRequestPredicateFromGroup(request, "searchPaths"));
        addClemetiers(cleMetierList, searchPredicates);
        if (request.getParameter("tags") != null) {
            addTags(request.getParameter("tags"), searchPredicates, "0", orCheckbox != null && orCheckbox.equals("true"));
        }
        if(request.getParameter("arborescence") != null){
            addTags(request.getParameter("arborescence"), searchPredicates, "1", true);

        }
        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        pagination = searchProvider.buildPagination(result, "Previous", "Next");
        searchResults = searchProvider.buildSearchResults(result, cleMetierList);
        totalResults = computeTotalMatches(result);
    }

    public boolean getShowResults() {
        return this.showResults;
    }

    @Override
    public Boolean getShowInputField() {
        return showInputField;
    }


    public String getAction() {
        return ModelUtils.getVanityOfPageIfExists(this.searchResultPage, resourceResolver);
    }

    private String computeTotalMatches(com.day.cq.search.result.SearchResult result) {
        log.debug("Inside computeTotalMatches");

        String totalResults = String.valueOf(result.getTotalMatches());

        //Returning a String with '+' character in the case guessTotal is being used
        if (result.hasMore()) {
            totalResults += "+";
        }
        return totalResults;
    }

    private void addClemetiers(List<String> cleMetierList, Map<String, String> searchPredicates) {
        if (!cleMetierList.isEmpty()) {
            searchPredicates.put("1_property", "jcr:content/clemetier");
            for (String cleMetier : cleMetierList) {
                searchPredicates.put("1_property." + index++ + "_value", cleMetier);
            }
        }
    }

    private void addTags(String tags, Map<String, String> searchPredicates, String groupIndex, boolean isOr) {
        int index = 1;
        for (String tag : tags.split(",")) {
            index++;
            searchPredicates.put("group." + groupIndex + "_group." + index + "_property", "jcr:content/cq:tags");
            searchPredicates.put("group." + groupIndex + "_group." + index + "_property.value", tag);
        }
        if(isOr) {
            searchPredicates.put("group." + groupIndex + "_group.p.or", "true");
        }
    }

    @Override
    public List<SearchResult> getResults() {
        log.debug("Inside getResults");
        return searchResults;
    }

    @JsonIgnore
    @Override
    public List<SearchResultsPagination> getPagination() {
        log.debug("Inside getPagination");
        return pagination;
    }

    @Override
    public String getResultTotal() {
        log.debug("Inside getResultTotal");
        return totalResults;
    }

    @Override
    public String getFallbackImage() {
        return imageService.getImageRendition(fileReferenceImage, resourceResolver);
    }

    @Override
    public String getOneResultLabel() {
        return oneResultLabel;
    }

    @Override
    public String getMultipleResultLabel() {
        return multipleResultLabel;
    }

    @Override
    public String getNoResultText() {
        return noResultText;
    }

    @Override
    public List<FilterModel> getTagNamespaces() {
        tagsList = new ArrayList<>();
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagNamespaces != null && tagManager != null) {
            for (int i = 0; i < tagNamespaces.size(); i++) {
                Tag parentTag = tagManager.resolve(tagNamespaces.get(i).getTagNamespace());
                tagsList = TagUtils.getParentAndChildTags(parentTag.listChildren(), parentTag, tagsList);
            }
        }
        return tagsList;
    }

    @Override
    public boolean getShowResultsMobileArboresence() {
        // TODO: OPTIMIZE WHOLE SEARCH
        if (showResults && !showInputField) {
            return true;
        } else {
            return false;
        }
    }

}
