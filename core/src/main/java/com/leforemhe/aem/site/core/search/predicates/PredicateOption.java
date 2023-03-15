package com.leforemhe.aem.site.core.search.predicates;

import java.util.Comparator;

public class PredicateOption {

    private final String label;
    private final String value;
    private boolean active;

    public PredicateOption(String label, String value) {
        this.label = label;
        this.value = value;
        this.active = false;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() { return active; }

    public static final class AlphabeticalByLabel implements Comparator<PredicateOption> {
        @Override
        public int compare(PredicateOption o1, PredicateOption o2) {
            return o1.getLabel().compareToIgnoreCase(o2.getLabel());
        }
    }
}