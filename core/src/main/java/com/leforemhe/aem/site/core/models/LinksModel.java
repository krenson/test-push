package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sling model of Links component.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinksModel {
    private final List<NavigationItemModel> footerLinks;

    /**
     * Constructor injecting child resources. Eases the mocking process for tests.
     * @param footerLinks links used in the footer
     */
    @Inject
    public LinksModel(@ChildResource @Named("footerLinks") List<NavigationItemModel> footerLinks) {
        this.footerLinks = footerLinks==null ? Collections.emptyList() : new ArrayList<>(footerLinks);
    }

    /**
     * Constructor with empty fields. Eases the mocking process for tests.
     */
    public LinksModel(){
        this.footerLinks = new ArrayList<>();
    }

    public List<NavigationItemModel> getFooterLinks() {
        return new ArrayList<>(footerLinks);
    }

}
