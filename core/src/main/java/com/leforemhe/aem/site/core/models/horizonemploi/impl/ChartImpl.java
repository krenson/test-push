package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.leforemhe.aem.site.core.models.horizonemploi.Chart;
import com.leforemhe.aem.site.core.models.pojo.GraphData;
import com.leforemhe.aem.site.core.services.GraphDataService;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        Chart.class }, resourceType = ChartImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChartImpl implements Chart {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/chart";

    @ValueMapValue
    private String dataPath;

    @ValueMapValue
    private String tableDescription;

    @ValueMapValue
    private Boolean showAsPercentage;

    @ValueMapValue
    private String chartType;

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private GraphDataService graphDataService;

    private String generatedUUID;
    private GraphData graphData;

    @Override
    public String getDataChartPath() {
        return dataPath;
    }

    @Override
    public String getTableDescription() {
        return this.tableDescription;
    }

    @Override
    public String getID() {
        if (generatedUUID == null) {
            this.generatedUUID = UUID.randomUUID().toString();
        }
        return this.generatedUUID;
    }

    @Override
    public GraphData getChartData() {
        if (graphData == null) {
            this.graphData = graphDataService.getChartData(request, this.dataPath);
        }
        return this.graphData;
    }

    @Override
    public boolean getShowAsPercentage() {
        return showAsPercentage == null ? false : showAsPercentage;
    }

    @Override
    public String getChartType() {
        return ObjectUtils.defaultIfNull(chartType, "horizontal-bar");
    }

    

}
