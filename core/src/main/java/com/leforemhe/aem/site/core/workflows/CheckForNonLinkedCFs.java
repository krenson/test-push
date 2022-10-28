package com.leforemhe.aem.site.core.workflows;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.services.ResourceResolverService;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component(service = WorkflowProcess.class, property = {"process.label = Check which CF stills needs a page"})
public class CheckForNonLinkedCFs implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CheckForNonLinkedCFs.class);

    @Reference
    private ResourceResolverService resourceResolverService;

    private ResourceResolver resourceResolver;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.debug("Executing the workflow check which cf needs a page");
        try {
            resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession());
            MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
            List<String> allIds = (List<String>) map.get("allIds");
            List<String> idsMissingPage = new ArrayList<>();
            for (String id : allIds) {
                if (!pageExists(id)) {
                    idsMissingPage.add(id);
                }
            }
            map.put("idsMissingPage", idsMissingPage);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }


    private boolean pageExists(String codeMetier) {
        Map<String, String> paramMap = new HashMap();
        paramMap.put("path", Constants.CONTENT_ROOT_PATH);
        paramMap.put("type", Constants.CQ_PAGE);
        paramMap.put("property", "jcr:content/clemetier");
        paramMap.put("property.value", codeMetier);

        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);

        Query query = builder.createQuery(PredicateGroup.create(paramMap), resourceResolver.adaptTo(Session.class));

        return !query.getResult().getHits().isEmpty();
    }

}
