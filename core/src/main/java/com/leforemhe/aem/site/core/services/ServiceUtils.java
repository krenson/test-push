package com.leforemhe.aem.site.core.services;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility methods for the services.
 */
@SuppressWarnings("WeakerAccess")
public class ServiceUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtils.class);

    private static final String PN_USER_EMAIL = "profile/email";

    /**
     * Private constructor of Service Utils. This class it not meant to be instantiated.
     */
    private ServiceUtils() {
    }

    /**
     * Get a resource resolver.
     *
     * @return resolver factory.
     */
    public static ResourceResolver getResourceResolver(ResourceResolverFactory resolverFactory, String systemUser) {
        LOG.debug("Inside getResourceResolver");
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, systemUser);
        try {
            LOG.debug("Get service resource resolver factory by {}", param);
            return resolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            LOG.error("Unable to get ResourceResolver: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get component path
     *
     * @return component path
     */
    public static String getComponentPath(String propertyPath){
        LOG.debug("Inside getComponentPath");
        return propertyPath.substring(0, propertyPath.lastIndexOf('/'));
    }

    /***
     * Tests whether the payload is a DAM asset or a cq:Page for DAM asset
     * returns all properties at the metadata node for DAM assets for cq:Page
     * returns all properties at the jcr:content node The Map<String, String>
     * that is returned contains string representations of each of the
     * respective properties
     *
     * @param payloadRes
     *            the payload as a resource
     * @param sdf
     *            used by the method to transform Date properties into Strings
     * @return Map<String, String> String representation of jcr properties
     */
    public static final Map<String, String> getPayloadProperties(Resource payloadRes, SimpleDateFormat sdf) {

        Map<String, String> emailParams = new HashMap<String, String>();

        if (payloadRes == null) {
            return emailParams;
        }

        // check if the payload is a page
        Page payloadPage = payloadRes.adaptTo(Page.class);

        if (payloadPage != null) {
            Map<String, String> pageContent = getJcrKeyValuePairs(payloadPage.getContentResource(), sdf);
            emailParams.putAll(pageContent);
        }

        return emailParams;
    }

    /**
     * Gets email(s) based on the path to a principal or principle name.
     * If it points to a user an array with a single email is returned,
     * else an array of emails for each individual in the group
     *
     * @param resourceResolver
     * @param principleOrPath  name of a user or group or the path to such
     * @return String[] of email(s) associated with account
     */
    public static final String[] getEmailAddrsFromPathOrName(ResourceResolver resourceResolver, String principleOrPath) {
        if (StringUtils.startsWith(principleOrPath, "/")) {
            return getEmailAddrsFromUserPath(resourceResolver, principleOrPath);
        }

        try {
            UserManager userManager = resourceResolver.adaptTo(UserManager.class);
            Authorizable auth = userManager.getAuthorizable(principleOrPath);
            return getEmailAddrsFromUserPath(resourceResolver, auth.getPath());
        } catch (RepositoryException e) {
            LOG.warn("Could not load repository paths for users. {}", e);
        }
        return new String[]{};
    }

    /**
     * Gets email(s) based on the path to a principal If the path is a user it
     * returns an array with a single email if the path is a group returns an
     * array emails for each individual in the group
     *
     * @param resourceResolver
     * @param principlePath    path to a CQ user or group
     * @return String[] of email(s) associated with account
     */
    @SuppressWarnings({"squid:S3776"})
    private static final String[] getEmailAddrsFromUserPath(ResourceResolver resourceResolver, String principlePath) {
        List<String> emailList = new LinkedList<String>();

        try {
            Resource authRes = resourceResolver.getResource(principlePath);

            if (authRes != null) {
                Authorizable authorizable = authRes.adaptTo(Authorizable.class);
                if (authorizable != null) {
                    // check if it is a group
                    if (authorizable.isGroup()) {
                        Group authGroup = authRes.adaptTo(Group.class);

                        // iterate over members of the group and add emails
                        Iterator<Authorizable> memberIt = authGroup.getMembers();
                        while (memberIt.hasNext()) {
                            String currEmail = getAuthorizableEmail(memberIt.next());
                            if (currEmail != null) {
                                emailList.add(currEmail);
                            }
                        }
                    } else {
                        // otherwise is an individual user
                        String authEmail = getAuthorizableEmail(authorizable);
                        if (authEmail != null) {
                            emailList.add(authEmail);
                        }
                    }
                }
            }
        } catch (RepositoryException e) {
            LOG.warn("Could not get list of email(s) for users. {}", e);
        }
        String[] emailReturn = new String[emailList.size()];
        return emailList.toArray(emailReturn);
    }

    /***
     * Method to add all properties of a resource to Key/Value map of strings
     * only converts dates to string format based on simple date format
     * concatenates String[] into a string of comma separated items all other
     * values uses toString
     *
     * @param resource
     * @return a string map where the key is the jcr property and the value is
     *
     */
    private static Map<String, String> getJcrKeyValuePairs(Resource resource, SimpleDateFormat sdf) {

        Map<String, String> returnMap = new HashMap<String, String>();

        if (resource == null) {
            return returnMap;
        }

        ValueMap resMap = resource.getValueMap();

        for (Map.Entry<String, Object> entry : resMap.entrySet()) {

            Object value = entry.getValue();

            if (value instanceof Calendar) {
                // Date property
                String fmtDate = formatDate((Calendar) value, sdf);
                returnMap.put(entry.getKey(), fmtDate);
            } else if (value instanceof String[]) {
                // concatenate string array
                String strValue = StringUtils.join((String[]) value, ", ");
                returnMap.put(entry.getKey(), strValue);

            } else {
                // all other properties just use default to string
                returnMap.put(entry.getKey(), value.toString());
            }
        }

        return returnMap;
    }

    private static String getAuthorizableEmail(Authorizable authorizable) throws RepositoryException {
        if (authorizable.hasProperty(PN_USER_EMAIL)) {
            Value[] emailVal = authorizable.getProperty(PN_USER_EMAIL);
            return emailVal[0].getString();
        }

        return null;
    }

    /***
     * Format date as a string using global variable sdf
     *
     * @param calendar
     * @return
     */
    private static String formatDate(Calendar calendar, SimpleDateFormat sdf) {

        return sdf.format(calendar.getTime());
    }
}
