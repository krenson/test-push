package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * FormaPass Configuration
 * leforemhe - FormaPass Configuration
 */
@ObjectClassDefinition(name = "leforemhe - FormaPass Configuration Service",
        description = "FormaPass Configuration Service")
public @interface FormaPassConfig {

    /**
     * All FormaPass base name
     * @return  FormaPass base name
     */
    @AttributeDefinition(
            name = " FormaPass base name",
            type = AttributeType.STRING
    )
    String formapassBaseName() default "/content/leforemhe/fr/catalogue-des-formations-insertions";

}