package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("WeakerAccess")
public class FormaPassConfigServiceTest extends AbstractAEMConfigServiceTest<FormaPassConfigService> {

    private static final String FORMAPASSBASENAME = "formapassBaseName";

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(FORMAPASSBASENAME, "/content/leforemhe/fr/catalogue-des-formations-insertions");
        return map;
    }
}
