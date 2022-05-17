package com.leforemhe.aem.site.core.models.pojo;

/**
 * Model to represent an Abbreviation
 * An Abbreviation is represented by a word/set of words and a abbreviation corresponding to this word/set of words
 */
public class Abbreviation {

    private String word;
    private String abbrev;

    /**
     * Instantiating an Abbreviation based on a given word/set of words and a abbreviation as parameter
     * @param word Word/set of words
     * @param abbrev Corresponding Abbreviation
     */
    public Abbreviation(String word, String abbrev) {
        this.word = word;
        this.abbrev = abbrev;
    }

    public String getWord() {
        return word;
    }

    public String getAbbreviation() {
        return abbrev;
    }
}
