package com.leforemhe.aem.site.core.models.pojo;

/**
 * Model to represent an Acronym.
 * An Acronym is represented by a set of words and an acronym representing this set of words
 */
public class Tag {

    private String title;
    private String backgroundColor;

    public Tag(String title, String backgroundColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
    }

    public String getTitle() {
        return title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
