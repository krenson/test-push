package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import javax.inject.Inject;

/**
 * Sling Model of a item in the Remarque Layout Component
 */
public class RemarqueLayoutItemModel {

    private String path;
    private Resource resource;

    /**
     * Constructor injecting the resource field and its path
     * @param resource Resource to inject
     */
    @Inject
    public RemarqueLayoutItemModel(Resource resource) {
        this.resource = resource;
        this.path = resource.getPath();
    }

    public String getPath() {
        return path;
    }
}
