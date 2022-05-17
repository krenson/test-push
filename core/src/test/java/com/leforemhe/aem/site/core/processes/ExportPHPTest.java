package com.leforemhe.aem.site.core.processes;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.leforemhe.aem.site.core.AbstractAEMTest;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.services.*;
import org.apache.sling.api.resource.LoginException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.jcr.Session;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("WeakerAccess")
class ExportPHPTest extends AbstractAEMTest {

    @Mock
    private WorkItem item;
    @Mock
    private WorkflowData workflowData;
    @Mock
    private WorkflowSession session;
    @Mock
    private MetaDataMap args;
    @Mock
    private ExportSFTPService exportSftpService;
    @Mock
    private ExportSFTPConfigService sftpConfigService;
    @Mock
    private ExportSFTPConfig exportSFTPConfig;
    @Mock
    private GlobalConfigService globalConfigService;
    @Mock
    private GlobalConfig globalConfig;
    @Mock
    private ResourceResolverService resourceResolverService;
    @Mock
    private ParserService parserService;
    @Mock
    private HTMLContentService htmlContentService;

    @InjectMocks
    private ExportPHP process;

    @Override
    public void setup() throws LoginException {

        lenient().when(globalConfig.publicServerURI()).thenReturn("test.resource.com");
        lenient().when(exportSFTPConfig.sftpBasePath()).thenReturn("path");

        lenient().when(sftpConfigService.getConfig()).thenReturn(exportSFTPConfig);
        lenient().when(globalConfigService.getConfig()).thenReturn(globalConfig);
        lenient().when(globalConfig.pathSeparator()).thenReturn("/");

        lenient().when(item.getWorkflowData()).thenReturn(workflowData);
        lenient().when(workflowData.getPayload()).thenReturn("/content/demo");
        lenient().when(session.getSession()).thenReturn(context.resourceResolver().adaptTo(Session.class));
        lenient().when(resourceResolverService.getResourceResolverFromSession(any())).thenReturn(context.resourceResolver().clone(null));
        lenient().when(htmlContentService.getHTMLContent(anyString(), any())).thenReturn("<p>content</p>");

        lenient().when(parserService.parseAbsolutePathClientLibsJavascript(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");
        lenient().when(parserService.parseAbsolutePathClientLibsCSS(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");
        lenient().when(parserService.parseAbsolutePathImg(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");
        lenient().when(parserService.parseAbsolutePathLink(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");

        context.load().json("/com/leforemhe/aem/site/core/processes/ExportPHPTest.json", "/content/demo");
    }

    @Test
    void testProcessOnPassword() {
        lenient().when(exportSFTPConfig.sftpAuthMethod()).thenReturn("password");
        process.execute(item, session, args);
        verify(sftpConfigService, times(1)).getConfig();
        verify(resourceResolverService, times(1)).getResourceResolverFromSession(any());
        verify(parserService, times(1)).parseAbsolutePathClientLibsCSS(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathClientLibsJavascript(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathImg(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathLink(any(), anyString());
        verify(htmlContentService, times(1)).getHTMLContent(eq("/content/demo.html"), any());
        verify(exportSftpService, times(1)).exportFileFromConfig(eq("ADD"),eq("<p>content</p>"), eq("demo.php"), eq("path/folder"), eq(exportSFTPConfig));
    }

    @Test
    void testProcessOnPublicKey() {
        lenient().when(exportSFTPConfig.sftpAuthMethod()).thenReturn("publickey");
        process.execute(item, session, args);
        verify(sftpConfigService, times(1)).getConfig();
        verify(resourceResolverService, times(1)).getResourceResolverFromSession(any());
        verify(parserService, times(1)).parseAbsolutePathClientLibsCSS(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathClientLibsJavascript(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathImg(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathLink(any(), anyString());
        verify(htmlContentService, times(1)).getHTMLContent(eq("/content/demo.html"), any());
        verify(exportSftpService, times(1)).exportFileFromConfig(eq("ADD"),eq("<p>content</p>"), eq("demo.php"), eq("path/folder"), eq(exportSFTPConfig));
    }
}
