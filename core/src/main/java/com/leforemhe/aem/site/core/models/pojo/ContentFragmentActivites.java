package com.leforemhe.aem.site.core.models.pojo;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.wcm.core.components.models.ListItem;

import java.util.List;

/**
 * Content Fragment Model Activites
 */
public class ContentFragmentActivites implements ListItem {


    private String title;
    private List<String> savoir;
    private List<String> savoirFaire;
    private String id;

    /**
     * Model for ContentFragment Activites
     */
    public ContentFragmentActivites(ContentFragment contentFragment) {
        this.title = contentFragment.getElement("description").getValue().getValue().toString();
        this.id = contentFragment.getElement("codeActivite").getValue().getValue().toString();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
