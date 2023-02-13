package com.leforemhe.aem.site.core.search.impl;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.leforemhe.aem.site.core.models.utils.ContentFragmentUtils;
import com.leforemhe.aem.site.core.search.SearchResult;
import com.leforemhe.aem.site.core.search.SearchResultsContentFragment;
import com.leforemhe.aem.site.core.search.providers.SearchProvider;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

import static com.leforemhe.aem.site.core.models.cfmodels.Job.CODE_METIER_KEY;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchResultsContentFragment.class
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
public class SearchResultContentFragmentImpl implements SearchResultsContentFragment {
    private static final Logger log = LoggerFactory.getLogger(SearchResultContentFragmentImpl.class);
    private static List<String> propertiesMetier = List.of("codeMetier", "titre", "synonymes", "descriptionMetier");
    private static final String METIER_CF_TYPE = "/conf/leforemhe/settings/dam/cfm/models/metier";
    @Self
    private SlingHttpServletRequest request;
    @Inject
    private ResourceResolver resourceResolver;
    @Inject
    private SearchProvider searchProvider;

    private List<String> cleMetierList = new ArrayList<>();
    private List<SearchResult> searchResults = Collections.EMPTY_LIST;


    public List<String> getContentFragmentsCleMetier(String queryParameter, String orCheckbox) {
        final Map<String, String> searchPredicates = new HashMap<>();
        log.debug("Search parameter q={}", queryParameter);
        searchPredicates.put("type", "dam:Asset");
        searchPredicates.put("p.limit", "-1");
        searchPredicates.put("path", "/content/dam/leforemhe");
        List<String> params = Arrays.asList(queryParameter.split(","));
        orCheckbox = orCheckbox != null && orCheckbox.equals("true") ? "true": "false";
        createPropertiesQueryMetier(params, searchPredicates, orCheckbox);
        createPropertiesQueryActivities(params, searchPredicates, orCheckbox);

        com.day.cq.search.result.SearchResult result = searchProvider.search(resourceResolver, searchPredicates);
        searchResults = searchProvider.buildSearchResults(result, null);
        for (SearchResult searchResult : searchResults) {
            ContentFragment contentFragment = request.getResourceResolver().resolve(searchResult.getPath()).adaptTo(ContentFragment.class);
            String cfType = resourceResolver.getResource(searchResult.getPath() + "/jcr:content/data").getValueMap().get("cq:model", String.class);
            if (cfType.equals(METIER_CF_TYPE)) {
                String codeMetier = ContentFragmentUtils.getSingleValue(contentFragment, CODE_METIER_KEY, String.class);
                if (codeMetier != null) {
                    cleMetierList.add(ContentFragmentUtils.getSingleValue(contentFragment, CODE_METIER_KEY, String.class));
                }
            } else {
                List<String> listCodeMetier = Arrays.asList(ContentFragmentUtils.getMultifieldValue(contentFragment, CODE_METIER_KEY, String.class));
                if (listCodeMetier != null) {
                    listCodeMetier.forEach(codeMetier -> {
                        cleMetierList.add(codeMetier);
                    });
                }
            }

        }
        return cleMetierList;
    }

    public List<SearchResult> getResults() {
        return searchResults;
    }

    private void createPropertiesQueryMetier(List<String> params, Map<String, String> searchPredicates, String orCheckbox) {
        int index = 0;
        if (params != null) {
            for (String param : params) {
                for (int i = 0; i < propertiesMetier.size(); i++) {
                    index++;
                    searchPredicates.put("group.1_group." + index + "_fulltext", param);
                    searchPredicates.put("group.1_group." + index + "_fulltext.relPath", "jcr:content/data/master/@" + propertiesMetier.get(i));
                }
            }
            searchPredicates.put("group.1_group.p.or", orCheckbox);
        }
    }

    private void createPropertiesQueryActivities(List<String> params, Map<String, String> searchPredicates, String orCheckbox) {
        int index = 0;
        for (String param : params) {
            index++;
            searchPredicates.put("group.2_group." + index + "_fulltext", '"' + param + '"');
            searchPredicates.put("group.2_group." + index + "_fulltext.relPath", "jcr:content/data/master/@description");
        }
        searchPredicates.put("2_group.p.or", orCheckbox);
    }
}
