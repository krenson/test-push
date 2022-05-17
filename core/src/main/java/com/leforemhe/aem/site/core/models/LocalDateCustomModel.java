package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import java.time.LocalDate;

/**
 * A Sling Model for custom LocalDate manipulations
 */
@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LocalDateCustomModel {

    private final LocalDate now = LocalDate.now();

    /**
     * Output this date as a String with the ISO-8601 format uuuu-MM-dd.
     * For example: 2021-04-22.
     */
    public String getNow() {
        return now.toString();
    }

    /**
     * Output the date of 2 years ago as a String with the ISO-8601 format uuuu-MM-dd.
     * For example: 2019-04-22.
     */
    public String getDateTwoYearsAgo() {
        return now.minusYears(2).toString();
    }

    /**
     * Output the date of 100 years ago as a String with the ISO-8601 format uuuu-MM-dd.
     * For example: 1921-04-22.
     */
    public String getDateHundredYearsAgo() {
        return now.minusYears(100).toString();
    }
}
