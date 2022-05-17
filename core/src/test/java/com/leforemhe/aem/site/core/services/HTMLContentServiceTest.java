package com.leforemhe.aem.site.core.services;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.leforemhe.aem.site.core.AbstractAEMTest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.engine.SlingRequestProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HTMLContentServiceTest extends AbstractAEMTest {

    private static final String FAKE_PATH = "http://localhost:4502/myressource";

    @Mock
    private RequestResponseFactory responseFactory;
    @Mock
    private SlingRequestProcessor requestProcessor;
    @Mock
    private ResourceResolver resourceResolver;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private HTMLContentService htmlContentService;

    @Override
    public void setup() {
        lenient().when(responseFactory.createRequest(HttpConstants.METHOD_GET, FAKE_PATH)).thenReturn(request);
    }

    @Test
    void getContent() throws ServletException, IOException {
        ArgumentCaptor<ByteArrayOutputStream> outCapture = ArgumentCaptor.forClass(ByteArrayOutputStream.class);
        lenient().when(responseFactory.createResponse(outCapture.capture())).thenReturn(response);
        String output = htmlContentService.getHTMLContent(FAKE_PATH, resourceResolver);
        assertEquals(outCapture.getValue().toString(), output);
        verify(responseFactory,times(1)).createResponse(outCapture.getValue());
        verify(responseFactory,times(1)).createRequest(HttpConstants.METHOD_GET, FAKE_PATH);
        verify(requestProcessor, times(1)).processRequest(request, response, resourceResolver);
    }
}
