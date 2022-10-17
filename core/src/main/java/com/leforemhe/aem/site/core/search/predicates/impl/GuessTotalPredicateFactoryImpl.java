package com.leforemhe.aem.site.core.search.predicates.impl;

import com.leforemhe.aem.site.core.search.predicates.PredicateFactory;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
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
public class GuessTotalPredicateFactoryImpl implements PredicateFactory {
    private static final Logger log = LoggerFactory.getLogger(GuessTotalPredicateFactoryImpl.class);

    public static final String PN_GUESS_TOTAL = "guessTotal";

    @Override
    public String getTitle(SlingHttpServletRequest request) {
        return null;
    }

    @Override
    public String getName() {
        return PN_GUESS_TOTAL;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final String guessTotalStr = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_GUESS_TOTAL, String.class);

        if (StringUtils.isNotBlank(guessTotalStr)) {
            if (guessTotalStr.equals("true")) {
                params.put("p.guessTotal", guessTotalStr);
            } else {
                try {
                    int guessTotalNumber = Integer.parseInt(guessTotalStr);

                    if (guessTotalNumber > 0) {
                        params.put("p.guessTotal", String.valueOf(guessTotalStr));
                    }
                } catch (Exception e) {
                    log.warn("Unable to parse valid integer from Request Param value [ {} ]", guessTotalStr);
                }
            }
        }

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
