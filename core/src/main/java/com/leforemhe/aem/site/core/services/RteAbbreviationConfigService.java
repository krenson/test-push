package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.RteAbbreviationConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Rich Text Editor Abbreviation Configuration Service
 */
@Component(
        immediate = true,
        service = RteAbbreviationConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.RteAbbreviationConfig"
)
@Designate(
        ocd= RteAbbreviationConfig.class
)
public class RteAbbreviationConfigService {

    private RteAbbreviationConfig configuration;

    @Activate
    protected void activate(RteAbbreviationConfig config) { this.configuration = config; }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public RteAbbreviationConfig getConfig() { return this.configuration; }

}
