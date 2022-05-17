package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;

public class RteAcronymConfigServiceTest extends AbstractAEMConfigServiceTest<RteAcronymConfigService> {

    private static final String ACRONYMS = "acronyms";
    private static final String[] ACRONYMS_VAL = {"Formation professionnelle et Emploi=Le FOREM", "Mise en Situation Professionnelle=MISIP"};

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ACRONYMS, ACRONYMS_VAL);
        return attributes;
    }
}
