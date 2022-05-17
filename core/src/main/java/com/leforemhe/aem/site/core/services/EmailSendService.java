/*
* #%L
* ACS AEM Commons Bundle
* %%
* Copyright (C) 2013 Adobe
* %%
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* #L%
*/
package com.leforemhe.aem.site.core.services;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service to send Email
 */
@Component(
        immediate = true,
        service = EmailSendService.class
)
public final class EmailSendService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSendService.class);
    private static final String MSG_INVALID_RECIPIENTS = "Invalid Recipients";

    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private GlobalConfigService globalConfigService;

    @Reference
    private ResourceResolverFactory resolverFactory;

    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 30000;

    private int connectTimeout;
    private int soTimeout;

    @Activate
    protected void activate(Map<String, Object> config) {
        connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        soTimeout = DEFAULT_SOCKET_TIMEOUT;
    }

    public List<String> sendEmail(final String templatePath,
                                  final Map<String, String> emailParams,
                                  final String... recipients) {

        LOG.debug("Inside sendEmail 1");

        List<String> failureList = new ArrayList<String>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException(MSG_INVALID_RECIPIENTS);
        }

        List<InternetAddress> addresses = new ArrayList<InternetAddress>(recipients.length);
        for (String recipient : recipients) {
            try {
                addresses.add(new InternetAddress(recipient));
            } catch (AddressException e) {
                LOG.warn("Invalid email address {} passed to sendEmail(). Skipping.", recipient);
            }
        }
        LOG.debug("Addresses: {}", addresses);
        InternetAddress[] iAddressRecipients = addresses.toArray(new InternetAddress[addresses.size()]);
        List<InternetAddress> failureInternetAddresses = sendEmail(templatePath, emailParams, iAddressRecipients);

        for (InternetAddress address : failureInternetAddresses) {
            failureList.add(address.toString());
        }

        return failureList;
    }

    public List<InternetAddress> sendEmail(final String templatePath, final Map<String, String> emailParams,
                                           final InternetAddress... recipients) {

        LOG.debug("Inside sendEmail 2");

        List<InternetAddress> failureList = new ArrayList<InternetAddress>();

        if (recipients == null || recipients.length <= 0) {
            throw new IllegalArgumentException(MSG_INVALID_RECIPIENTS);
        }

        final MailTemplate mailTemplate = this.getMailTemplate(templatePath);
        final Class<? extends Email> mailType = this.getMailType(templatePath);
        final MessageGateway<Email> messageGateway = messageGatewayService.getGateway(mailType);

        LOG.debug("MailTemplate: {}", mailTemplate);
        LOG.debug("MailType: {}", mailType);

        for (final InternetAddress address : recipients) {
            try {
                // Get a new email per recipient to avoid duplicate attachments
                final Email email = getEmail(mailTemplate, mailType, emailParams);
                email.setTo(Collections.singleton(address));
                LOG.debug("Send email to {}", address);
                if (messageGateway == null) {
                    throw new Exception("messageGateway error");
                }
                messageGateway.send(email);
                LOG.debug("done");
            } catch (Exception e) {
                failureList.add(address);
                LOG.error("Error sending email to [ " + address + " ]", e);
            }
        }

        return failureList;
    }

    private Email getEmail(final MailTemplate mailTemplate,
                           final Class<? extends Email> mailType,
                           final Map<String, String> params) throws EmailException, MessagingException, IOException {

        LOG.debug("Inside getEmail");

        final Email email = mailTemplate.getEmail(StrLookup.mapLookup(params), mailType);

        if (params.containsKey(EmailServiceConstants.SENDER_EMAIL_ADDRESS)
                && params.containsKey(EmailServiceConstants.SENDER_NAME)) {
            email.setFrom(
                    params.get(EmailServiceConstants.SENDER_EMAIL_ADDRESS),
                    params.get(EmailServiceConstants.SENDER_NAME));
        } else if (params.containsKey(EmailServiceConstants.SENDER_EMAIL_ADDRESS)) {
            email.setFrom(params.get(EmailServiceConstants.SENDER_EMAIL_ADDRESS));
        }
        if (connectTimeout > 0) {
            email.setSocketConnectionTimeout(connectTimeout);
        }
        if (soTimeout > 0) {
            email.setSocketTimeout(soTimeout);
        }

        // #1008 setting the subject via the setSubject(..) parameter.
        if (params.containsKey(EmailServiceConstants.SUBJECT)) {
            email.setSubject(params.get(EmailServiceConstants.SUBJECT));
        }

        if (params.containsKey(EmailServiceConstants.BOUNCE_ADDRESS)) {
            email.setBounceAddress(params.get(EmailServiceConstants.BOUNCE_ADDRESS));
        }

        return email;
    }

    private Class<? extends Email> getMailType(String templatePath) {
        LOG.debug("Inside getMailType");
        return templatePath.endsWith(".html") ? HtmlEmail.class : SimpleEmail.class;
    }

    private MailTemplate getMailTemplate(String templatePath) throws IllegalArgumentException {
        LOG.debug("Inside getMailTemplate");

        MailTemplate mailTemplate = null;
        try (ResourceResolver resourceResolver = ServiceUtils.getResourceResolver(resolverFactory, globalConfigService.getConfig().systemUser())){
            if (resourceResolver == null) {
                throw new RepositoryException("ServiceUtils.getResourceResolver() method return null");
            }
            var session = resourceResolver.adaptTo(Session.class);
            mailTemplate = MailTemplate.create(templatePath, session);

            if (mailTemplate == null) {
                throw new IllegalArgumentException("Mail template path [ "
                        + templatePath + " ] could not resolve to a valid template. Check system user's permission!!!");
            }
        } catch (RepositoryException e) {
            LOG.error("Unable to obtain an administrative resource resolver to get the Mail Template at [ "
                    + templatePath + " ]", e);
        }

        return mailTemplate;
    }
}
