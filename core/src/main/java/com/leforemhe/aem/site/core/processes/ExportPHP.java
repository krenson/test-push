package com.leforemhe.aem.site.core.processes;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.day.crx.JcrConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.models.ExportPHPPropertiesModel;
import com.leforemhe.aem.site.core.services.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

/**
 * Workflow to export a page to SFTP server in .php format
 */
@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=" + "Processus pour exporter des fichiers au format .php vers le serveur SFTP",
                "process.label" + "=" + "Exporter PHP vers serveur SFTP"
        }
)
public class ExportPHP implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ExportPHP.class);

    @Reference
    private ExportSFTPConfigService exportSFTPConfigService;
    @Reference
    private GlobalConfigService globalConfigService;
    @Reference
    private HTMLContentService htmlContentService;
    @Reference
    private ResourceResolverService resourceResolverService;
    @Reference
    private ParserService parserService;
    @Reference
    private ExportSFTPService exportSftpService;

    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) {
        LOG.debug("Inside execute");
        ExportSFTPConfig exportSFTPConfig =  exportSFTPConfigService.getConfig();
        GlobalConfig globalConfig =  globalConfigService.getConfig();
        String itemPath = (String) item.getWorkflowData().getPayload();
        LOG.debug("Starting exporting {}", itemPath);
        try (ResourceResolver resourceResolver = resourceResolverService.getResourceResolverFromSession(session.getSession())){
            if(resourceResolver!=null) {
                String requestPath = String.format("%s.%s", itemPath, "html");
                String content = htmlContentService.getHTMLContent(requestPath, resourceResolver);
                LOG.debug("Page {} fetched !", itemPath);
                content = parserService.parseAbsolutePathClientLibsCSS(content, globalConfig.publicServerURI());
                content = parserService.parseAbsolutePathClientLibsJavascript(content, globalConfig.publicServerURI());
                content = parserService.parseAbsolutePathImg(content, globalConfig.publicServerURI());
                content = parserService.parseAbsolutePathLink(content, globalConfig.publicServerURI());
                LOG.debug("Exporting Fetched Page...");
                Resource resource = resourceResolver.getResource(itemPath);
                if(resource==null) {
                    throw new ServletException("resourceResolver.getResource(itemPath) is null");
                }
                LOG.debug("resouce: {}", resource);
                Resource jcrContentResource = resource.getChild(JcrConstants.JCR_CONTENT);
                if(jcrContentResource==null) {
                    throw new ServletException("resourceResolver.getResource(itemPath) is null");
                }
                LOG.debug("jcrContentResource: {}", jcrContentResource);
                ExportPHPPropertiesModel model = jcrContentResource.adaptTo(ExportPHPPropertiesModel.class);
                if(model==null) {
                    throw new ServletException("Export properties are null");
                }
                LOG.debug("model: {}", model);
                String sftpBasePath = exportSFTPConfig.sftpBasePath();
                LOG.debug("Initial SFTP path: {}", sftpBasePath);
                String pathSeparator = globalConfig.pathSeparator();
                LOG.debug("pathSeparator value: {}", pathSeparator);
                if (sftpBasePath.endsWith(pathSeparator)) {
                    sftpBasePath = ((sftpBasePath.length() > 0) ? sftpBasePath.substring(0, sftpBasePath.length() - 1) : "");
                }
                if (sftpBasePath.startsWith(pathSeparator)) {
                    sftpBasePath = ((sftpBasePath.length() > 0) ? sftpBasePath.substring(1) : "");
                }
                LOG.debug("Final SFTP path: {}", sftpBasePath);
                sftpBasePath = sftpBasePath + pathSeparator + (model.getFolder() != null ? model.getFolder() : ".");
                LOG.debug("SFTP path result: {}", sftpBasePath);
                String filename = String.format("%s.%s", resource.getName(), "php");
                LOG.debug("filename: {}", filename);
                exportSftpService.exportFileFromConfig("ADD", content, filename, sftpBasePath, exportSFTPConfig);
            }
        } catch (ServletException e) {
            LOG.error("Something went wrong while fetching the page {}: {}", itemPath, e.getMessage());
        } finally {
            exportSftpService.closeConnections();
        }
    }
}