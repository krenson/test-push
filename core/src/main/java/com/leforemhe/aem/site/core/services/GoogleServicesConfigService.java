package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.GoogleServicesConfig;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Google Services Configuration Service
 */
@Component(
        immediate = true,
        service = GoogleServicesConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.GoogleServicesConfig"
)
@Designate(ocd = GoogleServicesConfig.class)
public class GoogleServicesConfigService {
    private GoogleServicesConfig configuration;

    protected final void activate(GoogleServicesConfig config){
        this.configuration = config;
    }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public GoogleServicesConfig getConfig() {
        return configuration;
    }
}
