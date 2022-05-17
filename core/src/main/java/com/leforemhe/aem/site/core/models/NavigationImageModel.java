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
public class NavigationImageModel {

    private final List<NavigationImageItemModel> items;

    private final List<NavigationItemModel> itemsListeAccesDirect;

    /**
     * Constructor injecting request and child resource. Eases the mocking process for testd.
     * @param items navigation items child resource
     * @param request request
     */
    @Inject
    public NavigationImageModel(@ChildResource @Named("items") List<NavigationImageItemModel> items, @ChildResource @Named("itemsListeAccesDirect") List<NavigationItemModel> itemsListeAccesDirect, SlingHttpServletRequest request) {
        this.items = items==null? Collections.emptyList() : items;
        this.items.forEach(item -> item.injectRequest(request));

        this.itemsListeAccesDirect = itemsListeAccesDirect==null? Collections.emptyList() : itemsListeAccesDirect;
        this.itemsListeAccesDirect.forEach(item -> item.injectRequest(request));

    }

    public List<NavigationImageItemModel> getItems() {
        return items!=null?items: Collections.emptyList();
    }

    public List<NavigationItemModel> getItemsListeAccesDirect() {
        return itemsListeAccesDirect!=null?itemsListeAccesDirect: Collections.emptyList();
    }

}
