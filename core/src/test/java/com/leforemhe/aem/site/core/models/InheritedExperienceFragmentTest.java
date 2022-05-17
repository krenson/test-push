package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.ExperienceFragment;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ForModel(
        models = {InheritedExperienceFragment.class, ExperienceFragment.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/InheritedExperienceFragmentTest.json"
)
@SuppressWarnings("WeakerAccess")
class InheritedExperienceFragmentTest extends AbstractModelTest {
    private static final String EF_NODE_NAME = "ef-listes-acces-direct";

    @Mock
    private ExperienceFragment experienceFragmentMock;

    @BeforeEach
    public void setup(){
        context.registerInjectActivateService(new GlobalConfigService(),
                "contentPath", "/content/demo-page/page-1");
    }

    @Test
    void getFragmentVariationPathFromNode(){
        final String expected = "root-ef-path";
        context.currentResource("/content/demo-page/jcr:content/" + EF_NODE_NAME);
        Resource resource = context.request().getResource();
        InheritedExperienceFragment inheritedExperienceFragment = resource.adaptTo(InheritedExperienceFragment.class);
        assertNotNull(inheritedExperienceFragment);
        lenient().when(experienceFragmentMock.getLocalizedFragmentVariationPath())
                .thenReturn(resource.getValueMap().get(ExperienceFragment.PN_FRAGMENT_VARIATION_PATH, String.class));
        inheritedExperienceFragment.setCoreComponent(experienceFragmentMock);
        assertEquals(expected, inheritedExperienceFragment.getLocalizedFragmentVariationPath());
    }

    @Test
    void getFragmentVariationPathFromParentBeyondLimit(){
        context.currentResource("/content/demo-page/page-1/jcr:content/" + EF_NODE_NAME);
        Resource resource = context.request().getResource();
        InheritedExperienceFragment inheritedExperienceFragment = resource.adaptTo(InheritedExperienceFragment.class);
        assertNotNull(inheritedExperienceFragment);
        lenient().when(experienceFragmentMock.getLocalizedFragmentVariationPath())
                .thenReturn(null);
        inheritedExperienceFragment.setCoreComponent(experienceFragmentMock);
        assertNull(inheritedExperienceFragment.getLocalizedFragmentVariationPath());
    }

    @Test
    void getFragmentVariationPathFromNodeWithParents(){
        final String expected = "page2-ef-path";
        context.currentResource("/content/demo-page/page-1/page-2/jcr:content/" + EF_NODE_NAME);
        Resource resource = context.request().getResource();
        InheritedExperienceFragment inheritedExperienceFragment = resource.adaptTo(InheritedExperienceFragment.class);
        assertNotNull(inheritedExperienceFragment);
        lenient().when(experienceFragmentMock.getLocalizedFragmentVariationPath())
                .thenReturn(null);
        inheritedExperienceFragment.setCoreComponent(experienceFragmentMock);
        assertEquals(expected, inheritedExperienceFragment.getLocalizedFragmentVariationPath());
    }

    @Test
    void getFragmentVariationPathFromInheritance(){
        final String expected = "page2-ef-path";
        context.currentResource("/content/demo-page/page-1/page-2/page-3/jcr:content/" + EF_NODE_NAME);
        Resource resource = context.request().getResource();
        InheritedExperienceFragment inheritedExperienceFragment = resource.adaptTo(InheritedExperienceFragment.class);
        assertNotNull(inheritedExperienceFragment);
        lenient().when(experienceFragmentMock.getLocalizedFragmentVariationPath())
                .thenReturn(null);
        inheritedExperienceFragment.setCoreComponent(experienceFragmentMock);
        assertEquals(expected, inheritedExperienceFragment.getLocalizedFragmentVariationPath());
    }
}
