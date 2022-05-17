package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.OpenGraphConfig;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Dynamic Javascript Configuration
 */
@Component(
        immediate = true,
        service = OpenGraphConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.OpenGraphConfig"
)
@Designate(
        ocd = OpenGraphConfig.class
)
public class OpenGraphConfigService {
    private OpenGraphConfig configuration;

    protected final void activate(OpenGraphConfig config){
        this.configuration = config;
    }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public String getType(){
        return configuration.type();
    }

    public String getSiteName(){
        return configuration.siteName();
    }

    public String getImage(){
        return configuration.image();
    }
}
