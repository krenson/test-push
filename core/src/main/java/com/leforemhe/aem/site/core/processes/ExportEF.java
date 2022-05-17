package com.leforemhe.aem.site.core.processes;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.day.crx.JcrConstants;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.models.ExportEFPropertiesModel;
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
 * Workflow process to export an Experience Fragment to the SFTP server
 */
@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=" + "Processus pour exporter le contenu de Fragment d'Experience en un fichier au format .html vers le serveur SFTP",
                "process.label" + "=" + "Exporter Fragment d'Experience vers serveur SFTP"
        }
)
public class ExportEF implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ExportEF.class);

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
    @Reference
    private ExportAssetsService exportAssetsService;

    @Override
    public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        LOG.debug("Inside execute");
        ExportSFTPConfig exportSftpConfig = exportSFTPConfigService.getConfig();
        GlobalConfig globalConfig = globalConfigService.getConfig();
        String itemPath = (String) item.getWorkflowData().getPayload();
        LOG.debug("Starting exporting {}", itemPath);
        try (ResourceResolver resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession())) {
            if (resourceResolver != null) {
                Resource resource = resourceResolver.getResource(itemPath);
                if (resource == null) {
                    throw new ServletException("resourceResolver.getResource(itemPath) is null");
                }
                LOG.debug("resource: {}", resource);

                Resource jcrContentResource = resource.getChild(JcrConstants.JCR_CONTENT);
                if (jcrContentResource == null) {
                    throw new ServletException("resourceResolver.getResource(itemPath) is null");
                }
                LOG.debug("jcrContentResource: {}", jcrContentResource);

                ExportEFPropertiesModel model = jcrContentResource.adaptTo(ExportEFPropertiesModel.class);
                if (model == null) {
                    throw new ServletException("Export properties are null");
                }
                LOG.debug("model: {}", model);
                boolean enableAPI = model.getEnableAPI();
                LOG.debug("Enabled API: {}", enableAPI);
                boolean enableSFTP = model.getEnableSFTP();
                LOG.debug("Enable SFTP: {}", enableSFTP);
                if (enableSFTP || enableAPI) {
                    LOG.debug("Starting to generate the EF content...");
                    String requestPath = String.format("%s.%s.%s", itemPath, "content", "html");
                    String content = htmlContentService.getHTMLContent(requestPath, resourceResolver);
                    LOG.debug("EF {} fetched !", itemPath);
                    content = parserService.parseAbsolutePathClientLibsCSS(content, globalConfig.publicServerURI());
                    content = parserService.parseAbsolutePathClientLibsJavascript(content, globalConfig.publicServerURI());
                    content = parserService.parseAbsolutePathImg(content, globalConfig.publicServerURI());
                    content = parserService.parseAbsolutePathLink(content, globalConfig.publicServerURI());
                    LOG.debug("Exporting Fetched EF...");
                    String filename = model.getFilename();
                    LOG.debug("filename: {}", filename);
                    content = model.getHtmlToPrepend() + "\n" + content + "\n" + model.getHtmlToAppend();
                    LOG.debug("content: {}", content);
                    String pathSeparator = globalConfig.pathSeparator();
                    LOG.debug("pathSeparator value: {}", pathSeparator);
                    if (enableAPI) {
                        String exportContextPath = globalConfig.exportContextPath();
                        LOG.debug("Initial exportContextPath value: {}", exportContextPath);
                        if (!exportContextPath.startsWith(pathSeparator)) {
                            exportContextPath = pathSeparator + ((exportContextPath.length() > 0) ? exportContextPath.substring(1) : "");
                        }
                        if (exportContextPath.endsWith(pathSeparator)) {
                            exportContextPath = ((exportContextPath.length() > 0) ? (exportContextPath.substring(0, exportContextPath.length() - 1)) : "");
                        }
                        LOG.debug("Final exportContextPath value: {}", exportContextPath);
                        String assetsPath = globalConfig.assetsPath();
                        LOG.debug("Initial Assets path: {}", assetsPath);
                        if (!assetsPath.startsWith(pathSeparator)) {
                            assetsPath = pathSeparator + assetsPath;
                        }
                        if (assetsPath.endsWith(pathSeparator)) {
                            assetsPath = ((assetsPath.length() > 0) ? assetsPath.substring(0, assetsPath.length() - 1) : "");
                        }
                        LOG.debug("Final assets path: {}", assetsPath);
                        String apiPath = assetsPath + exportContextPath + (model.getFolder() != null ? (pathSeparator + model.getFolder()) : "");
                        LOG.debug("API path result: {}", apiPath);
                        exportAssetsService.exportFileFromConfig(content, filename, apiPath);
                    }
                    if (enableSFTP) {
                        String sftpBasePath = exportSftpConfig.sftpBasePath();
                        if (sftpBasePath.endsWith(pathSeparator)) {
                            sftpBasePath = sftpBasePath.substring(0, sftpBasePath.length() - 1);
                        }
                        if (sftpBasePath.startsWith(pathSeparator)) {
                            sftpBasePath = sftpBasePath.substring(1);
                        }
                        String sftpPath = sftpBasePath + pathSeparator + (model.getFolder() != null ? model.getFolder() : ".");
                        LOG.debug("SFTP path: {}", sftpPath);
                        exportSftpService.exportFileFromConfig("ADD", content, filename, sftpPath, exportSftpConfig);
                    }
                }
            }
        } catch (ServletException e) {
            LOG.error("Something went wrong while fetching the EF {}: {}", itemPath, e.getMessage());
        } finally {
            exportSftpService.closeConnections();
        }
    }
}
