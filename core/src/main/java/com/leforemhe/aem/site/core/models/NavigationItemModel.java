package com.leforemhe.aem.site.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * Sling model of a navigation item.
 * Can be an internal, external, or document link.
 * Can also be configured as "no link" in case the link type is not mandatory.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationItemModel {
    private static final String INTERNAL_LINK = "internal-link";
    private static final String NO_LINK = "no-link";
    private static final String EXTERNAL_LINK = "external-link";
    private static final String DOCUMENT_LINK = "document-link";

    /**
     * Target for opening the link in the same frame as it was clicked.
     */
    public static final String SELF_TARGET = "_self";
    /**
     * Target for opening the link in a new window or tab.
     */
    public static final String BLANK_TARGET = "_blank";
    /**
     * Target for opening the link in the parent frame.
     */
    public static final String PARENT_TARGET = "_parent";
    /**
     * Target for opening the link in the full body of the window.
     */
    public static final String TOP_TARGET = "_top";

    private static final Logger LOG = LoggerFactory.getLogger(NavigationItemModel.class);

    @Self
    private Resource resource;

    /**
     * Injected by parent model
     */
    private SlingHttpServletRequest request;

    @Inject
    private GlobalConfigService globalConfigService;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Default(values = NavigationItemModel.INTERNAL_LINK)
    @ValueMapValue
    private String linkType;
    @Default(values = NavigationItemModel.BLANK_TARGET)
    @ValueMapValue
    private String target;
    @Default(values = "")
    @ValueMapValue
    private String internalLinkReference;
    @ValueMapValue
    private String externalLinkReference;
    @ValueMapValue
    @Default(values = "")
    private String internalLabel;
    @ValueMapValue
    @Default(values = "")
    private String externalLabel;
    @ValueMapValue
    private String documentLabel;
    @ValueMapValue
    private String documentReference;
    @ValueMapValue
    @Default(values = "")
    private String title;
    @ValueMapValue
    @Default(values = "")
    private String description;

    /**
     * Linkpage used and initialized in case of an internal link.
     */
    private Page linkPage;

    /**
     * Init component.
     */
    @PostConstruct
    public void init() {
        getLinkPage();

        if (isInternal()) {
            target = NavigationItemModel.SELF_TARGET;
        }
    }

    public boolean isInternal() {
        return INTERNAL_LINK.equals(linkType) || StringUtils.isEmpty(linkType);
    }

    public boolean isExternal() {
        return EXTERNAL_LINK.equals(linkType) || StringUtils.isEmpty(linkType);
    }

    public boolean isDocument() {
        return DOCUMENT_LINK.equals(linkType);
    }

    /**
     * Return whether the link of this navigation item opens a new tab (or window).
     */
    public boolean opensNewTab() {
        return this.getTarget().equals(NavigationItemModel.BLANK_TARGET);
    }

    public String getLinkReference() {
        LOG.debug("NavigationItemModel: Inside getLinkReference");

        if (isDocument()) {
            return ModelUtils.getVanityUrlForAssetLink(documentReference, globalConfigService.getConfig().assetsPath());
        }
        return isInternal() ? ModelUtils.addHtmlExtensionIfNecessary(ModelUtils.getVanityOfPageIfExists(internalLinkReference, resourceResolver)) : externalLinkReference;
    }

    /**
     * Needed to check which navigation element need to be active in Header application variation
     * @return the full path of the link (not the vanity one)
     */
    public String getFullPath() {
        if (isDocument()) {
            return documentReference;
        }
        return isInternal() ? internalLinkReference : externalLinkReference;
    }

    public String getLabel() {
        if (isDocument()) {
            return documentLabel;
        }
        return isInternal() ? getInternalLabel() : externalLabel;
    }

    private String getInternalLabel() {
        return internalLabel.length()>0 ? internalLabel : getTitle();
    }

    public String getTitle() {
        if (isInternal() && getLinkPage() != null) {
            return  StringUtils.isEmpty(getLinkPage().getNavigationTitle()) ? getLinkPage().getPageTitle() : getLinkPage().getNavigationTitle();
        }
        return title;
    }

    public String getDescription() {
        if (isInternal() && getLinkPage() != null) {
            return linkPage.getDescription();
        }
        return description;
    }

    public boolean isActive() {
        LOG.debug("NavigationItemModel: Inside isActive");
        if(isExternal()){
            return false;
        }
        return ModelUtils.isParentPage(linkPage, getCurrentPage());
    }

    public List<Page> getChildPages() {
        LOG.debug("NavigationItemModel: Inside getChildPages");

        if (!isInternal()) {
            return Collections.emptyList();
        }
        List<Page> pages = new LinkedList<>();
        Resource pageResource = resourceResolver.resolve(getLinkReference());
        Objects.requireNonNull(pageResource).getChildren().forEach(childPage -> {
            if (ModelUtils.isPage(childPage)) pages.add(childPage.adaptTo(Page.class));
        });
        return pages;
    }


    void injectRequest(SlingHttpServletRequest request) {
        this.request = request;
    }

    private Page getCurrentPage() {
        LOG.trace("NavigationItemModel : Inside getCurrentPage");

        String requestPath = request.getPathInfo();
        String resourcePath;

        if(StringUtils.contains(requestPath,'.')) {
            resourcePath = StringUtils.substring(requestPath,0, StringUtils.indexOf(requestPath,'.'));
        }else{
            resourcePath = requestPath;
        }
        Resource pageResource = resourceResolver.resolve(resourcePath);
       return Objects.requireNonNull(pageResource)
                .adaptTo(Page.class);
    }

    public boolean isLink() {
        return !NO_LINK.equals(linkType);
    }

    /**
     * Get and instantiate linkPage of this navigation item and return it in case it is not yet instantiated and this navigation item is internal.
     */
    private Page getLinkPage() {
        if (linkPage == null && isInternal()) {
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            linkPage = Objects.requireNonNull(pageManager).getPage(internalLinkReference);
        }
        return linkPage;
    }

    /**
     * Return the dc:title property of the document linked to this navigation item.
     */
    public String getDocumentTitleFromDam() {
        if (!isDocument()) {
            throw new IllegalStateException("This navigation item is not a document");
        }

        return Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
                resourceResolver.getResource(documentReference))
                .getChild(JcrConstants.JCR_CONTENT))
                .getChild("metadata"))
                .getValueMap()
                .get(DamConstants.DC_TITLE, String.class);
    }

    public String getTarget() {
        return target;
    }

    public String getExtension() {
        if (isDocument()) {
            String[] pathSplitted = getFullPath().split("\\.");

            String extension = pathSplitted[pathSplitted.length - 1];

            return extension.toUpperCase();

        } else {
            return StringUtils.EMPTY;
        }
    }

    public String getDocumentLabel() {
        if (isDocument()) {
            String normalTitle = getLabel();

            Asset document = resourceResolver.getResource(getFullPath()).adaptTo(Asset.class);
            long fileSizeLong = document.getOriginal().getSize();
            String fileSize = readableFileSize(fileSizeLong);

            return normalTitle + " (" + getExtension() + " - " + fileSize + ")";
        } else {
            return StringUtils.EMPTY;
        }
    }

    private static String readableFileSize(long size) {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.FRENCH);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');

        if(size <= 0) return "0";
        final String[] units = new String[] { "o", "ko", "Mo", "Go", "To" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("##0.#", formatSymbols).format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
