package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Dynamic Javascript Configuration
 * Configuration of the generation of dynamic javascript file
 */
@ObjectClassDefinition(name="leforemhe - Open Graph",
        description = "Configuration of the Open Graph")
public @interface OpenGraphConfig {

    /**
     * Path to listen
     * @return Path of the experience fragments on which the file generation is triggered.
     */
    @AttributeDefinition(
            name = "Type",
            type = AttributeType.STRING,
            description = "Open Graph - Type"
    )
    String type() default "government";

    /**
     * Javascript folder path
     * @return Path of the javascript files folder
     */
    @AttributeDefinition(
            name = "Site Name",
            type = AttributeType.STRING,
            description = "Open Graph - Site Name"
    )
    String siteName() default "Le Forem";

    @AttributeDefinition(
            name = "Image",
            type = AttributeType.STRING,
            description = "Open Graph - Image"
    )
    String image();
}
