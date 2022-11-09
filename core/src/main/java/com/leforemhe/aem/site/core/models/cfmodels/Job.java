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
    public static final String SECTORS_KEY = "secteurs";
    public static final String TREE_STRUCTURE_KEY = "arborescence";
    public static final String CODE_ESCO_KEY = "codeEsco";
    public static final String LIBELLE_ESCO_KEY = "refLibelleEsco";
    public static final String RELATED_JOBS_KEY = "metiersProches";
    public static final String POSSIBLE_JOBS_KEY = "metiersEnvisageables";
    public static final String STRUCTURES_KEY = "structures";
    public static final String ASSETS_KEY = "atouts";
    public static final String ENVIRONMENTS_KEY = "environnements";
    public static final String OBLIGATIONS_KEY = "obligations";
    public static final String VANITY_URL = "vanityUrl";
    public static final String OBLIGATOIRE_KEY = "obligatoire";
    public static final String CONDITIONS_KEY = "conditions";
    public static final String NAME_JOB_KEY = "nomMetier";

    public static final String CONTENT_FRAGMENT_MODEL_CONF = "/conf/leforemhe/settings/dam/cfm/models/metier";
    public static final String CONTENT_FRAGMENT_MODEL_TYPE = "job";

    private String codeMetier;
    private String title;
    private String description;
    private String[] synonymes;
    private List<JobTag> labels;
    private String codeEsco;
    private String libelleEsco;
    private String[] structures;
    private String[] environments;
    private String[] assets;
    private List<Job> relatedJobs = new ArrayList<>();
    private List<Job> possibleJobs = new ArrayList<>();
    private String link;
    private String[] obligations;
    private String[] obligatoire;
    private String[] tagIds;
    private String[] conditions;
    private String vanityUrl;
    private String name;

    public Job(ContentFragment contentFragment, List<Tag> labels, String link, String[] tagIds) {
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
        this.structures = ContentFragmentUtils.getMultifieldValue(contentFragment, STRUCTURES_KEY, String.class);
        this.environments = ContentFragmentUtils.getMultifieldValue(contentFragment, ENVIRONMENTS_KEY, String.class);
        this.assets = ContentFragmentUtils.getMultifieldValue(contentFragment, ASSETS_KEY, String.class);
        this.obligations = ContentFragmentUtils.getMultifieldValue(contentFragment, OBLIGATIONS_KEY, String.class);
        this.obligatoire = ContentFragmentUtils.getMultifieldValue(contentFragment, OBLIGATOIRE_KEY, String.class);
        this.tagIds = tagIds;
        this.link = link;
        this.conditions = ContentFragmentUtils.getMultifieldValue(contentFragment, CONDITIONS_KEY, String.class);
        this.vanityUrl = ContentFragmentUtils.getSingleValue(contentFragment, VANITY_URL, String.class);
        this.name = ContentFragmentUtils.getSingleValue(contentFragment, NAME_JOB_KEY, String.class);
    }

    public Job() {}

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

    public String[] getStructures() {
        return structures;
    }

    public String[] getEnvironments() {
        return environments;
    }

    public String[] getAssets() {
        return assets;
    }

    public List<Job> getRelatedJobs() {
        return relatedJobs;
    }

    public List<Job> getPossibleJobs() {
        return possibleJobs;
    }

    public String getLink() {
        return link;
    }

    public String[] getObligatoire() {
        return obligatoire;
    }

    public String getVanityUrl() {
        return vanityUrl;
    }

    public String getName() {
        return name;
    }

    public String[] getObligations() {
        return obligations;
    }

    public String[] getTagIds() {
        return tagIds;
    }

    public String[] getConditions() {
        return conditions;
    }

    public void setRelatedJobs(List<Job> relatedJobs) {
        this.relatedJobs = relatedJobs;
    }

    public void setPossibleJobs(List<Job> possibleJobs) {
        this.possibleJobs = possibleJobs;
    }

    public String getSingleTextValueFromElement(String element) {
        switch (element) {
            case DESCRIPTION_KEY: return getDescription();
            case CODE_METIER_KEY: return getCodeMetier();
            case NAME_JOB_KEY: return getName();
            default: return StringUtils.EMPTY;
        }
    }

    public String[] getMultiTextValueFromElement(String element) {
        switch (element) {
            case SYNONYMES_KEY: return getSynonymes();
            case ASSETS_KEY: return getAssets();
            case ENVIRONMENTS_KEY: return getEnvironments();
            case STRUCTURES_KEY: return getStructures();
            case OBLIGATIONS_KEY: return getObligations();
            case OBLIGATOIRE_KEY: return getObligatoire();
            case CONDITIONS_KEY: return getConditions();
            default: return ArrayUtils.EMPTY_STRING_ARRAY;
        }
    }

    public List<Job> getJobsFromElement(String element) {
        switch (element) {
            case POSSIBLE_JOBS_KEY: return getPossibleJobs();
            case RELATED_JOBS_KEY: return getRelatedJobs();
            default: return new ArrayList<>();
        }
    }

}
