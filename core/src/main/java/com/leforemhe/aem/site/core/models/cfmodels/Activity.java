package com.leforemhe.aem.site.core.models.cfmodels;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.leforemhe.aem.site.core.models.utils.ContentFragmentUtils;

public class Activity {
    public final static String CONTENT_FRAGMENT_MODEL_CONF = "/conf/leforemhe/settings/dam/cfm/models/activite";
    public final static String CODE_ACTIVITY_KEY = "codeActivite";
    public final static String CODE_METIER_KEY = "codeMetier";
    public final static String DESCRIPTION_KEY = "description";
    public final static String SAVOIR_KEY = "savoir";
    public final static String SAVOIR_FAIRE_KEY = "savoirFaire";

    private String id;
    private String title;
    private String[] savoir;
    private String[] savoirFaire;

    public Activity(ContentFragment contentFragment) {
        this.title = ContentFragmentUtils.getSingleValue(contentFragment, DESCRIPTION_KEY, String.class);
        this.id = ContentFragmentUtils.getSingleValue(contentFragment, CODE_ACTIVITY_KEY, String.class);
        this.savoir = ContentFragmentUtils.getMultifieldValue(contentFragment, SAVOIR_KEY, String.class);
        this.savoirFaire = ContentFragmentUtils.getMultifieldValue(contentFragment, SAVOIR_FAIRE_KEY, String.class);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String[] getSavoir() {
        return savoir;
    }

    public String[] getSavoirFaire() {
        return savoirFaire;
    }

}
