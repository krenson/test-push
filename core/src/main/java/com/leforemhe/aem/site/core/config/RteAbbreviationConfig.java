package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Rich Text Editor Abbreviations Configuration
 * Configuration for RTE Abbreviation plugin
 */
@ObjectClassDefinition(name="leforemhe - RTE - Abbreviation",
        description = "Configuration for RTE Abbreviation plugin")
public @interface RteAbbreviationConfig {

    /**
     * Abbreviations
     * @return Abbreviations
     */
    @AttributeDefinition(
            name = "Abbreviations",
            type = AttributeType.STRING
    )
    String[] abbreviations() default {};
}
