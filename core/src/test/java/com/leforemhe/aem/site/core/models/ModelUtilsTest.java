package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
@SuppressWarnings("WeakerAccess")
class ModelUtilsTest {
    private static final String KNOWN_CHILD = "known-child";
    private static final String UNKNOWN_CHILD = "unknown-child";
    private static final String EXCEPTION_CHILD = "exception-child";
    private final AemContext context = new AemContext();

    @Mock
    private Resource resourceMock;
    @Mock
    private Node nodeMock;

    @BeforeEach
    public void setUp() throws RepositoryException {
        lenient().when(resourceMock.adaptTo(Node.class)).thenReturn(nodeMock);
        lenient().when(nodeMock.hasNode(KNOWN_CHILD)).thenReturn(true);
        lenient().when(nodeMock.hasNode(UNKNOWN_CHILD)).thenReturn(false);
        lenient().when(nodeMock.hasNode(EXCEPTION_CHILD)).thenThrow(new RepositoryException());
    }

    @Test
    void hasChildTest() {
        assertTrue(ModelUtils.hasChild(resourceMock, KNOWN_CHILD));
        assertFalse(ModelUtils.hasChild(resourceMock, UNKNOWN_CHILD));
    }

    @Test
    void hasChildReturnsFalseOnException(){
        assertFalse(ModelUtils.hasChild(resourceMock, EXCEPTION_CHILD));
    }

    @Test
    void isPageWithNoPrimaryType() {
        Resource resource = context.create().resource("/content/invalid-node",
                com.day.cq.commons.jcr.JcrConstants.JCR_TITLE, "node with no jcr:primaryType");

        assertFalse(ModelUtils.isPage(resource));
    }

    @Test
    void isParentWithParentNull(){
        Resource resource = context.create().resource("/content/page",
                com.day.cq.commons.jcr.JcrConstants.JCR_TITLE, "random page",
                com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE, com.day.cq.wcm.api.NameConstants.NT_PAGE);

        assertFalse(ModelUtils.isParentPage(null, resource.adaptTo(Page.class)));
    }

    @Test
    void getVanityUrlOfPage(){
        String componentPath = "/content/page",
                vanityPath = "vanityUrl.html";
        Resource componentResource = context.create().resource(componentPath,
                com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE, com.day.cq.wcm.api.NameConstants.NT_PAGE);
        context.create().resource(componentPath+"/"+com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT,
                com.day.cq.wcm.api.NameConstants.PN_SLING_VANITY_PATH, vanityPath);
        assertEquals(vanityPath, ModelUtils.getVanityOfPageIfExists(componentPath, context.resourceResolver()));
    }

    @Test
    void getVanityOfComponentReturnsUrlOfComponent() {
        String componentPath = "/content/page/component";
        Resource componentResource = context.create().resource(componentPath,
                com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE, com.day.cq.wcm.api.NameConstants.NT_COMPONENT);
        assertEquals(componentPath, ModelUtils.getVanityOfPageIfExists(componentPath, context.resourceResolver()));
    }

    @Test
    void getVanityUrlForFullAssetLink() {
        String assetsPath = "/content/dam/leforemhe/fr";
        String secondPart = "/documents/document.pdf";
        String assetLink = assetsPath + secondPart;

        assertEquals(secondPart, ModelUtils.getVanityUrlForAssetLink(assetLink, assetsPath));
    }

    @Test
    void getVanityUrlForAlreadyVanityAssetLink() {
        String assetLink = "/documents/document.pdf";

        assertEquals(assetLink, ModelUtils.getVanityUrlForAssetLink(assetLink, "/content/dam/leforemhe/fr"));
    }


    @Test
    void addHtmlExtensionWhenNecessaryTest() {
        String linkWithoutHtmlExtension = "/content/leforemhe/fr/this-is-a-random-link";
        String linkWithHtmlExtension = "/content/leforemhe/fr/this-is-a-random-link.html";

        assertEquals(linkWithHtmlExtension, ModelUtils.addHtmlExtensionIfNecessary(linkWithoutHtmlExtension));
    }

    @Test
    void dontAddHtmlExtensionWhenNotNecessaryTest() {
        String linkWithHtmlExtension = "/content/leforemhe/fr/this-is-a-random-link.html";

        assertEquals(linkWithHtmlExtension, ModelUtils.addHtmlExtensionIfNecessary(linkWithHtmlExtension));
    }
}
