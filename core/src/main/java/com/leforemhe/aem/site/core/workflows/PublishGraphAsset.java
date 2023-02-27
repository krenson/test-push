package com.leforemhe.aem.site.core.workflows;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.day.crx.JcrConstants;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.services.ResourceResolverService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component(service = WorkflowProcess.class, property = {"process.label = Publish Graph Asset"})
public class PublishGraphAsset implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(PublishGraphAsset.class);

    @Reference
    private ResourceResolverService resourceResolverService;

    @Reference
    private Replicator replicator;

    private ResourceResolver resourceResolver;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.debug("Into workflow publish graph asset");
        try {
            String payloadPath = workItem.getWorkflowData().getPayload().toString();

            resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession());
            Resource resource = resourceResolver.getResource(payloadPath);
            if (resource != null) {
                String resourcePath = resource.getPath();
                MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
                List<String> ids = new ArrayList<>();
                if (resource.getResourceType().equals(Constants.DAM_ASSET) && resourcePath.contains("chiffres-graphes")) {
                    String format = resource.getChild("jcr:content/metadata").getValueMap().get(Constants.DC_FORMAT).toString();
                    if (format.equals(Constants.APPLICATION_JSON)) {
                        replicator.replicate(workflowSession.getSession(), ReplicationActionType.ACTIVATE, resourcePath);
                        LOG.debug("Resource: {} ACTIVATED", resourcePath);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
