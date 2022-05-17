package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;

public class RteAbbreviationConfigServiceTest extends AbstractAEMConfigServiceTest<RteAbbreviationConfigService> {

    private static final String ABBREVIATIONS = "abbreviations";
    private static final String[] ABBREVIATIONS_VAL = {"et cetera=etc", "Monsieur=Mr."};

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ABBREVIATIONS, ABBREVIATIONS_VAL);
        return attributes;
    }
}
