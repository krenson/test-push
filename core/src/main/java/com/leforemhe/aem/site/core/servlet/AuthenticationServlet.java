/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.leforemhe.aem.site.core.servlet;

import com.leforemhe.aem.site.core.services.AuthenticationConfigService;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="leforemhe/components/site/servlet/authentication",
        methods= HttpConstants.METHOD_POST,
        selectors="leforemhe",
        extensions="json")
@ServiceDescription("Authentication Servlet")
public class AuthenticationServlet extends SlingAllMethodsServlet {
    public static final String AUTHORIZED_COOKIE_KEY = "authorized";
    public static final String USERNAME_KEY = "username";
    public static final String PASS_KEY = "password";
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServlet.class);

    @Reference
    private transient AuthenticationConfigService authService;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
        final SlingHttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter(USERNAME_KEY);
        String password = req.getParameter(PASS_KEY);
        LOG.debug("Login with username/password: {}/***", username);
        if(authService.authenticateUser(username, password)){
            LOG.debug("Authentication has been a success!");
            Cookie authorizedCookie = new Cookie(AUTHORIZED_COOKIE_KEY, "true");
            authorizedCookie.setHttpOnly(true);
            authorizedCookie.setSecure(req.isSecure());
            authorizedCookie.setPath("/");
            authorizedCookie.setMaxAge(60*60*24*7);
            resp.addCookie(authorizedCookie);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AUTHORIZED_COOKIE_KEY, true);
            resp.setStatus(200);
            resp.getWriter().write(jsonObject.toString());
        } else {
            LOG.debug("Authentication has been wrong!");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AUTHORIZED_COOKIE_KEY, false);
            resp.setStatus(401);
            resp.getWriter().write(jsonObject.toString());
        }
    }
}
