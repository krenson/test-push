package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Sling Model of the Dynamic Javascript Configuration Model
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JsConfigModel {
    @ValueMapValue
    private String filename;
    @ValueMapValue
    private String environment;
    @ValueMapValue
    private String authorinstance;

    @ChildResource
    private List<PageRestriction> pagesRestrict;

    public String getFilename() {
        return filename;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getAuthorInstance() {
        return authorinstance;
    }

    public List<PageRestriction> getPagesRestrict() {
        return pagesRestrict== null ? Collections.emptyList() : new LinkedList<>(pagesRestrict);
    }

    /**
     * Sling Model of a Restriction of the Dynamic Javascript on a page/set of pages
     */
    @Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class PageRestriction {
        @ValueMapValue
        private String path;
        @ValueMapValue
        private String children;

        public String getPath(){
            return path;
        }

        /**
         *
         * @return true if the restriction also include the children of the pages defined
         */
        public boolean includeChildren(){
            return Boolean.TRUE.toString().equals(children);
        }
    }
}
