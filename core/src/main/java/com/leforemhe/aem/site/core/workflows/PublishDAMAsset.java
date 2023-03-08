package com.leforemhe.aem.site.core.workflows;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.services.ResourceResolverService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Component(service = WorkflowProcess.class, property = {"process.label = Publish Graph Asset"})
public class PublishDAMAsset implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(PublishDAMAsset.class);

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
                if (resource.getResourceType().equals(Constants.DAM_ASSET)) {
                    replicator.replicate(workflowSession.getSession(), ReplicationActionType.ACTIVATE, resourcePath);
                    LOG.debug("Resource: {} ACTIVATED", resourcePath);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
