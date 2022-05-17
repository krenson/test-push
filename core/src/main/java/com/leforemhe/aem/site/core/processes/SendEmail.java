package com.leforemhe.aem.site.core.processes;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leforemhe.aem.site.core.services.*;
import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

import javax.servlet.ServletException;

/**
 * Workflow process to send an template email
 */
@Component(
        service = WorkflowProcess.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=" + "Process to send a template email",
                "process.label" + "=" + "Send an email based on a template using the EmailSendService"
        }
)
public class SendEmail implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(SendEmail.class);

    /**
     * Service used to send the email
     */
    @Reference
    private EmailSendService emailService;

    @Reference
    private GlobalConfigService globalConfigService;

    @Reference
    private ResourceResolverService resourceResolverService;

    /**
     * The available arguments to this process implementation.
     */
    protected enum Arguments {
        PROCESS_ARGS("PROCESS_ARGS"),
        /**
         * emailTemplate - process argument
         */
        TEMPLATE("emailTemplate"),
        /**
         * sendTo - process argument
         */
        SEND_TO("sendTo"),

        /**
         * dateFormat - process argument
         */
        DATE_FORMAT("dateFormat");

        private String argumentName;

        Arguments(String argumentName) {
            this.argumentName = argumentName;
        }

        public String getArgumentName() {
            return this.argumentName;
        }

    }

    @Override
    public final void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaData)
            throws WorkflowException {

        LOG.debug("Inside execute");

        final WorkflowData workflowData = workItem.getWorkflowData();
        final String type = workflowData.getPayloadType();

        // Check if the payload is a path in the JCR
        if (!StringUtils.equals(type, "JCR_PATH")) {
            return;
        }

        String[] args = buildArguments(metaData);

        // process arguments
        String emailTemplate = getValueFromArgs(Arguments.TEMPLATE.getArgumentName(), args);
        LOG.debug("emailTemplate {}", emailTemplate);

        if (emailTemplate == null) {
            LOG.warn("Invalid process arguments, returning");
            return;
        }

        // set date format to be used in emails
        String sdfParam = getValueFromArgs(Arguments.DATE_FORMAT.getArgumentName(), args);
        SimpleDateFormat sdf = getSimpleDateFormat(sdfParam);

        // Get the path to the JCR resource from the payload
        final String payloadPath = workflowData.getPayload().toString();

        try (ResourceResolver resourceResolver = resourceResolverService.getResourceResolverFromSession(workflowSession.getSession())) {
            if (resourceResolver != null) {
                Resource payloadRes = resourceResolver.getResource(payloadPath);

                if (payloadRes == null) {
                    throw new ServletException("resourceResolver.getResource(payloadPath) is null");
                }
                LOG.debug("payloadRes: {}", payloadRes);

                // Email Parameter map
                Map<String, String> emailParams = new HashMap<String, String>();

                // Set jcr path
                emailParams.put(EmailServiceConstants.JCR_PATH, payloadPath);

                // Get Payload params
                Map<String, String> payloadProp = ServiceUtils.getPayloadProperties(payloadRes, sdf);
                if (payloadProp != null) {
                    emailParams.putAll(payloadProp);
                }

                // Get Url params
                Map<String, String> urlParams = getUrls(payloadRes);
                emailParams.putAll(urlParams);

                // Get Additional Parameters to add
                Map<String, String> wfParams = getAdditionalParams(workItem, workflowSession, payloadRes);
                emailParams.putAll(wfParams);
                LOG.debug("emailParams {}", emailParams);

                // get email addresses based on CQ user or group
                String[] emailTo = getEmailAddrs(workItem, payloadRes, args);
                LOG.debug("emailTo {}", emailTo);

                List<String> failureList = emailService.sendEmail(emailTemplate, emailParams, emailTo);

                if (failureList.isEmpty()) {
                    LOG.info("Email sent successfully to {} recipients", emailTo.length);
                } else {
                    LOG.error("Email sent failed");
                }
            }
        } catch (ServletException e) {
            LOG.error("Could not acquire a ResourceResolver object from the Workflow Session's JCR Session: {}", e);
        }
    }

    /***
     * Gets a String[] of email addresses to send the email to. By default calls
     * {@link com.leforemhe.aem.site.core.processes.email.ServiceUtils#getEmailAddrsFromUserPath(ResourceResolver, String)}
     * Protected so that it can be overridden by implementing classes to add
     * unique logic to where emails are routed to.
     *
     * @param workItem
     *            the current WorkItem in the workflow
     * @param payloadResource
     *            the current payload as a Resource
     * @param args
     *            process arguments configured by the workflow step
     * @return String[] of email addresses
     */
    protected String[] getEmailAddrs(WorkItem workItem, Resource payloadResource, String[] args) {
        ResourceResolver resolver = payloadResource.getResourceResolver();
        String sendToUser = getValueFromArgs(Arguments.SEND_TO.getArgumentName(), args);
        return ServiceUtils.getEmailAddrsFromPathOrName(resolver, sendToUser);
    }

    /***
     * Returns a Map<String, String> of additional parameters that will be added
     * to the full list of email parameters that is sent to the EmailService. By
     * default adds the Workflow Title:
     * {@link com.leforemhe.aem.site.core.processes.email.EmailServiceConstants#WF_MODEL_TITLE
     * WF_MODEL_TITLE} and adds the Workflow Step Title:
     * {@link com.leforemhe.aem.site.core.processes.email.EmailServiceConstants#WF_STEP_TITLE
     * WF_STEP_TITLE}
     * {@link com.leforemhe.aem.site.core.processes.email.EmailServiceConstants#WF_INITIATOR
     * WF_INITIATOR} Protected so that implementing classes can override and
     * add additional parameters.
     *
     * @param workItem
     * @param workflowSession
     * @param payloadResource
     * @return Map<String, String> of additional parameters to be added to email
     *         params
     */
    protected Map<String, String> getAdditionalParams(WorkItem workItem, WorkflowSession workflowSession,
            Resource payloadResource) {
        Map<String, String> wfParams = new HashMap<String, String>();

        try {
            wfParams.put(EmailServiceConstants.WF_STEP_TITLE, workItem.getNode().getTitle());
            wfParams.put(EmailServiceConstants.WF_MODEL_TITLE, workItem.getWorkflow().getWorkflowModel()
                    .getTitle());
            // Set workflow initiator
            wfParams.put(EmailServiceConstants.WF_INITIATOR, workItem.getWorkflow().getInitiator());
            
            if(workItem.getMetaDataMap().containsKey("comment")) {
                wfParams.put(EmailServiceConstants.WF_STEP_COMMENT, workItem.getMetaDataMap().get("comment").toString());
            }
        } catch (Exception e) {
            LOG.warn("Error getting workflow title and workflow step title {}", e);
        }

        return wfParams;
    }

    /***
     * Gets value from workflow process arguments
     *
     * @param key
     * @param arguments
     * @return String of the argument value or null if not found
     */
    protected String getValueFromArgs(String key, String[] arguments) {
        for (String str : arguments) {
            String trimmedStr = str.trim();
            if (trimmedStr.startsWith(key + ":")) {
                return trimmedStr.substring((key + ":").length());
            }
        }
        return null;
    }

    /***
     * Uses the AuthorUIHelper to generate links to the payload on author Uses
     * Externalizer to generate links to the payload on publish
     *
     * @param payloadRes
     * @return
     */
    private Map<String, String> getUrls(Resource payloadRes) {

        Map<String, String> urlParams = new HashMap<String, String>();
        if (payloadRes == null) {
            return urlParams;
        }

        String payloadPath = payloadRes.getPath();
        ResourceResolver resolver = payloadRes.getResourceResolver();

        // add absolute author url
        urlParams.put(EmailServiceConstants.AUTHOR_LINK, globalConfigService.getConfig().authorServerURI());

        // add publish url
        urlParams.put(EmailServiceConstants.PUBLISH_LINK, globalConfigService.getConfig().publicServerURI());
        LOG.debug("urlParams {}", urlParams);

        return urlParams;
    }

    /***
     *
     * @param metaData
     * @return
     */
    private String[] buildArguments(MetaDataMap metaData) {
        // the 'old' way, ensures backward compatibility
        String processArgs = metaData.get(Arguments.PROCESS_ARGS.getArgumentName(), String.class);
        if (processArgs != null && !processArgs.equals("")) {
            return processArgs.split(",");
        } else {
            return new String[0];
        }
    }

    /***
     * Set the format to be used for displaying dates in the email Defaults to
     * format of 'yyyy-MM-dd hh:mm a'
     *
     * @param formatString
     *            - workflow process argument to override default format
     * @return SimpleDateFormat that will be used to convert jcr Date properties
     *         to Strings
     */
    private SimpleDateFormat getSimpleDateFormat(String formatString) {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        if (formatString == null || formatString.isEmpty()) {
            return defaultFormat;
        }

        try {
            return new SimpleDateFormat(formatString);
        } catch (IllegalArgumentException e) {
            // invalid pattern
            return defaultFormat;
        }
    }
}
