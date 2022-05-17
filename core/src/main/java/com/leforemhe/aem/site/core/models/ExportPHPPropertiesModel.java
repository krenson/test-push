package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Sling model of ExportPHP process.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExportPHPPropertiesModel {

    private static final Logger LOG = LoggerFactory.getLogger(ExportEFPropertiesModel.class);

    private final String folder;

    /**
     * Constructor injecting the request for page resolving.
     * @param folder folder
     * @param currentResource resource
     */
    @Inject
    public ExportPHPPropertiesModel(@ValueMapValue @Named("exportFolder") String folder,
                                    @SlingObject Resource currentResource) {
        LOG.debug("Inside ExportPHPPropertiesModel");
        this.folder = folder;
    }

    /**
     * Return the folder
     */
    public String getFolder() {
        return folder;
    }
}
