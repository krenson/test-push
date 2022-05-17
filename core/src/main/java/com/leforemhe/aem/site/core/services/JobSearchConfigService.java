package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.JobSearchConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Job Search Configuration Service
 */
@Component(
        immediate = true,
        service = JobSearchConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.JobSearchConfig"
)
@Designate(
        ocd = JobSearchConfig.class
)
public class JobSearchConfigService {

    private JobSearchConfig configuration;

    @Activate
    protected void activate(JobSearchConfig config){ this.configuration = config; }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public JobSearchConfig getConfig() { return configuration;}
}
