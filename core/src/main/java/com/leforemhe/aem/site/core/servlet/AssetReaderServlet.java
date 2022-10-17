package com.leforemhe.aem.site.core.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "leforemhe/components/site/servlet/assetreaderservlet", methods = HttpConstants.METHOD_GET, extensions = "json")
@ServiceDescription("Asset Reader Servlet")
public class AssetReaderServlet extends SlingSafeMethodsServlet {

    public static final String CHART_DATA_PATH_PARAM = "chartDataPath";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        JsonObject errorResponse = new JsonObject();

        String chartDataPath = request.getParameter(CHART_DATA_PATH_PARAM);

        if (StringUtils.isNotEmpty(chartDataPath)) {
            Asset graphDataAsset = getGraphDataAsset(request, chartDataPath);
            if (graphDataAsset == null) {
                response.setStatus(404);
                errorResponse.addProperty("error", "Could not find graph data");
                response.getWriter().write(errorResponse.toString());
                return;
            }
            JsonObject parsedGraphData = parseGraphData(graphDataAsset);
            if (parsedGraphData != null) {
                response.setStatus(200);
                response.getWriter().write(parsedGraphData.toString());
                return;
            } else {
                response.setStatus(500);
                errorResponse.addProperty("error", "Could not parse graph data");
                response.getWriter().write(errorResponse.toString());
                return;
            }
        } else {
            response.setStatus(400);
            errorResponse.addProperty("error", "Add the chartDataPath parameter to your request");
            response.getWriter().write(errorResponse.toString());
            return;
        }
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

    private JsonObject parseGraphData(Asset graphDataAsset) {
        if (graphDataAsset != null) {
            return (JsonObject) JsonParser
                    .parseReader(new InputStreamReader(graphDataAsset.getRendition("original").getStream(),
                            StandardCharsets.UTF_8));
        }
        return null;
    }
}