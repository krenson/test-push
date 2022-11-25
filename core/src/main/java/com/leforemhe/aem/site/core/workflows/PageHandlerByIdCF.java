package com.leforemhe.aem.site.core.workflows;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import com.leforemhe.aem.site.core.services.ResourceResolverService;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


@Component(service = WorkflowProcess.class, property = {"process.label = Create linked page for CF(s)"})
public class PageHandlerByIdCF implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(PageHandlerByIdCF.class);

    @Reference
    private ResourceResolverService resourceResolverService;

    @Reference
    private ContentFragmentUtilService contentFragmentUtilService;

    private ResourceResolver resourceResolver;

    private String template;
    private String destinationFolder;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.debug("Executing the workflow create linked page");
        try {
            resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession());
            template = metaDataMap.get("pageTemplate", "empty");
            destinationFolder = metaDataMap.get("folderDestination", "empty");
            MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
            List<String> ids = (List<String>) map.get("idsMissingPage");
            if (template != null && destinationFolder != null && ids != null) {
                createPageFromIDs(ids);
            } else {
                if (ids == null) {
                    LOG.debug("Cannot create page, ids is null");
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    //J130501-1

    private void createPageFromIDs(List<String> jobIDs) {
        Session session = this.resourceResolver.adaptTo(Session.class);
        PageManager pageManager = this.resourceResolver.adaptTo(PageManager.class);
        for (String id : jobIDs) {
            try {
                if (pageManager != null) {
                    switch (template) {
                        case "/conf/leforemhe/settings/wcm/templates/pge_resp_he_metier1": handleJobPageCreation(id, pageManager);
                    }
                }
                if (session != null) {
                    session.save();
                }
            } catch (WCMException | RepositoryException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    private void handleJobPageCreation(String jobId, PageManager pageManager) throws WCMException {
        Job job = contentFragmentUtilService.getJobFromJobID(jobId);
        if (job != null) {
            Page page = pageManager.create(destinationFolder, JcrUtil.createValidName(job.getName()), template, job.getTitle());
            LOG.debug("Page created for: {}", jobId);
            Node pageNode = resourceResolver.getResource(page.getPath() + "/jcr:content").adaptTo(Node.class);
            handlePageProperties(pageNode, job);
        } else {
            LOG.debug("No CF found for: {}", jobId);
        }
    }

    private void handlePageProperties(Node pageNode, Job job) {
        try {
            pageNode.setProperty(JcrConstants.JCR_TITLE, job.getTitle());
            pageNode.setProperty(JcrConstants.JCR_DESCRIPTION, job.getDescription());
            pageNode.setProperty(Constants.CQ_TAGS, job.getTagIds());
            pageNode.setProperty(Constants.SLING_VANITY_PATH, job.getVanityUrl());
            pageNode.setProperty(Constants.CLE_METIER, job.getCodeMetier());
            pageNode.setProperty(Constants.LAST_UPDATED, Calendar.getInstance(TimeZone.getTimeZone("CET")));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

}
