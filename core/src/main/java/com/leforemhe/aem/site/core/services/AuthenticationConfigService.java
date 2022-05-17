package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.config.AuthenticationConfig;
import com.leforemhe.aem.site.core.config.JavascriptConfig;
import com.leforemhe.aem.site.core.servlet.ContactFormServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authentication Configuration
 */
@Component(
        immediate = true,
        service = AuthenticationConfigService.class,
        configurationPid = "com.leforemhe.aem.site.core.config.AuthenticationConfig"
)
@Designate(
        ocd = AuthenticationConfig.class
)
public class AuthenticationConfigService {
    private AuthenticationConfig configuration;

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationConfigService.class);

    @Activate
    protected final void activate(AuthenticationConfig config){ this.configuration = config; }

    /**
     * OSGi deactivation of the service
     */
    @Deactivate
    protected void deactivate() {
        // Add behaviour on OSGi deactivation of the service
    }

    public AuthenticationConfig getConfig() { return configuration;}

    public boolean authenticateUser(String username, String password) {
        LOG.debug("Inside authenticateUser");
        String[] users = configuration.users();
        for(String user : users){
            LOG.debug("Getting authentication config for all users...");
            String[] userCredentials = user.split(":");
            String foundUsername = userCredentials[0];
            String foundPassword = userCredentials[1];
            LOG.debug("Checking authentication config service with user: {}", foundUsername);
            if(org.apache.commons.lang3.StringUtils.equals(username, foundUsername) && org.apache.commons.lang3.StringUtils.equals(password, foundPassword)){
                LOG.debug("Authentication mapping was okay with user: {}", foundUsername);
                return true;
            }
        }
        return false;
    }
}
