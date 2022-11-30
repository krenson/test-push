package com.leforemhe.aem.site.core.models.horizonemploi;

import org.osgi.annotation.versioning.ProviderType;

import com.leforemhe.aem.site.core.models.pojo.GraphData;

@ProviderType
public interface Chart {
    String getDataChartPath();
    boolean getShowAsPercentage();
    String getTableDescription();
    String getID();
    GraphData getChartData();
    String getChartType();
    String getGraphType();
}
