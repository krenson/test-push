package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sling model of a column in a Remarque Layout Component
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RemarqueLayoutColumnModel {

    @SlingObject
    private Resource resource;

    private List<RemarqueLayoutItemModel> items;

    /**
     * Initiating the items of the columns
     */
    @PostConstruct
    public void init(){
        if(items == null) {
            items = new ArrayList<>();
            for (Resource itemResource : resource.getChildren()) {
                RemarqueLayoutItemModel item = new RemarqueLayoutItemModel(itemResource);
                items.add(item);
            }
        }
    }

    public String getPath() {
        return resource.getPath();
    }

    public String getName() {
        return resource.getName();
    }

    public List<RemarqueLayoutItemModel> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
