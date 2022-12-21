package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.leforemhe.aem.site.core.models.horizonemploi.Chart;
import com.leforemhe.aem.site.core.models.pojo.GraphData;
import com.leforemhe.aem.site.core.services.GraphDataService;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {
        Chart.class }, resourceType = ChartImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChartImpl implements Chart {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/chart";
    public static final String CHART_SELECTOR = "%s_%s";
    public static final String CHARTS_ASSET_FOLDER = "/content/dam/leforemhe/fr/chiffres-graphes/";

    @ScriptVariable
    private Page currentPage;

    @ValueMapValue
    private String dataPath;

    @ValueMapValue
    private String tableDescription;

    @ValueMapValue
    private Boolean showAsPercentage;

    @ValueMapValue
    private String chartType;

    @ValueMapValue
    private String graphType;

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private GraphDataService graphDataService;

    private String generatedUUID;
    private GraphData graphData;
    private String automaticGeneratedPath;

    @PostConstruct
    public void init() {
        String cleMetier = currentPage.getProperties().get(Constants.CLE_METIER, String.class);
        automaticGeneratedPath = CHARTS_ASSET_FOLDER + String.format(CHART_SELECTOR, cleMetier.toLowerCase(), getGraphType());
    }

    @Override
    public String getDataChartPath() {
        return dataPath != null ? dataPath : automaticGeneratedPath;
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
            if(this.dataPath != null) {
                this.graphData = graphDataService.getChartData(request, this.dataPath);
            } else {
                this.graphData = graphDataService.getChartData(request, automaticGeneratedPath + Constants.JSON_EXTENSION);
            }
        }
        return this.graphData;
    }
    ///content/dam/leforemhe/fr/chiffres-graphes/A120301-1_Historique.json
    ///content/dam/leforemhe/fr/chiffres-graphes/A120301-1_Historique.json

    @Override
    public boolean getShowAsPercentage() {
        return showAsPercentage == null ? false : showAsPercentage;
    }

    @Override
    public String getChartType() {
        return ObjectUtils.defaultIfNull(chartType, "horizontal");
    }

    @Override
    public String getGraphType() {
        return graphType;
    }


}
