package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class JavascriptConfigServiceTest extends AbstractAEMConfigServiceTest<DynamicJavascriptConfigService> {
    private static final String PATH_TO_LISTEN = "pathToListen";
    private static final String PATH_TO_LISTEN_VAL = "/path/to/listen";
    private static final String JAVASCRIPT_FOLDER_PATH = "dynamicJavascriptFolderPath";
    private static final String JAVASCRIPT_FOLDER_PATH_VAL="/path/to/js/folder";

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(PATH_TO_LISTEN, PATH_TO_LISTEN_VAL);
        map.put(JAVASCRIPT_FOLDER_PATH, JAVASCRIPT_FOLDER_PATH_VAL);
        return map;
    }
}
