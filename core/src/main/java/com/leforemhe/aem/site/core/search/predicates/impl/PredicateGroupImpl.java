package com.leforemhe.aem.site.core.search.predicates.impl;

import com.leforemhe.aem.site.core.search.predicates.PredicateGroup;
import com.leforemhe.aem.site.core.search.predicates.PredicateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PredicateGroupImpl implements PredicateGroup {
    private final Logger log = LoggerFactory.getLogger(PredicateGroupImpl.class);

    private final String title;
    private final String name;
    private final List<PredicateOption> predicateOptions;

    public PredicateGroupImpl(String title, String name, List<PredicateOption> predicateOptions) {
        this.title = title;
        this.name = name;
        this.predicateOptions = predicateOptions;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<PredicateOption> getOptions() {
        return predicateOptions;
    }
}