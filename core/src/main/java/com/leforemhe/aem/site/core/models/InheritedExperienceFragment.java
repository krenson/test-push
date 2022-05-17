package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.ExperienceFragment;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Sling Model of the Inherited Experience Fragment Component
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, adapters = ExperienceFragment.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class InheritedExperienceFragment implements ExperienceFragment {
    @Self
    @Via(type = ResourceSuperType.class)
    private ExperienceFragment coreComponent;
    @SlingObject
    private Resource resource;
    @SlingObject
    private ResourceResolver resourceResolver;

    private final GlobalConfigService globalConfigService;

    private PageManager pageManager;

    @ValueMapValue
    private String fragmentPathRelativeToPage;

    /**
     * Public constructor instantiating the Global Configuration Service
     * @param globalConfigService Global Configuration Service
     */
    @Inject
    public InheritedExperienceFragment(GlobalConfigService globalConfigService) {
        this.globalConfigService = globalConfigService;
    }

    /**
     * Initiating the pageManager field
     */
    @PostConstruct
    public void init() {
        // turn the resourceResolver into a PageManager once
        pageManager = resourceResolver.adaptTo(PageManager.class);
    }

    @Override
    public String getLocalizedFragmentVariationPath() {
        if (coreComponent.getLocalizedFragmentVariationPath() != null) { // configured in dialog
            return coreComponent.getLocalizedFragmentVariationPath();
        } else {// try to inherit
            return getInheritedFragmentVariationPath();
        }
    }

    private String getInheritedFragmentVariationPath() {
        Page page = pageManager.getContainingPage(resource);
        Resource inheritedExperienceFragmentResource;
        ValueMap efProperties;
        while (page.getPath().contains(globalConfigService.getConfig().contentPath())
                && (inheritedExperienceFragmentResource = page.getContentResource(fragmentPathRelativeToPage)) != null) {
            efProperties = inheritedExperienceFragmentResource.getValueMap();
            if (efProperties.containsKey(ExperienceFragment.PN_FRAGMENT_VARIATION_PATH)) {
                return efProperties.get(ExperienceFragment.PN_FRAGMENT_VARIATION_PATH, String.class);
            }
            page = page.getParent();
        }
        return null;
    }

    /**
     * Allows to mock the core component in tests.
     *
     * @param coreComponent core component model
     */
    void setCoreComponent(ExperienceFragment coreComponent) {
        this.coreComponent = coreComponent;
    }
}
