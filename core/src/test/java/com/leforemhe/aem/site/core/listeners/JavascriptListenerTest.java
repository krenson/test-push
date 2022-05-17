package com.leforemhe.aem.site.core.listeners;

import com.leforemhe.aem.site.core.AbstractAEMTest;
import com.leforemhe.aem.site.core.config.JavascriptConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.services.DynamicJavascriptConfigService;
import com.leforemhe.aem.site.core.services.GlobalConfigService;
import com.leforemhe.aem.site.core.services.JavascriptToFileService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.sling.jcr.api.SlingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.ObservationManager;

import static org.mockito.Mockito.*;

class JavascriptListenerTest extends AbstractAEMTest {

    @Mock
    private DynamicJavascriptConfigService dynamicJavascriptConfigService;
    @Mock
    private JavascriptConfig javascriptConfig;
    @Mock
    private GlobalConfigService globalConfigService;
    @Mock
    private GlobalConfig globalConfig;
    @Mock
    private JavascriptToFileService javascriptToFileService;
    @Mock
    private Event event;
    @Mock
    private SlingRepository repository;
    @Mock
    private Session adminSession;
    @Mock
    private EventIterator eventIterator;
    @Mock
    private ObservationManager observationManager;
    @Mock
    private Workspace workspace;

    JavascriptListener javascriptListener = new JavascriptListener();

    @Override
    public void setup() throws RepositoryException, IllegalAccessException {
        lenient().when(dynamicJavascriptConfigService.getConfig()).thenReturn(javascriptConfig);
        lenient().when(globalConfigService.getConfig()).thenReturn(globalConfig);
        lenient().when(globalConfig.systemUser()).thenReturn("datawrite");
        lenient().when(javascriptConfig.pathToListen()).thenReturn("/content/path/to/listen.js");
        context.load().json("/com/leforemhe/aem/site/core/listeners/JavascriptListenerTest.json", "/content/path/to/listen.js");
        context.registerService(dynamicJavascriptConfigService);
        context.registerService(globalConfigService);
        context.registerService(javascriptToFileService);
        context.registerService(repository);
        lenient().when(adminSession.hasPermission(anyString(), anyString())).thenReturn(true);
        lenient().when(workspace.getObservationManager()).thenReturn(observationManager);
        lenient().when(adminSession.getWorkspace()).thenReturn(workspace);
        lenient().when(repository.loginService(anyString(), any())).thenReturn(adminSession);
        FieldUtils.writeField(javascriptListener, "repository", repository, true);
        FieldUtils.writeField(javascriptListener, "adminSession", adminSession, true);
        javascriptListener = context.registerInjectActivateService(javascriptListener);
        lenient().when(eventIterator.nextEvent()).thenReturn(event);
        lenient().when(eventIterator.hasNext()).thenReturn(true, false);
        lenient().when(event.getPath()).thenReturn("/content/path/to/listen.js/property");
    }

    @Test
    void testOnEvent() {
        javascriptListener.onEvent(eventIterator);
        verify(eventIterator, times(2)).hasNext();
        verify(eventIterator, times(1)).nextEvent();
        //verify(javascriptToFileService, times(1)).createJsFile(anyString(), any(), any());
    }
}
