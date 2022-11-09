package com.leforemhe.aem.site.core.models.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class GraphDataSet {
    private String label;
    private List<Double> data;

    public GraphDataSet(String label, JsonArray data) {
        this.label = label;
        if (data != null) {
            this.data = new ArrayList<>();
            Iterator<JsonElement> dataListIt = data.iterator();
            while (dataListIt.hasNext()) {
                this.data.add(dataListIt.next().getAsBigDecimal().doubleValue());
            }
        }
    }

    public String getLabel() {
        return this.label;
    }

    public List<Double> getData() {
        return this.data;
    }
}
