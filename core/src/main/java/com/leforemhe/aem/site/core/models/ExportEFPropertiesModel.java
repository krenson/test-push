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
 * Sling model of ExportEF process.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExportEFPropertiesModel {

    private static final Logger LOG = LoggerFactory.getLogger(ExportEFPropertiesModel.class);

    private final String folder;
    private final String filename;
    private final String htmlToPrepend;
    private final String htmlToAppend;
    private final String navigationActivePage;
    private final boolean hideHamburgerMenuOnDesktop;
    private final boolean hideHamburgerMenuFully;
    private final boolean enableAPI;
    private final boolean enableSFTP;

    /**
     * Constructor injecting the request for page resolving.
     * @param folder folder
     * @param filename file name
     * @param htmlToPrepend html to prepend
     * @param htmlToAppend html to append
     * @param navigationActivePage navigation active page
     * @param hideHamburgerMenuOnDesktop hide hamburger menu
     * @param hideHamburgerMenuFully hide hamburger menu fully
     * @param enableAPI copy the EF's html into the /content/dam
     * @param enableSFTP copy the EF's html via SFTP
     *
     */
    @Inject
    public ExportEFPropertiesModel(@ValueMapValue @Named("exportFolder") String folder,
                                   @ValueMapValue @Named("exportFilename") String filename,
                                   @ValueMapValue @Named("exportPrependHTML") String htmlToPrepend,
                                   @ValueMapValue @Named("exportAppendHTML") String htmlToAppend,
                                   @ValueMapValue @Named("exportNavigationActivePage") String navigationActivePage,
                                   @ValueMapValue @Named("hideHamburgermenuOnDesktop") boolean hideHamburgerMenuOnDesktop,
                                   @ValueMapValue @Named("hideHamburgerMenuFully") boolean hideHamburgerMenuFully,
                                   @ValueMapValue @Named("enableAPI") boolean enableAPI,
                                   @ValueMapValue @Named("enableSFTP") boolean enableSFTP,
                                   @SlingObject Resource currentResource) {
        LOG.debug("Inside ExportEFPropertiesModel");

        if (filename != null && !filename.trim().isEmpty()) {
            this.filename = filename;
        } else {
            String efName;
            String efFolderName;
            String efVariationName;

            Resource efResource;
            Resource efFolderResource;
            Resource efVariationResource;

            efVariationResource = currentResource.getParent();
            efResource = efVariationResource != null ? efVariationResource.getParent() : null;
            efFolderResource = efResource != null ? efResource.getParent() : null;

            efVariationName = efVariationResource != null ? efVariationResource.getName().concat(".") : "";
            efName = efResource != null ? efResource.getName().concat(".") : "";
            efFolderName = efFolderResource != null ? efFolderResource.getName().concat(".") : "";

            this.filename = String.format("%s%s%s%s", efFolderName, efName, efVariationName, "html");
        }
        this.folder = folder;
        this.htmlToPrepend = htmlToPrepend != null ? htmlToPrepend : "";
        this.htmlToAppend = htmlToAppend != null ? htmlToAppend : "";
        this.navigationActivePage = navigationActivePage;
        this.hideHamburgerMenuOnDesktop = hideHamburgerMenuOnDesktop;
        this.hideHamburgerMenuFully = hideHamburgerMenuFully;
        this.enableAPI = enableAPI;
        this.enableSFTP = enableSFTP;
    }

    /**
     * Return the folder
     */
    public String getFolder() { return folder; }

    /**
     * Return the file name
     */
    public String getFilename() { return filename; }

    /**
     * Return the HTML to prepend
     */
    public String getHtmlToPrepend() {
        return htmlToPrepend;
    }

    /**
     * Return the HTML to append
     */
    public String getHtmlToAppend() {
        return htmlToAppend;
    }

    /**
     * Return the navigation active page
     */
    public String getNavigationActivePage() {
        return navigationActivePage;
    }

    /**
     * Return checkbok hide hamburger
     */
    public boolean getHideHamburgerMenu() {
        return hideHamburgerMenuOnDesktop;
    }

    /**
     * Return checkbok hide hamburger
     */
    public boolean getHideHamburgerMenuFully() {
        return hideHamburgerMenuFully;
    }

    /**
     * Return checkbok enable API
     */
    public boolean getEnableAPI() { return enableAPI; }

    /**
     * Return checkbok enable SFTP
     */
    public boolean getEnableSFTP() {
        return enableSFTP;
    }
}
