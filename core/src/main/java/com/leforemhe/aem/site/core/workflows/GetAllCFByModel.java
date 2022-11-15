package com.leforemhe.aem.site.core.workflows;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.services.ResourceResolverService;
import org.apache.jackrabbit.oak.jcr.Jcr;
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


@Component(service = WorkflowProcess.class, property = {"process.label = Get CF's by model"})
public class GetAllCFByModel implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllCFByModel.class);

    @Reference
    private ResourceResolverService resourceResolverService;

    private ResourceResolver resourceResolver;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.debug("Executing the workflow get all cf");
        try {
            String payloadPath = workItem.getWorkflowData().getPayload().toString();
            String model = metaDataMap.get("cfModel", "empty");
            resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession());
            Resource resource = resourceResolver.getResource(payloadPath);
            if (resource != null) {
                MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
                List<String> ids = new ArrayList<>();
                if (resource.getResourceType().equals(Constants.DAM_ASSET)) {
                    ids.add(resource.getChild("jcr:content/data/master").getValueMap().get("codeMetier").toString());
                } else {
                    List<Hit> hits = new ArrayList<>();
                    hits = getAllContentFragments(payloadPath, model);
                    ids = getJobIds(hits);
                }
                LOG.debug("ID's to check for a page: {}", ids);
                map.put("allIds", ids);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private List<Hit> getAllContentFragments(String path, String cfModel) {
        LOG.info("Inside get all cf query");
        Map<String, String> paramMap = new HashMap();
        paramMap.put("path", path);
        paramMap.put("type", Constants.DAM_ASSET);
        paramMap.put("p.limit", "-1");
        paramMap.put("1_property", "jcr:content/data/cq:model");
        paramMap.put("1_property.value", cfModel);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(PredicateGroup.create(paramMap), resourceResolver.adaptTo(Session.class));
        return query.getResult().getHits();
    }

    private List<String> getJobIds(List<Hit> hits) {
        List<String> jobIds = new ArrayList<>();
        for (Hit hit : hits) {
            String jobID = null;
            try {
                Object resource = hit.getResource().getChild("jcr:content/data/master").getValueMap().get("codeMetier");
                if (resource != null) {
                    jobID = resource.toString();
                }
            } catch (RepositoryException e) {
                LOG.error(e.getMessage(), e);
            }
            jobIds.add(jobID);
        }
        return jobIds;
    }

}
