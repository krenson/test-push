package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Sling model of Hamburger menu component.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HamburgerMenuModel {
    private static final String NAVIGATION_COMPONENT = "navigation";

    @SlingObject
    private Resource currentResource;

    private final SlingHttpServletRequest request;
    private final GlobalConfigService globalConfigService;

    /**
     * NavigationModel field to store the resource once it has been retrieved.
     * This avoids to retrieve it multiple times
     * (for instance for testing if component is ready before showing its content).
     */
    private List<NavigationItemModel> navigationItems;

    private static final Logger LOG = LoggerFactory.getLogger(HamburgerMenuModel.class);

    @ScriptVariable
    private Page currentPage;

    /**
     * Constructor injecting the request for page resolving.
     *
     * @param request             request
     * @param globalConfigService globalConfigService
     */
    @Inject
    public HamburgerMenuModel(SlingHttpServletRequest request, GlobalConfigService globalConfigService) {
        this.request = request;
        this.globalConfigService = globalConfigService;
    }

    /**
     * Initiates the component. This method is called automatically once the sling model is instantiated.
     */
    @PostConstruct
    public void init() {
        Resource header = currentResource.getParent();
        Resource navigationResource;
        if (header == null || (navigationResource = header.getChild(NAVIGATION_COMPONENT)) == null) {
            navigationItems = Collections.emptyList();
            LOG.error("Parent of Hamburger Menu is not a Header component");
            return;
        }
        NavigationModel navigationModel = navigationResource.adaptTo(NavigationModel.class);
        navigationItems = Objects.requireNonNull(navigationModel).getItems();
        if (navigationItems == null) {
            navigationItems = Collections.emptyList();
        }
        navigationItems.forEach(navigationItemModel -> navigationItemModel.injectRequest(request));
    }

    /**
     * Return the first level page of the current page.
     */
    public Page getFirstLevelPage() {
        // if under experience frag -> return home page
        if (isCurrentPageExperienceFragment()) {
            Resource resource = currentResource.getResourceResolver().getResource(globalConfigService.getConfig().contentPath());
            return Objects.requireNonNull(resource).adaptTo(Page.class);
        }

        Page result = currentPage;
        while (result.getParent() != null && !result.getParent().getPath().equals(globalConfigService.getConfig().contentPath())) {
            result = result.getParent();
        }

        return result;
    }

    public boolean isCurrentPageExperienceFragment() {
        return currentPage.getPath().startsWith(globalConfigService.getConfig().experienceFragmentsPath());
    }

    public boolean isNavigationReady() {
        return navigationItems != null && !navigationItems.isEmpty();
    }

    public NavigationItemModel getActiveNavigationItem() {
        if (!isNavigationReady()) {
            return null;
        }
        return navigationItems.stream()
                .filter(NavigationItemModel::isActive)
                .findFirst().orElse(null);
    }

    public List<NavigationItemModel> getInactiveNavigationItems() {
        return navigationItems.stream().filter(navModel -> !navModel.isActive())
                .collect(Collectors.toList());
    }

    // Is used in Hamburgernavigation, so we need to go one level higher to find main hamburgermenu
    public String getFirstLevelPrefix() {
        Resource hamburgerMenu = currentResource.getParent().getChild("hamburgermenu");
        return hamburgerMenu.getValueMap().get("firstLevelPrefix") == null ? "" : String.format("%s ", hamburgerMenu.getValueMap().get("firstLevelPrefix"));
    }

    @SuppressWarnings("WeakerAccess")
    public List<NavigationItemModel> getNavigationItems() {

        return new ArrayList<>(navigationItems);
    }

    /**
     * Return whether the hamburgermenu of the current page should be hidden on desktop.
     * <p>
     * This method returns false when either the hideHamburgermenuOnDesktop of this page
     * or of one of its parent pages is set to true. Otherwise true.
     */
    public boolean isHamburgermenuHiddenOnDesktop() {
        try {
            Boolean isHamburgermenuHiddenOnDesktop = currentPage.getProperties().get("hideHamburgermenuOnDesktop", Boolean.class);
            if (isHamburgermenuHiddenOnDesktop == null) {
                isHamburgermenuHiddenOnDesktop = false;
            }

            Page currentParent = currentPage;
            while (Boolean.TRUE.equals(!isHamburgermenuHiddenOnDesktop) && (currentParent = currentParent.getParent()) != null) {
                isHamburgermenuHiddenOnDesktop = currentParent.getProperties().get("hideHamburgermenuOnDesktop", Boolean.class);
                if (isHamburgermenuHiddenOnDesktop == null) {
                    isHamburgermenuHiddenOnDesktop = false;
                }
            }

            return isHamburgermenuHiddenOnDesktop;
        } catch (NoClassDefFoundError e) {
            LOG.error("Something went wrong with the hambuergermenu: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Return whether this hamburgermenu of the current header resource should be completely hidden (the html should be empty).
     */
    public boolean isHamburgerFullyHidden() {
        Boolean hideHamburgermenuFully = currentPage.getProperties().get("hideHamburgermenuFully", Boolean.class);
        if (Boolean.TRUE.equals(hideHamburgermenuFully)) {
            return hideHamburgermenuFully;
        }
        return false;
    }

    /**
     * Check whether the current hamburgermenu resource is inside the Header Agenda variation.
     */
    public boolean isInsideAgendaVariation() {
        var headerAgendaReference = "leforemhe/components/site/header/headeragenda";
        var headerResource = currentResource.getParent();
        if (headerResource != null) {
            return headerResource.isResourceType(headerAgendaReference);
        }
        return false;
    }

    /**
     * Returns size of firstpage menu items
     */
    public int getSizeFirstpageLevelChildren() {
        Iterable<Page> iterable = () -> getFirstLevelPage().listChildren();
        int size = (int) StreamSupport.stream(iterable.spliterator(), false).count();

        if (size > 4) {
            size = 4;
        }

        return size;
    }
    public NavigationImageItemModel getForemJeunesData() {
        return currentResource.getParent().getChild("foremjeunes").adaptTo(NavigationImageItemModel.class);
    }
}
