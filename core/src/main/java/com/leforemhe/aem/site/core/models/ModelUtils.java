package com.leforemhe.aem.site.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Utility methods for the models.
 */
@SuppressWarnings("WeakerAccess")
public class ModelUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ModelUtils.class);

    /**
     * Private constructor of Models Utils. This class it not meant to be instantiated.
     */
    private ModelUtils() {
    }

    /**
     * Checks if the 'parent' is a parent page of 'child'.
     *
     * @param parent The supposed parent page.
     * @param child  The supposed child page.
     * @return true if 'parent' is parent of 'child', false otherwise.
     */
    public static boolean isParentPage(Page parent, Page child) {
        if (parent == null) {
            return false;
        }
        boolean isParent = false;
        Page intermediateParent = child;
        while (!isParent && intermediateParent != null) {
            isParent = intermediateParent.getPath().equals(parent.getPath());
            intermediateParent = intermediateParent.getParent();
        }
        return isParent;
    }

    /**
     * Checks if a resource is a page.
     *
     * @param resource the resource to be checked.
     * @return true if the resource is a page, false otherwise.
     */
    public static boolean isPage(Resource resource) {

        ValueMap resourceProps = resource.getValueMap();
        return resourceProps.containsKey(JcrConstants.JCR_PRIMARYTYPE)
                && resourceProps.get(JcrConstants.JCR_PRIMARYTYPE).equals("cq:Page");
    }

    /**
     * Check if a resource has a child
     *
     * @param resource     the resource to be checked.
     * @param resourceName the name of the child.
     * @return true if the resource has the child specified
     */
    public static boolean hasChild(Resource resource, String resourceName) {

        Node nodeResource = resource.adaptTo(Node.class);
        try {
            return (nodeResource != null) && nodeResource.hasNode(resourceName);
        } catch (RepositoryException e) {
            LOG.error("Error while checking if a resource has a child: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Get the vanity url of a page if it exists, otherwise return the given real path of the page.
     *
     * @param internalLinkReference the real path of the page
     * @param resourceResolver      the resource resolver to fetch the page resource object
     * @return the vanity url of the page if there's one, the real url otherwise
     */
    public static String getVanityOfPageIfExists(String internalLinkReference, ResourceResolver resourceResolver) {
        LOG.trace("ModelUtils: Inside vanityIfExists");
        Resource resource = resourceResolver.getResource(internalLinkReference);
        Page page;
        String vanityUrl;
        if (resource == null) {
            LOG.error("No resource at '{}'", internalLinkReference);
            return internalLinkReference;
        }
        page = resource.adaptTo(Page.class);
        if (page == null) {
            LOG.error("Resource '{}' is not a page", internalLinkReference);
            return internalLinkReference;
        }
        vanityUrl = page.getVanityUrl();
        return vanityUrl == null ? internalLinkReference : vanityUrl;
    }

    /**
     * Get the given linkreference with the html extension appended to it if necessary.
     *
     * @param internalLinkReference the given link reference to get with html extension
     */
    public static String addHtmlExtensionIfNecessary(String internalLinkReference) {
        if (!internalLinkReference.endsWith(".html")) {
            return internalLinkReference + ".html";
        }

        return internalLinkReference;
    }

    /**
     * Return the vanity url of given link reference of an asset by (eventually) removing the non vanity prefix.
     *
     * @param assetLinkReference the link reference of the asset (document, image) to return the vanity url from
     * @param nonVanityPrefix    the prefix of non vanity urls
     * @return the vanity url of given link reference of an asset
     */
    public static String getVanityUrlForAssetLink(String assetLinkReference, String nonVanityPrefix) {
        if (isAssetLinkFullUrl(assetLinkReference, nonVanityPrefix)) {
            assetLinkReference = assetLinkReference.replaceFirst(
                    nonVanityPrefix.endsWith("/") ? nonVanityPrefix.substring(0, nonVanityPrefix.length() - 1) : nonVanityPrefix,
                    "");
        }
        return assetLinkReference;
    }

    public static String getFullUrlForAssetLink(String assetLinkReference, String nonVanityPrefix) {
        if (!isAssetLinkFullUrl(assetLinkReference, nonVanityPrefix)) {
            if (assetLinkReference.startsWith("/")) {
                assetLinkReference = nonVanityPrefix + assetLinkReference;
            } else {
                assetLinkReference = nonVanityPrefix + "/" + assetLinkReference;
            }
        }
        return assetLinkReference;
    }

    /**
     * Check whether the given assetLinkReference (document or image) is a full url (and thus not a vanity url).
     *
     * @param assetLinkReference the given link reference to check whether it is a full url
     * @param nonVanityPrefix    the prefix of non vanity urls
     * @return whether the given assetLinkReference is a full url
     */
    public static boolean isAssetLinkFullUrl(String assetLinkReference, String nonVanityPrefix) {
        return (assetLinkReference == null) ? false : assetLinkReference.startsWith(nonVanityPrefix);
    }

    /**
     * Check whether the given linkReference (for pages) is a full url (and thus not a vanity url).
     *
     * @param linkReference   the given link reference to check whether it is a full url
     * @param nonVanityPrefix the prefix of non vanity urls
     * @return whether the given assetLinkReference is a full url
     */
    public static boolean isPageLinkFullUrl(String linkReference, String nonVanityPrefix) {
        return linkReference.startsWith(nonVanityPrefix);
    }

    public static String getFeaturedImageOfPage(String path, ResourceResolver resourceResolver) {
        String featuredImage = resourceResolver.getResource(path).getChild("jcr:content")
                .getChild("cq:featuredimage").getValueMap()
                .get("fileReference", String.class);
        if (featuredImage != null) {
            return featuredImage;
        }
        return StringUtils.EMPTY;
    }
}
