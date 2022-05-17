package com.leforemhe.aem.site.core.models.pojo;

/**
 * Model to represent an Acronym.
 * An Acronym is represented by a set of words and an acronym representing this set of words
 */
public class Acronym {

    private String text;
    private String acronymDefinition;

    /**
     * Instantiating an Acronym based on a given set of words and given acronym
     * @param text Set of words
     * @param acronym Corresponding acronym
     */
    public Acronym(String text, String acronym) {
        this.text = text;
        this.acronymDefinition = acronym;
    }

    public String getText() {
        return text;
    }

    public String getAcronym() {
        return acronymDefinition;
    }
}
