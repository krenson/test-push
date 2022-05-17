package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Google Services Configuration
 * Configuration of google services and keys
 */
@ObjectClassDefinition(name="leforemhe - Google services config",
        description = "Configuration of google services and keys")
public @interface GoogleServicesConfig {

    /**
     * GCSE key
     * @return Google custom search engine key
     */
    @AttributeDefinition(
            name = "GCSE key",
            type = AttributeType.STRING,
            description = "Google custom search engine key"
    )
    String googleCustomSearchEngineKey() default "009994014376239286379:tjqeu6y7vs8";

    /**
     * GCSE key - parameter name
     * @return Name of the parameter where to put the GCSE key when requesting GCSE
     */
    @AttributeDefinition(
            name = "GCSE key - parameter name",
            type = AttributeType.STRING,
            description = "Google custom search engine key - name of the parameter where to put the GCSE key when requesting GCSE"
    )
    String googleCustomSearchEngineKeyParameterName() default "cx";

    /**
     * GCSE Source
     * @return Google custom search engine source
     */
    @AttributeDefinition(
            name = "GCSE Source",
            type = AttributeType.STRING,
            description = "Google custom search engine source"
    )
    String googleCustomSearchEngineSource() default "https://cse.google.com/cse.js";
}
