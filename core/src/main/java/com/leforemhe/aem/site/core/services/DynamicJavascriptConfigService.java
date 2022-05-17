package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.JavascriptConfig;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Dynamic Javascript Configuration
 */
@Component(
        immediate = true,
        service = DynamicJavascriptConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.DynamicJavascriptConfig"
)
@Designate(
        ocd = JavascriptConfig.class
)
public class DynamicJavascriptConfigService {

    private JavascriptConfig configuration;

    protected final void activate(JavascriptConfig config){
        this.configuration = config;
    }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public JavascriptConfig getConfig() {
        return configuration;
    }
}
