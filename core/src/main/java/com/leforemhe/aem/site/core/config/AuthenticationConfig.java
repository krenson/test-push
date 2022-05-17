package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "leforemhe - Authentication Configuration",
    description = "Configuration for authentication"
)
public @interface AuthenticationConfig {
    
    @AttributeDefinition(name = "Users", description = "Users for basic authentication", type = AttributeType.STRING)
    public String[] users();
}
