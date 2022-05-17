package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;

/**
 * Sling model for a list of links.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationModel {

    private final List<NavigationItemModel> items;

    /**
     * Constructor injecting request and child resource. Eases the mocking process for testd.
     * @param items navigation items child resource
     * @param request request
     */
    @Inject
    public NavigationModel(@ChildResource @Named("items") List<NavigationItemModel> items, SlingHttpServletRequest request) {
        this.items = items==null? Collections.emptyList() : items;
        this.items.forEach(item -> item.injectRequest(request));
    }

    public List<NavigationItemModel> getItems() {
        return items!=null?items: Collections.emptyList();
    }
}
