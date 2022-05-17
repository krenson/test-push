package com.leforemhe.aem.site.core.processes;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.day.crx.JcrConstants;
import com.jcraft.jsch.Channel;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.config.FormaPassConfig;
import com.leforemhe.aem.site.core.config.GlobalConfig;
import com.leforemhe.aem.site.core.models.ExportEFPropertiesModel;
import com.leforemhe.aem.site.core.models.ModelUtils;
import com.leforemhe.aem.site.core.services.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.util.*;

/**
 * Workflow to export a page to SFTP server in .php format
 */
@Component(service = WorkflowProcess.class, property = {
        Constants.SERVICE_DESCRIPTION + "="
                + "Processus pour exporter des fichiers FormaPass au format .jsp vers le serveur SFTP",
        "process.label" + "=" + "Exporter FormaPass vers serveur SFTP" })
public class ExportFormaPass implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ExportFormaPass.class);
    private static final String ALL_CONTENT = "ALL";
    @Reference
    private ExportSFTPConfigService exportSFTPConfigService;
    @Reference
    private FormaPassConfigService formaPassConfigService;
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
        ExportSFTPConfig exportSFTPConfig = exportSFTPConfigService.getConfig();
        GlobalConfig globalConfig = globalConfigService.getConfig();
        FormaPassConfig formaPassConfig = formaPassConfigService.getConfig();

        String[] params = args.get("PROCESS_ARGS", "string").toString().split(",");
        LOG.debug("Param Argument {}", params[0]);

        String itemPath = (String) item.getWorkflowData().getPayload();
        LOG.debug("Starting exporting {}", itemPath);

        List<Resource> newResources = new ArrayList<Resource>();

        if (StringUtils.startsWith(itemPath, formaPassConfig.formapassBaseName())) {
            try (ResourceResolver resourceResolver = resourceResolverService
                    .getResourceResolverFromSession(session.getSession())) {
                if (resourceResolver != null) {
                    Resource pageResource = resourceResolver.getResource(itemPath);
                    if (pageResource == null) {
                        throw new ServletException("resourceResolver.getResource(itemPath) is null");
                    }

                    Resource jcrContentResource = pageResource.getChild(JcrConstants.JCR_CONTENT);
                    if (jcrContentResource == null) {
                        throw new ServletException("resourceResolver.getResource(itemPath) is null");
                    }
                    LOG.debug("jcrContentResource: {}", jcrContentResource);

                    ExportEFPropertiesModel model = jcrContentResource.adaptTo(ExportEFPropertiesModel.class);
                    if (model == null) {
                        throw new ServletException("Export properties are null");
                    }
                    LOG.debug("model: {}", model);
                    boolean enableSFTP = model.getEnableSFTP();
                    LOG.debug("Enable SFTP: {}", enableSFTP);

                    String pathSeparator = globalConfig.pathSeparator();
                    LOG.debug("pathSeparator value: {}", pathSeparator);

                    String sftpBasePath = exportSFTPConfig.sftpBasePath();
                    if (sftpBasePath.endsWith(pathSeparator)) {
                        sftpBasePath = sftpBasePath.substring(0, sftpBasePath.length() - 1);
                    }
                    if (sftpBasePath.startsWith(pathSeparator)) {
                        sftpBasePath = sftpBasePath.substring(1);
                    }

                    if (enableSFTP) {
                        LOG.debug("childPage: {}", pageResource);
                        newResources.add(pageResource);

                        if (StringUtils.equals(ALL_CONTENT, params[0]))
                            newResources.addAll(getChildPages(pageResource));
                        // Add File.lock
                        String sftpPathFileLock = sftpBasePath + pathSeparator
                                + (model.getFolder() != null ? model.getFolder() : ".");
                        LOG.debug("SFTP pathFile Lock Add: {}", sftpPathFileLock);
                        exportSftpService.exportFileFromConfig(exportSFTPConfig.sftpActionPut(), ".",
                                exportSFTPConfig.sftpFilelockName(), sftpPathFileLock, exportSFTPConfig);

                        // Export All content
                        LOG.debug("Nb {}", newResources.size());
                        for (Resource resource : newResources) {
                            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                            Page currentPage = Objects.requireNonNull(pageManager).getPage(resource.getPath());
                            LOG.debug(" Resourec path : {}", resource.getPath());
                            LOG.debug(" Vanity Url : {}", currentPage.getVanityUrl());

                            String requestPath = String.format("%s.%s", resource.getPath(), "html");
                            LOG.debug(" requestPath : {}", requestPath);
                            String content = htmlContentService.getHTMLContent(requestPath, resourceResolver);
                            content = parserService.parseAbsolutePathClientLibsCSS(content,
                                    globalConfig.publicServerURI());
                            content = parserService.parseAbsolutePathClientLibsJavascript(content,
                                    globalConfig.publicServerURI());
                            content = parserService.parseAbsolutePathImg(content, globalConfig.publicServerURI());
                            content = parserService.parseAbsolutePathLink(content, globalConfig.publicServerURI());
                            LOG.debug("Exporting Fetched Page...");

                            String filename = StringUtils.substringAfterLast(currentPage.getVanityUrl(), "/");
                            LOG.debug("filename: {}", filename);
                            String vanityPath = StringUtils.substringBeforeLast(currentPage.getVanityUrl(), "/");
                            LOG.debug("vanityPath: {}", vanityPath);

                            content = model.getHtmlToPrepend() + "\n" + content + "\n" + model.getHtmlToAppend();
                            // LOG.debug("content: {}", content);

                            String sftpPath = sftpBasePath + pathSeparator
                                    + (model.getFolder() != null ? model.getFolder() : ".") + vanityPath;
                            LOG.debug("SFTP path: {}", sftpPath);
                            exportSftpService.exportFileFromConfig(exportSFTPConfig.sftpActionPut(), content, filename,
                                    sftpPath, exportSFTPConfig);
                        }
                        // Remove File.lock
                        LOG.debug("SFTP pathFile Lock Remove: {}", sftpPathFileLock);
                        exportSftpService.exportFileFromConfig(exportSFTPConfig.sftpActionRemove(), ".",
                                exportSFTPConfig.sftpFilelockName(), sftpPathFileLock, exportSFTPConfig);
                    }
                }
            } catch (ServletException e) {
                LOG.error("Something went wrong while fetching the page {}: {}", itemPath, e.getMessage());
            } finally {
                exportSftpService.closeConnections();
            }
        } else {
            LOG.error("Something is not working properly. Check OSGi configuration to FormaPassConfig: {} - {}", itemPath, formaPassConfig.formapassBaseName());
        }
    }

    public List<Resource> getChildPages(Resource pageResource) {
        List<Resource> resources = new ArrayList<Resource>();
        Objects.requireNonNull(pageResource).getChildren().forEach(childPage -> {
            if (ModelUtils.isPage(childPage)) {
                LOG.debug("childPage: {}", childPage);
                resources.add(childPage);
                if (childPage.hasChildren())
                    resources.addAll(getChildPages(childPage));
            }
        });
        return resources;
    }
}