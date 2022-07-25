package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.ContentFragmentConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;


/**
 * Service to access the configuration. Can be injected via @Reference.
 */
@Component(
        immediate = true,
        service = ContentFragmentConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.ContentFragmentConfig"
)
@Designate(ocd = ContentFragmentConfig.class)
public class ContentFragmentConfigService {
    private ContentFragmentConfig configuration;

    @Activate
    protected final void activate(ContentFragmentConfig config) {
        this.configuration = config;
    }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public ContentFragmentConfig getConfig() {
        return configuration;
    }
}
