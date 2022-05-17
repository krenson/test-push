package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Export to SFTP process Configuration Service
 */
@Component(
        immediate = true,
        service = ExportSFTPConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.ExportSFTPConfig"
)
@Designate(
        ocd=ExportSFTPConfig.class
)
public class ExportSFTPConfigService {

    private ExportSFTPConfig configuration;

    @Activate
    protected final void activate(ExportSFTPConfig config){ this.configuration = config; }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public ExportSFTPConfig getConfig() { return configuration;}

}
