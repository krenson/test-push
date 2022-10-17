package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.leforemhe.aem.site.core.models.horizonemploi.Chart;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        Chart.class }, resourceType = ChartImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChartImpl implements Chart {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/chart";

    @ValueMapValue
    private String dataPath;

    @Self
    private SlingHttpServletRequest request;

    @Override
    public String getDataChartPath() {
        return dataPath;
    }

}
