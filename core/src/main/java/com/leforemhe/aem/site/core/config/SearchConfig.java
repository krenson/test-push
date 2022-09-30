package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Search configuration
 */
@ObjectClassDefinition(name = "leforemhe - Search config", description = "Configuration for the Search configuration")
public @interface SearchConfig {
    /**
     * Return the path where it should search for suggestions
     *
     * @return Search for suggestions path
     */
    @AttributeDefinition(name = "Path suggestions from CF", type = AttributeType.STRING)
    String suggestionsPath() default "/content/dam/leforemhe/fr/metiers";

    /**
     * Return the amount of suggestions
     *
     * @return Model Activites path
     */
    @AttributeDefinition(name = "Path CF model Activites in DAM", type = AttributeType.INTEGER)
    int amountOfSuggestions() default 5;
}
