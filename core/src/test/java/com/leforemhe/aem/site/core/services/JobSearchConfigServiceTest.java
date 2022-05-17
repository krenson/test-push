package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class JobSearchConfigServiceTest extends AbstractAEMConfigServiceTest<JobSearchConfigService> {

    private static final String JOB_SEARCH_COUNT_LINK = "jobSearchCountLink";
    private static final String JOB_SEARCH_COUNT_LINK_VAL = "https://leforemhe-dev.forem.be/HotJob/servlet/Admin.JobOffCount?_json_=true";

    private static final String JOB_SEARCH_REQUEST_PATH = "jobSearchRequestPath";
    private static final String JOB_SEARCH_REQUEST_PATH_VAL = "https://leforemhe.be/recherche-offres-emploi/jsp/index.jsp";

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(JOB_SEARCH_COUNT_LINK, JOB_SEARCH_COUNT_LINK_VAL);
        map.put(JOB_SEARCH_REQUEST_PATH, JOB_SEARCH_REQUEST_PATH_VAL);
        return map;
    }
}
