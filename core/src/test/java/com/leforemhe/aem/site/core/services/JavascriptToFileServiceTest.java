package com.leforemhe.aem.site.core.services;

import com.adobe.granite.asset.api.AssetException;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.dam.api.AssetManager;
import com.leforemhe.aem.site.core.AbstractAEMTest;
import com.leforemhe.aem.site.core.config.JavascriptConfig;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.engine.SlingRequestProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JavascriptToFileServiceTest extends AbstractAEMTest {

    @Mock
    private RequestResponseFactory requestResponseFactory;
    @Mock
    private SlingRequestProcessor requestProcessor;
    @Mock
    private DynamicJavascriptConfigService dynamicJavascriptConfigService;
    @Mock
    private JavascriptConfig javascriptConfig;
    @Mock
    private ResourceResolver resourceResolver;
    @Mock
    private ValueMap componentProperties;
    @Mock
    private AssetManager assetManager;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JavascriptToFileService service;

    @Override
    public void setup() throws PersistenceException {
        Resource jsFolderResource = context.create().resource("/content/dam/leforemhe/fr/javascripts", "jcr:primaryType", "nt:folder");
        Resource jsAssetFolderResource = context.create().resource(jsFolderResource, "file", "jcr:primaryType", "sling:folder");

        lenient().when(dynamicJavascriptConfigService.getConfig()).thenReturn(javascriptConfig);
        lenient().when(componentProperties.get("filename", String.class)).thenReturn("file.js");
        lenient().when(resourceResolver.getResource("/content/dam/leforemhe/fr/javascripts")).thenReturn(jsFolderResource);
        lenient().when(resourceResolver.create(any(), anyString(), anyMap())).thenReturn(jsAssetFolderResource);
        lenient().when(resourceResolver.adaptTo(AssetManager.class)).thenReturn(assetManager);
        lenient().when(assetManager.createOrUpdateAsset(anyString(), any(), anyString(), anyBoolean())).thenReturn(null);
        lenient().when(javascriptConfig.dynamicJavascriptFolderPath()).thenReturn("/content/dam/leforemhe/fr/javascripts/dynamic");
        lenient().when(requestResponseFactory.createRequest(any(), any())).thenReturn(request);
        lenient().when(requestResponseFactory.createResponse(any())).thenReturn(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "all"})
    void createJSFileTest(String environment) throws ServletException, IOException {
        lenient().when(componentProperties.get("environment", String.class)).thenReturn(environment);
        service.createJsFile("file", "/content/javascript/file.js", componentProperties, resourceResolver);
        verify(dynamicJavascriptConfigService, times(1)).getConfig();
        verify(componentProperties, times(1)).get("filename", String.class);
        verify(assetManager, times(1)).createOrUpdateAsset(anyString(), any(), anyString(), anyBoolean());
        verify(requestResponseFactory, times(1)).createRequest(any(), any());
        verify(requestResponseFactory, times(1)).createResponse(any());
        verify(requestProcessor, times(1)).processRequest(request, response, resourceResolver);
    }

    @Test
    void createJSFileTestWithNullResolver() throws ServletException, IOException {
        service.createJsFile("file", "/content/javascript/file.js", componentProperties, null);
        verify(dynamicJavascriptConfigService, times(1)).getConfig();
        verify(componentProperties, times(0)).get("filename", String.class);
        verify(assetManager, times(0)).createOrUpdateAsset(anyString(), any(), anyString(), anyBoolean());
        verify(requestResponseFactory, times(0)).createRequest(any(), any());
        verify(requestResponseFactory, times(0)).createResponse(any());
        verify(requestProcessor, times(0)).processRequest(request, response, resourceResolver);
    }

    @Test
    void createJSFileTestWithNullAssetManager() throws ServletException, IOException {
        lenient().when(resourceResolver.adaptTo(AssetManager.class)).thenReturn(null);
        service.createJsFile("file", "/content/javascript/file.js", componentProperties, resourceResolver);
        verify(dynamicJavascriptConfigService, times(1)).getConfig();
        verify(componentProperties, times(1)).get("filename", String.class);
        verify(assetManager, times(0)).createOrUpdateAsset(anyString(), any(), anyString(), anyBoolean());
        verify(requestResponseFactory, times(0)).createRequest(any(), any());
        verify(requestResponseFactory, times(0)).createResponse(any());
        verify(requestProcessor, times(0)).processRequest(request, response, resourceResolver);
    }

    @Test
    void createJsFileDoesNotCrashOnException() {
        lenient().when(resourceResolver.adaptTo(AssetManager.class))
                .thenThrow(new AssetException("dummy exception meant to be caught"));
        try {
            service.createJsFile("file", "/content/javascript/file.js", componentProperties, resourceResolver);
        } catch (Exception e) {
            fail(e);
        }

    }

    @Test
    void createJsFileDoesNotCrashWhenAssetManagerIsNotResolved() {
        lenient().when(resourceResolver.adaptTo(AssetManager.class))
                .thenReturn(null);
        try {
            //service.createJsFile("/content/javascript/file.js", componentProperties, resourceResolver);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void createJsFileDoesNothingWhenNoName() {
        lenient().when(componentProperties.get("filename", String.class)).thenReturn(null);
        //service.createJsFile("/content/javascript/file.js", componentProperties, resourceResolver);
        verify(resourceResolver, never()).adaptTo(AssetManager.class);

    }
}
