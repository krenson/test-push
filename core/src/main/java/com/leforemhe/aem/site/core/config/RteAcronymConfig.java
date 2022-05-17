package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Rich Text Editor Acronyms Configuration
 * Configuration for RTE Acronym plugin
 */
@ObjectClassDefinition(name="leforemhe - RTE - Acronym",
        description = "Configuration for RTE Acronym plugin")
public @interface RteAcronymConfig {

    /**
     * Acronyms
     * @return Acronyms
     */
    @AttributeDefinition(
            name = "Acronyms",
            type = AttributeType.STRING
    )
    public String[] acronyms() default {};

}
