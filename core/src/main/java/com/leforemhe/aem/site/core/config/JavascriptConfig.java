package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Dynamic Javascript Configuration
 * Configuration of the generation of dynamic javascript file
 */
@ObjectClassDefinition(name="leforemhe - Dynamic javascript",
        description = "Configuration of the generation of dynamic javascript files")
public @interface JavascriptConfig {

    /**
     * Path to listen
     * @return Path of the experience fragments on which the file generation is triggered.
     */
    @AttributeDefinition(
            name = "Path to listen",
            type = AttributeType.STRING,
            description = "Path of the experience fragments on which the file generation is triggered"
    )
    String pathToListen() default "/content/experience-fragments/leforemhe/fr/site/javascript";

    /**
     * Javascript folder path
     * @return Path of the javascript files folder
     */
    @AttributeDefinition(
            name = "Javascript folder path",
            type = AttributeType.STRING,
            description = "Path of the javascript files folder"
    )
    String dynamicJavascriptFolderPath() default "/content/dam/leforemhe/fr/javascript/dynamic";

    @AttributeDefinition(
            name = "Javascript folder path",
            type = AttributeType.STRING,
            description = "Path of the javascript files folder"
    )
    String staticJavascriptFolderPath() default "/content/dam/leforemhe/fr/javascript/static";
}
