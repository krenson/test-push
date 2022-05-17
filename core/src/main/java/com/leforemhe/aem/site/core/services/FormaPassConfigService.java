package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.config.FormaPassConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * FormaPass process Configuration Service
 */
@Component(
        immediate = true,
        service = FormaPassConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.FormaPassConfig"
)
@Designate(
        ocd=FormaPassConfig.class
)
public class FormaPassConfigService {

    private FormaPassConfig configuration;

    @Activate
    protected final void activate(FormaPassConfig config){ this.configuration = config; }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public FormaPassConfig getConfig() { return configuration;}

}
