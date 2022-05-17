package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.GlobalConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;


/**
 * Service to access the configuration. Can be injected via @Reference.
 */
@Component(
        immediate = true,
        service = GlobalConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.GlobalConfig"
)
@Designate(
        ocd = GlobalConfig.class
)
public class GlobalConfigService {
    private GlobalConfig configuration;

    @Activate
    protected final void activate(GlobalConfig config) {
        this.configuration = config;
    }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public GlobalConfig getConfig() {
        return configuration;
    }
}
