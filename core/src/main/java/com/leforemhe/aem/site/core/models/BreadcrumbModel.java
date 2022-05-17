package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Breadcrumb;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;
import java.util.*;

/**
 * Sling model of Breadcrumb (filariane) component.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BreadcrumbModel implements Breadcrumb {

    @ScriptVariable
    private Page currentPage;

    @Self
    private Breadcrumb breadcrumb;

    private final GlobalConfigService globalConfigService;

    /**
     * Constructor injecting the global configuration service
     * @param globalConfigService globalConfigService
     */
    @Inject
    public BreadcrumbModel(GlobalConfigService globalConfigService) {
        this.globalConfigService = globalConfigService;
    }

    /**
     * Get all navigation items to display in the breadcrumb.
     *  This method first verifies whether one parent has checked the hideInBreadcrumb property.
     *  If so, a list with only the last item of the default items list is returned. Otherwise,
     *  the normal default behavior is applied and all navigation items are returned.
     */
    @Override
    public Collection<NavigationItem> getItems() {
        if (!isVisible()) {
            // Return list with only last navigationItem
            return removeAllExceptLast(breadcrumb.getItems());
        }
        return breadcrumb.getItems();
    }

    /**
     * Return a new Collection that contains only the last element of the given collection.
     *
     * @throws IllegalArgumentException if collection is empty.
     */
    private static Collection<NavigationItem> removeAllExceptLast(Collection<NavigationItem> c) {
        if (c.size() <= 1) {
            throw new IllegalArgumentException("Collection may not be empty");
        }

        List<NavigationItem> l = new ArrayList<>(c);

        while (l.size() > 1) {
            l.remove(0);
        }

        return l;
    }

    /**
     * Return whether the breadcrumb menu of the current page is visible for the entire navigation path.
     *
     *  This method returns false when either the hideInBreadcrumb of this page
     *  or of one of its parent pages is set to true. Otherwise true.
     */
    public boolean isVisible() {
        Page current = currentPage;

        while (current != null && !current.getPath().equals(globalConfigService.getConfig().contentPath())) {
            if (Boolean.TRUE.equals(current.getProperties().get("hideInBreadcrumb", Boolean.class))) {
                return false;
            }
            current = current.getParent();
        }
        return true;
    }

}
