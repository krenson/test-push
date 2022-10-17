package com.leforemhe.aem.site.core.search.predicates.impl;

import com.leforemhe.aem.site.core.search.predicates.PredicateFactory;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.leforemhe.aem.site.core.services.SearchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
        immediate = true,
        service = PredicateFactory.class
)
public class PathsPredicateFactoryImpl implements PredicateFactory {
    public static final String PN_SEARCH_PATHS = "searchPaths";
    public static final String DEFAULT_SEARCH_PATH = "/content/leforemhe";
    public static final String SAFEGUARD_SEARCH_PATH = "/content/leforemhe";
    private static final Logger log = LoggerFactory.getLogger(PathsPredicateFactoryImpl.class);

    @Reference
    SearchConfigService searchConfigService;

    @Override
    public String getTitle(SlingHttpServletRequest request) {
        return null;
    }

    @Override
    public String getName() {
        return PN_SEARCH_PATHS;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final String[] paths = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_SEARCH_PATHS, new String[]{searchConfigService.getConfig().suggestionsPath()});

        for (String path : paths) {
            if (StringUtils.startsWith(path, SAFEGUARD_SEARCH_PATH)) {
                params.put("path", path);
            }
        }

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
