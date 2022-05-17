package com.leforemhe.aem.site.core.processes;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.day.crx.JcrConstants;
import com.leforemhe.aem.site.core.AbstractAEMTest;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.services.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.jcr.Session;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ExportEFTest extends AbstractAEMTest {

    @Mock
    private WorkItem item;
    @Mock
    private WorkflowData workflowData;
    @Mock
    private WorkflowSession workflowSession;
    @Mock
    private MetaDataMap args;
    @Mock
    private ExportSFTPService exportSftpService;
    @Mock
    private ExportAssetsService exportAssetsService;
    @Mock
    private ExportSFTPConfigService exportSftpConfigService;
    @Mock
    private ExportSFTPConfig config;
    @Mock
    private GlobalConfigService globalConfigService;
    @Mock
    private GlobalConfig gconfig;
    @Mock
    private ResourceResolverService resourceResolverService;
    @Mock
    private ParserService parserService;
    @Mock
    private HTMLContentService htmlContentService;
    @Mock
    private ResourceResolver resourceResolver;


    @InjectMocks
    private ExportEF process;

    private static final String HEADER_PATH = "/content/experiencefragments/header/default/master";
    private static final String FOOTER_PATH = "/content/experiencefragments/footer/default/master";

    public void setup() throws LoginException {

        lenient().when(exportSftpConfigService.getConfig()).thenReturn(config);
        lenient().when(globalConfigService.getConfig()).thenReturn(gconfig);
        lenient().when(gconfig.assetsPath()).thenReturn("/content/dam/leforemhe/fr");
        lenient().when(gconfig.exportContextPath()).thenReturn("");
        lenient().when(gconfig.pathSeparator()).thenReturn("/");
        lenient().when(gconfig.publicServerURI()).thenReturn("test.resource.com");
        lenient().when(config.sftpBasePath()).thenReturn("path");
        lenient().when(item.getWorkflowData()).thenReturn(workflowData);
        lenient().when(workflowData.getPayload()).thenReturn("/content/experiencefragments/myef/default/master");
        lenient().when(workflowSession.getSession()).thenReturn(context.resourceResolver().adaptTo(Session.class));
        lenient().when(resourceResolverService.getResourceResolverFromSession(any())).thenReturn(context.resourceResolver().clone(null));
        lenient().when(htmlContentService.getHTMLContent(anyString(), any())).thenReturn("<p>content</p>");

        lenient().when(parserService.parseAbsolutePathClientLibsJavascript(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");
        lenient().when(parserService.parseAbsolutePathClientLibsJavascript(eq(""),any())).thenReturn("");
        lenient().when(parserService.parseAbsolutePathClientLibsCSS(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");
        lenient().when(parserService.parseAbsolutePathImg(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");
        lenient().when(parserService.parseAbsolutePathLink(eq("<p>content</p>"),any())).thenReturn("<p>content</p>");

        context.load().json("/com/leforemhe/aem/site/core/processes/ExportEFTest.json", "/content/experiencefragments");
    }

    @Test
    void testOnSimpleExperienceFragment() {
        process.execute(item, workflowSession, args);
        verify(exportSftpConfigService, times(1)).getConfig();
        verify(globalConfigService, times(1)).getConfig();
        verify(resourceResolverService, times(1)).getResourceResolverFromSession(any());
        verify(parserService, times(1)).parseAbsolutePathClientLibsCSS(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathClientLibsJavascript(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathImg(any(), anyString());
        verify(parserService, times(1)).parseAbsolutePathLink(any(), anyString());
        verify(htmlContentService, times(1)).getHTMLContent(eq("/content/experiencefragments/myef/default/master.content.html"), any());

        Resource resource = context.resourceResolver().getResource("/content/experiencefragments/myef/default/master");
        assertNotNull(resource);
        assertNotNull(resource.getChild(JcrConstants.JCR_CONTENT));

        verify(exportAssetsService, times(1)).exportFileFromConfig(eq("<p>head</p>"+"\n"+"<p>content</p>"+"\n"+"<p>footer</p>"), eq("filename"), eq("/content/dam/leforemhe/fr/folder"));
        verify(exportSftpService, times(1)).exportFileFromConfig(eq("ADD"),eq("<p>head</p>"+"\n"+"<p>content</p>"+"\n"+"<p>footer</p>"), eq("filename"), eq("path/folder"), eq(config));
    }
}
