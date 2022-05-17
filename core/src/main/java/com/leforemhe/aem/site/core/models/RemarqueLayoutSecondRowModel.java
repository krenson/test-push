package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sling model of the second row of the Remarque Layout Component
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RemarqueLayoutSecondRowModel {

    @SlingObject
    private Resource resource;

    private List<RemarqueLayoutItemModel> items;

    /**
     * Initiating the items of the second row
     */
    @PostConstruct
    public void init() {
        if(items == null) {
            items = new ArrayList<>();
            for(Resource childResource : resource.getChildren()) {
                RemarqueLayoutItemModel item = new RemarqueLayoutItemModel(childResource);
                items.add(item);
            }
        }
    }

    public List<List<RemarqueLayoutItemModel>> getGroups() {
        List<List<RemarqueLayoutItemModel>> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i += 2) {
            List<RemarqueLayoutItemModel> item = new ArrayList<>();
            item.add(items.get(i));
            if (i + 1 < items.size()) {
                item.add(items.get(i + 1));
            }
            result.add(item);
        }
        return result;
    }

    public boolean isLastContainerFull() {
        return items.size() % 2 == 0;
    }

    public @NotNull List<RemarqueLayoutItemModel> getItems() {
        return new ArrayList<>(items);
    }
}
