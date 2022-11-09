package com.leforemhe.aem.site.core.models.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GraphData {
    private List<String> labels;
    private List<GraphDataSet> dataset;

    public GraphData(JsonObject object) {
        if (object != null && object.has("labels") && object.has("datasets")) {
            JsonArray labelsArray = object.getAsJsonArray("labels");
            this.labels = new ArrayList<String>();
            Iterator<JsonElement> itLabels = labelsArray.iterator();
            while (itLabels.hasNext()) {
                this.labels.add(itLabels.next().getAsString());
            }
            this.dataset = new ArrayList();
            Iterator<JsonElement> itDatasets = object.getAsJsonArray("datasets").iterator();
            while (itDatasets.hasNext()) {
                JsonObject datasetObject = itDatasets.next().getAsJsonObject();
                String label = datasetObject.get("label").getAsString();
                JsonArray data = datasetObject.getAsJsonArray("data");
                this.dataset.add(new GraphDataSet(label, data));
            }
        }
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public List<GraphDataSet> getDataset() {
        return this.dataset;
    }
}
