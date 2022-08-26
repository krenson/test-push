package com.leforemhe.aem.site.core.models.pojo;

import com.adobe.cq.dam.cfm.ContentFragment;

import java.util.List;

/**
 * Content Fragment Model Activites
 */
public class ContentFragmentModel {


    private String title;
    private List<String> savoir;
    private List<String> savoirFaire;
    private String id;

    /**
     * Model for ContentFragment Activites
     */
    public ContentFragmentModel(ContentFragment contentFragment) {
        this.title = contentFragment.getElement("description").getValue().getValue().toString();
        this.id = contentFragment.getElement("codeActivite").getValue().getValue().toString();
    }


    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

}
