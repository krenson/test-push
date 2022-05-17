package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class GlobalConfigServiceTest extends AbstractAEMConfigServiceTest<GlobalConfigService> {
    private static final String APP_NAME = "appName";
    private static final String UNPLACED_PAGES_NODE_NAME = "unplacedPagesNodeName";
    private static final String CONTENT_PATH = "contentPath";
    private static final String ASSETS_PATH = "assetsPath";
    private static final String API_CONTEXT_PATH = "exportContextPath";
    private static final String LINK_SERVER_URI = "publicServerURI";
    private static final String SYSTEM_USER = "systemUser";
    private static final String PATH_SEPARATOR = "pathSeparator";

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(APP_NAME, "leforemhe");
        map.put(UNPLACED_PAGES_NODE_NAME, "pages-non-places");
        map.put(CONTENT_PATH, "contentPath");
        map.put(ASSETS_PATH, "assetsPath");
        map.put(API_CONTEXT_PATH, "exportContextPath");
        map.put(LINK_SERVER_URI, "test.resource.com");
        map.put(SYSTEM_USER, "systemUser");
        map.put(PATH_SEPARATOR, "/");

        return map;
    }
}
