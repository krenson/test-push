package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Sling model of copyright component.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CopyrightModel {
    private static final String DATE_YEAR_FORMAT = "yyyy";
    @ValueMapValue
    private String label;

    public String getYear(){
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_YEAR_FORMAT));
    }

    public String getLabel() {
        return label;
    }
}
