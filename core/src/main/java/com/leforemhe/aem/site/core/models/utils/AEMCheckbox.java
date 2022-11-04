package com.leforemhe.aem.site.core.models.utils;

public class AEMCheckbox {
    private final String label;
    private final String value;
    private final String tagName;

    public AEMCheckbox(String label, String value) {
        this.label = label;
        // Replacing : and / because JS Queryselector couldn't handle it
        this.value = value.replaceAll(":", "-").replaceAll("/", "-");
        this.tagName = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public String getTagName() {
        return tagName;
    }
}
