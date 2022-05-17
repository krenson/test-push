package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Global Configuration
 * leforemhe - Global Configuration
 */
@ObjectClassDefinition(name = "leforemhe - Global Configuration Service",
        description = "Global Configuration Service")
public @interface GlobalConfig {

    String APPNAME = "leforemhe";
    String DEFAULTRESOURCESERVER = "localhost:4502";
    String SEPARATOR = "/";
    String RUNMODE = "author";

    /**
     * Application name
     * @return Application name
     */
    @AttributeDefinition(
            name = "App name",
            type = AttributeType.STRING,
            description = "Application name")
    String appName() default APPNAME;

    /**
     * Runmode
     * @return Runmode
     */
    @AttributeDefinition(
            name = "Runmode",
            type = AttributeType.STRING,
            description = "Runmode")
    String runMode() default RUNMODE;

    /**
     * Unplaced pages node name
     * @return Name of the node (in url) of the unplaced pages
     */
    @AttributeDefinition(
            name = "Unplaced pages node name",
            type = AttributeType.STRING,
            description = "Name of the node (in url) of the unplaced pages"
    )
    String unplacedPagesNodeName() default "pages-non-places";

    /**
     * Highest level path
     * @return Path of the highest node from which an experience fragment can be inherited
     */
    @AttributeDefinition(
            name = "Content path",
            type = AttributeType.STRING,
            description = "Content path for leforemhe site"
    )
    String contentPath() default "/content/leforemhe/fr";

    /**
     * Highest level asset path
     * @return Asset path
     */
    @AttributeDefinition(
            name = "Assets path",
            type = AttributeType.STRING,
            description = "Assets path for leforemhe site"
    )
    String assetsPath() default "/content/dam/leforemhe/fr";

    /**
     * Highest level experience fragments path
     * @return Experience fragments path
     */
    @AttributeDefinition(
            name = "Experience fragments path",
            type = AttributeType.STRING,
            description = "Experience fragments path for leforemhe site"
    )
    String experienceFragmentsPath() default "/content/experience-fragments/leforemhe/fr";

    /**
     * System user
     * @return System user
     */
    @AttributeDefinition(
            name = "System user",
            type = AttributeType.STRING,
            description = "System user to map to a service in OSGI in order to write or update nodes properties within CRX"
    )
    String systemUser() default "datawrite";

    /**
     * Export assets context to expose Experience Fragment generated via ExportEF process
     * @return export assets context
     */
    @AttributeDefinition(
            name = "Export assets context",
            type = AttributeType.STRING,
            description = "Export assets context via assets to expose EF which has been generated via ExportEF process"
    )
    String exportContextPath() default "";

    /**
     * AEM server URL (public server)
     * @return URL of the public server to which the user is redirected by clicking on a link
     */
    @AttributeDefinition(
            name = "AEM server URL (public server)",
            type = AttributeType.STRING,
            description = "URL of the public server to which the user is redirected by clicking on a link"
    )
    String publicServerURI() default DEFAULTRESOURCESERVER;

    /**
     * AEM server URL (public server)
     * @return URL of the public server to which the user is redirected by clicking on a link
     */
    @AttributeDefinition(
            name = "AEM server URL (author server)",
            type = AttributeType.STRING,
            description = "URL of the author server to which the user is redirected by clicking on a link"
    )
    String authorServerURI() default DEFAULTRESOURCESERVER;

    /**
     * AEM CRX separator path
     * @return separator path on AEM CRX
     */
    @AttributeDefinition(
            name = "AEM CRX separator path",
            type = AttributeType.STRING,
            description = "Separator path used via AEM CRX nodes tree"
    )
    String pathSeparator() default SEPARATOR;
}