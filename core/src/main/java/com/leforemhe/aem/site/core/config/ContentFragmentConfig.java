package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Content Fragment configuration
 */
@ObjectClassDefinition(name="leforemhe - Content Fragment config",
        description = "Configuration for the content fragments and models")
public @interface ContentFragmentConfig {


    /**
     * Return the path were metier CF's are stored
     * @return Model Metier path
     */
    @AttributeDefinition(
            name = "Path CF model Metier in DAM",
            type = AttributeType.STRING
    )
    String modelMetierPath() default "/content/dam/leforemhe/fr/metiers";

    /**
     * Return the path were activites CF's are stored
     * @return Model Activites path
     */
    @AttributeDefinition(
            name = "Path CF model Activites in DAM",
            type = AttributeType.STRING
    )
    String modelActivitesPath() default "/content/dam/leforemhe/fr/activites";

}
