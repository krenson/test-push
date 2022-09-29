package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.SearchConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Service to access the configuration. Can be injected via @Reference.
 */
@Component(
        immediate = true,
        service = SearchConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.SearchConfig"
)
@Designate(ocd = SearchConfig.class)
public class SearchConfigService {
    private SearchConfig configuration;

    @Activate
    protected final void activate(SearchConfig config) {
        this.configuration = config;
    }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public SearchConfig getConfig() {
        return configuration;
    }
}
