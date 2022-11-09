package com.leforemhe.aem.site.core.services;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.leforemhe.aem.site.core.models.pojo.GraphData;

@Component(immediate = true, service = GraphDataService.class)
public class GraphDataService {
    private static final Logger LOG = LoggerFactory.getLogger(GraphDataService.class);

    public JsonObject getGraphDataInJson(SlingHttpServletRequest request, String path) {
        return parseGraphData(getGraphDataAsset(request, path));
    }

    public GraphData getChartData(SlingHttpServletRequest request, String path) {
        JsonObject object = getGraphDataInJson(request, path);
        return new GraphData(object);

    }

    private Asset getGraphDataAsset(SlingHttpServletRequest request, String path) {
        if (StringUtils.isNotEmpty(path)) {
            ResourceResolver resolver = request.getResourceResolver();
            Resource chartDataResource = resolver.getResource(path);
            if (chartDataResource != null && DamUtil.isAsset(chartDataResource)) {
                return chartDataResource.adaptTo(Asset.class);
            }
        }
        return null;
    }

    private Resource getGraphDataResource(SlingHttpServletRequest request, String path) {
        if (StringUtils.isNotEmpty(path)) {
            ResourceResolver resolver = request.getResourceResolver();
            Resource chartDataResource = resolver.getResource(path);
            return chartDataResource;
        }
        return null;
    }

    private JsonObject parseGraphData(Asset graphDataAsset) {
        if (graphDataAsset != null && graphDataAsset.getOriginal() != null) {
            return (JsonObject) JsonParser
                    .parseReader(new InputStreamReader(graphDataAsset.getOriginal().getStream(),
                            StandardCharsets.UTF_8));
        }
        return null;
    }

    private JsonObject parseGraphDataFromResource(Resource graphDataResource) {
        if (graphDataResource != null && graphDataResource.getChild("jcr:content") != null) {
            InputStream is = graphDataResource.getChild("jcr:content").adaptTo(InputStream.class);
            return (JsonObject) JsonParser
                    .parseReader(new InputStreamReader(is,
                            StandardCharsets.UTF_8));
        }
        return null;
    }

}
