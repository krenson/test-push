package com.leforemhe.aem.site.core.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class AssetReaderServletTest {
    private final AemContext ctx = new AemContext();

    @BeforeEach
    public void init() {
        ctx.addModelsForClasses(AssetReaderServlet.class);
        ctx.load().json("/com/leforemhe/aem/site/core/servlets/AssetReaderServletTest.json", "/content");
        ctx.load().binaryFile("/com/leforemhe/aem/site/core/test-resources/barchart-data-test.json",
                "/content/dam/chart-data.json/jcr:content/renditions/original");
    }

    @Test
    void getCorrectGraphData() throws ServletException, IOException {
        MockSlingHttpServletRequest request = ctx.request();
        MockSlingHttpServletResponse response = ctx.response();

        request.setMethod("GET");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(AssetReaderServlet.CHART_DATA_PATH_PARAM, "/content/dam/chart-data.json");
        request.setParameterMap(parameters);

        AssetReaderServlet servlet = ctx.registerInjectActivateService(new AssetReaderServlet());
        servlet.doGet(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    void getGraphDataFileNotFound() throws ServletException, IOException {
        MockSlingHttpServletRequest request = ctx.request();
        MockSlingHttpServletResponse response = ctx.response();

        request.setMethod("GET");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(AssetReaderServlet.CHART_DATA_PATH_PARAM, "/content/dam/chart-data-not-found.json");
        request.setParameterMap(parameters);

        AssetReaderServlet servlet = ctx.registerInjectActivateService(new AssetReaderServlet());
        servlet.doGet(request, response);

        assertEquals(404, response.getStatus());
    }

    @Test
    void doRequestWithoutParameter() throws ServletException, IOException {
        MockSlingHttpServletRequest request = ctx.request();
        MockSlingHttpServletResponse response = ctx.response();

        request.setMethod("GET");
        AssetReaderServlet servlet = ctx.registerInjectActivateService(new AssetReaderServlet());
        servlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }
}
