package com.leforemhe.aem.site.core.workflows;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
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
import java.util.*;


@Component(service = WorkflowProcess.class, property = {"process.label = Create linked page for CF(s)"})
public class PageHandlerByIdCF implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(PageHandlerByIdCF.class);

    @Reference
    private ResourceResolverService resourceResolverService;

    @Reference
    private ContentFragmentUtilService contentFragmentUtilService;

    @Reference
    private QueryBuilder queryBuilder;

    private ResourceResolver resourceResolver;

    private String template;
    private String destinationFolder;
    private String action;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.debug("Executing the workflow create linked page");
        try {
            resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession());
            template = metaDataMap.get("pageTemplate", "empty");
            destinationFolder = metaDataMap.get("folderDestination", "empty");
            action = metaDataMap.get("action", "empty");
            boolean updatePage = action.equals(Constants.UPDATE);
            MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
            List<String> ids = (List<String>) map.get("allIds");
            if (template != null && destinationFolder != null && ids != null) {
                handlePageFromIDs(ids, updatePage);
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

    private void handlePageFromIDs(List<String> jobIDs, boolean updatePage) {
        Session session = this.resourceResolver.adaptTo(Session.class);
        PageManager pageManager = this.resourceResolver.adaptTo(PageManager.class);
        for (String id : jobIDs) {
            try {
                if (pageManager != null) {
                    switch (template) {
                        case "/conf/leforemhe/settings/wcm/templates/pge_resp_he_metier1":
                            if (updatePage) { handleJobPageUpdate(id, pageManager); } else { handleJobPageReplacement(id, pageManager, session);}
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

    private void handleJobPageReplacement(String jobId, PageManager pageManager, Session session) throws WCMException, RepositoryException {
        Job job = contentFragmentUtilService.getJobFromJobID(jobId);
        if (job != null) {
            Node pageNode = getPageNodeFromJobID(jobId);
            if (pageNode != null) {
                handlePageDeletion(pageNode, session, pageManager);
            }
            Page page = pageManager.create(destinationFolder, JcrUtil.createValidName(job.getName()), template, job.getTitle());
            LOG.debug("Page created for: {}", jobId);
            pageNode = resourceResolver.getResource(page.getPath() + "/jcr:content").adaptTo(Node.class);
            handlePageProperties(pageNode, job);
        } else {
            LOG.debug("No CF found for: {}", jobId);
        }
    }

    private void handleJobPageUpdate(String jobId, PageManager pageManager) throws WCMException, RepositoryException {
        Job job = contentFragmentUtilService.getJobFromJobID(jobId);
        if (job != null) {
            Node pageNode = getPageNodeFromJobID(jobId);
            if (pageNode == null) {
                Page page = pageManager.create(destinationFolder, JcrUtil.createValidName(job.getName()), template, job.getTitle());
                LOG.debug("Page created for: {}", jobId);
                pageNode = resourceResolver.getResource(page.getPath() + "/jcr:content").adaptTo(Node.class);
            }
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

    private void handlePageDeletion(Node pageNode, Session session, PageManager pageManager) throws RepositoryException, WCMException {
        if (pageNode.getProperty(JcrConstants.JCR_PRIMARYTYPE).getValue().toString().equals(Constants.CQ_PAGE)) {
            pageManager.delete(pageManager.getPage(pageNode.getPath()), false, true);
        } else {
            if  (pageNode.getParent().getProperty(JcrConstants.JCR_PRIMARYTYPE).getValue().toString().equals(Constants.CQ_PAGE)) {
                pageManager.delete(pageManager.getPage(pageNode.getParent().getPath()), false, true);
            }
        }
    }

    private Node getPageNodeFromJobID(String jobID) throws RepositoryException {
        Map<String, String> predicateMap = new HashMap();
        predicateMap.put("path", Constants.CONTENT_ROOT_PATH);
        predicateMap.put("type", Constants.CQ_PAGE);
        predicateMap.put("property", Constants.CLE_METIER_PROPERTY_CF);
        predicateMap.put("property.value", jobID);
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), resourceResolver.adaptTo(Session.class));

        SearchResult result = query.getResult();
        List <Hit> list = result.getHits();

        if (!list.isEmpty() && list.get(0).getNode() != null) {
            return list.get(0).getNode().getNode(JcrConstants.JCR_CONTENT);
        } else {
            return null;
        }
    }

}
