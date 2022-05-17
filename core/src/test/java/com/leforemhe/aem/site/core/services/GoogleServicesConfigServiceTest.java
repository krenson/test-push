package com.leforemhe.aem.site.core.services;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
@SuppressWarnings("WeakerAccess")
public class GoogleServicesConfigServiceTest extends AbstractAEMConfigServiceTest<GoogleServicesConfigService>{
    private static final String GCSE_KEY = "googleCustomSearchEngineKey";
    private static final String GCSE_KEY_VAL = "gcse-key";

    private static final String GCSE_KEY_PARAMETER_NAME = "googleCustomSearchEngineKeyParameterName";
    private static final String GCSE_KEY_PARAMETER_NAME_VALUE = "cx";

    private static final String GCSE_SOURCE = "googleCustomSearchEngineSource";
    private static final String GCSE_SOURCE_VALUE = "https://cse.google.com/cse.js";

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(GCSE_KEY, GCSE_KEY_VAL);
        map.put(GCSE_KEY_PARAMETER_NAME, GCSE_KEY_PARAMETER_NAME_VALUE);
        map.put(GCSE_SOURCE, GCSE_SOURCE_VALUE);
        return map;
    }
}
