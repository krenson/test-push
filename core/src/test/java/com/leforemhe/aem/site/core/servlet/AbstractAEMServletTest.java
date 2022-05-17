package com.leforemhe.aem.site.core.servlet;

import com.leforemhe.aem.site.core.AbstractAEMTest;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.model.MediaType;

import java.util.Random;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public abstract class AbstractAEMServletTest extends AbstractAEMTest {

    protected static final int MOCK_SERVER_PORT = 1080;

    @Mock
    protected SlingHttpServletRequest requestMock;
    @Mock
    protected SlingHttpServletResponse responseMock;

    private MockServerClient mockServerClient;

    @BeforeEach
    public void setupGeneric() throws Exception {
        this.setUpServer();
        super.setupGeneric();
    }

    @AfterEach
    public void stopServer() {
        mockServerClient.stop();
    }

    private void setUpServer() {
        ConfigurationProperties.initializationClass(this.getClass().getName());
        mockServerClient = startClientAndServer(MOCK_SERVER_PORT);
        byte[] body = new byte[20];
        new Random().nextBytes(body);
        mockServerClient.when(request())
                .respond(
                        response().withStatusCode(200)
                                .withContentType(MediaType.PNG)
                                .withBody(body)
                );
    }
}
