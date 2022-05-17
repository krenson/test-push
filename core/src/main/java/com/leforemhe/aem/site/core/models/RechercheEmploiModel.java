package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.services.JobSearchConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Sling model of the Recherche Emploi Component
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RechercheEmploiModel {

    private final JobSearchConfigService jobSearchConfigService;

    private final ResourceResolver resourceResolver;

    private final String helpUrl;

    @ValueMapValue
    private String label;

    private static final Logger LOG = LoggerFactory.getLogger(RechercheEmploiModel.class);

    /**
     * Public constructor initiating the fields
     *
     * @param jobSearchConfigService Job Search Configuration Service
     * @param resourceResolver CRX Resource Resolver
     * @param helpUrl Url of the help page
     */
    @Inject
    public RechercheEmploiModel(@OSGiService JobSearchConfigService jobSearchConfigService, @SlingObject ResourceResolver resourceResolver, @ValueMapValue @Named("helpUrl") String helpUrl) {
        this.jobSearchConfigService = jobSearchConfigService;
        this.helpUrl = helpUrl;
        this.resourceResolver = resourceResolver;
    }

    public String getHelpUrl() {
        LOG.debug("RechercheEmploiModel: Inside getHelpUrl");

        return ModelUtils.getVanityOfPageIfExists(helpUrl, resourceResolver);
    }

    public String getJobCountUrl() { return jobSearchConfigService.getConfig().jobSearchCountLink(); }

    public String getJobSearchRequestPath() { return jobSearchConfigService.getConfig().jobSearchRequestPath(); }

    public String getLabel() {
        return label;
    }
}
