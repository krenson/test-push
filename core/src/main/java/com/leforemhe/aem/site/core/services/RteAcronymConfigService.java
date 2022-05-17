package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.RteAcronymConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Rich Text Editor Configuration Service
 */
@Component(
        immediate = true,
        service = RteAcronymConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.RteAcronymConfig"
)
@Designate(
        ocd= RteAcronymConfig.class
)
public class RteAcronymConfigService {

    private RteAcronymConfig configuration;

    @Activate
    protected void activate(RteAcronymConfig config) { this.configuration = config; }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public RteAcronymConfig getConfig() {
        return this.configuration;
    }
}
