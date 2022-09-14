package com.leforemhe.aem.site.core.models.cfmodels;

import java.util.ArrayList;
import java.util.List;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.tagging.Tag;
import com.leforemhe.aem.site.core.models.utils.ContentFragmentUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Job {
    public static final String CODE_METIER_KEY = "codeMetier";
    public static final String TITLE_KEY = "titre";
    public static final String DESCRIPTION_KEY = "description";
    public static final String SYNONYMES_KEY = "synonymes";
    public static final String LABELS_KEY = "etiquettes";
    public static final String CODE_ESCO_KEY = "codeEsco";
    public static final String LIBELLE_ESCO_KEY = "refLibelleEsco";
    public static final String PROFESSIONS_NEARBY_KEY = "metiersProches";
    public static final String STRUCTURES_KEY = "structures";
    public static final String ASSETS_KEY = "atouts";
    public static final String ENVIRONMENTS_KEY = "environnements";
    public static final String CONTENT_FRAGMENT_MODEL_CONF = "/conf/leforemhe/settings/dam/cfm/models/metier";
    public static final String CONTENT_FRAGMENT_MODEL_TYPE = "job";

    private String codeMetier;
    private String title;
    private String description;
    private String[] synonymes;
    private List<JobTag> labels;
    private String codeEsco;
    private String libelleEsco;
    private String[] professionsNearby;
    private String[] structures;
    private String[] environments;
    private String[] assets;

    public Job(ContentFragment contentFragment, List<Tag> labels) {
        this.codeMetier = ContentFragmentUtils.getSingleValue(contentFragment, CODE_METIER_KEY, String.class);
        this.title = ContentFragmentUtils.getSingleValue(contentFragment, TITLE_KEY, String.class);
        this.description = ContentFragmentUtils.getSingleValue(contentFragment, DESCRIPTION_KEY, String.class);
        this.synonymes = ContentFragmentUtils.getMultifieldValue(contentFragment, SYNONYMES_KEY, String.class);
        this.labels = new ArrayList<>();
        for (Tag label : labels) {
            this.labels.add(new JobTag(label));
        }
        this.codeEsco = ContentFragmentUtils.getSingleValue(contentFragment, CODE_ESCO_KEY, String.class);
        this.libelleEsco = ContentFragmentUtils.getSingleValue(contentFragment, LIBELLE_ESCO_KEY, String.class);
        this.professionsNearby = ContentFragmentUtils.getMultifieldValue(contentFragment, PROFESSIONS_NEARBY_KEY,
                String.class);
        this.structures = ContentFragmentUtils.getMultifieldValue(contentFragment, STRUCTURES_KEY, String.class);
        this.environments = ContentFragmentUtils.getMultifieldValue(contentFragment, ENVIRONMENTS_KEY, String.class);
        this.assets = ContentFragmentUtils.getMultifieldValue(contentFragment, ASSETS_KEY, String.class);
    }

    public String getCodeMetier() {
        return codeMetier;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getSynonymes() {
        return synonymes;
    }

    public List<JobTag> getLabels() {
        return labels;
    }

    public String getCodeEsco() {
        return codeEsco;
    }

    public String getLibelleEsco() {
        return libelleEsco;
    }

    public String[] getProfessionsNearby() {
        return professionsNearby;
    }

    public String[] getStructures() {
        return structures;
    }

    public String[] getEnvironments() {
        return environments;
    }

    public String[] getAssets() {
        return assets;
    }

    public String getSingleTextValueFromElement(String element) {
        switch (element) {
            case DESCRIPTION_KEY: return getDescription();
            case CODE_METIER_KEY: return getCodeMetier();
            default: return StringUtils.EMPTY;
        }
    }

    public String[] getMultiTextValueFromElement(String element) {
        switch (element) {
            case SYNONYMES_KEY: return getSynonymes();
            case ASSETS_KEY: return getAssets();
            default: return ArrayUtils.EMPTY_STRING_ARRAY;
        }
    }
}
