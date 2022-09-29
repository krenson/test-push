package com.leforemhe.aem.site.core.models.utils;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagConstants;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.cfmodels.Job;
import com.leforemhe.aem.site.core.models.cfmodels.JobTag;
import com.leforemhe.aem.site.core.services.ContentFragmentUtilService;
import org.apache.commons.lang3.StringUtils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;

public class EventUtils {

    public static String getEventNodeType(Event event) {
        try {
            return event.getInfo().get(JcrConstants.JCR_PRIMARYTYPE).toString();
        } catch (RepositoryException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public static void handleJobPageEvent(Node pageNode, ContentFragmentUtilService contentFragmentUtilService) {
        try {
            String jobID = pageNode.getProperty(Constants.CLE_METIER).getValue().toString();
            if (!jobID.isEmpty()) {
                Job job = contentFragmentUtilService.getJobFromJobID(jobID);
                if (job != null) {
                    pageNode.setProperty(JcrConstants.JCR_TITLE, job.getTitle());
                    pageNode.setProperty(JcrConstants.JCR_DESCRIPTION, job.getDescription());
                    pageNode.setProperty(Constants.CQ_TAGS, job.getTagIds());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
