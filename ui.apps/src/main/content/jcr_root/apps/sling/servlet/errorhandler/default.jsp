<%--
  Copyright 1997-2009 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Default error handler

--%><%@page session="false" pageEncoding="utf-8"
            import="com.day.cq.wcm.api.WCMMode,
                    java.io.PrintWriter,
                    org.apache.sling.api.SlingConstants,
                    org.apache.sling.api.request.RequestProgressTracker,
                    org.apache.sling.api.request.ResponseUtil,
                    org.apache.commons.lang3.StringEscapeUtils" %><%
%><%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects /><%
%><%

    String message = (String) request.getAttribute("javax.servlet.error.message");
    Integer scObject = (Integer) request.getAttribute("javax.servlet.error.status_code");
    boolean isAuthorMode = true; // WCMMode.fromRequest(request) != WCMMode.DISABLED && !sling.getService(SlingSettingsService.class).getRunModes().contains("publish");

    int statusCode = (scObject != null)
            ? scObject.intValue()
            : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    if (message == null) {
        message = statusToString(statusCode);
    }

    response.setStatus(statusCode);
    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");

%><!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>
    <link href="/etc.clientlibs/leforemhe/clientlibs/clientlib-dependencies.css" rel="stylesheet">
    <title><%= statusCode %> <%= StringEscapeUtils.escapeHtml4(message) %></title>
    <style>
        body {
            font-family: "robotoregular";
            font-size: 18px;
        }

        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 30px 0;
            padding: 0 10%;
        }

        .circle {
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            border: 15px solid #B8B8B8;
            border-radius: 50%;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -ms-flex-direction: column;
            flex-direction: column;
            height: 160px;
            padding: 50px 40px 30px 40px;
            width: 160px;
        }

        .eyes {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-orient: horizontal;
            -webkit-box-direction: normal;
            -ms-flex-direction: row;
            flex-direction: row;
            -webkit-box-pack: justify;
            -ms-flex-pack: justify;
            justify-content: space-between;
            margin-bottom: 50px;
        }

        .eye {
            border: 15px solid #B8B8B8;
            border-radius: 50%;
            height: 30px;
            margin: 0 20px 0 20px;
            width: 30px;
        }

        .mouth {
            border: 7px solid #B8B8B8;
            border-radius: 7px;
            height: 0;
            -webkit-transform: rotate(-22deg);
            -ms-transform: rotate(-22deg);
            transform: rotate(-22deg);
            width: 95px;
        }

        .Page_indisponible {
            font-size: 2.667rem;
            color: rgb(147, 170, 79);
            font-weight: bold;
        }

        p {
            font-size: 1rem;
            color: rgb(44, 62, 80);
        }

        a {
            color: rgb(136, 157, 73);
            text-decoration: underline;
        }

        a:hover {text-decoration: none;}

        .bouton {
            background-color: #93aa4f;
            border-radius: 4px;
            display: inline-flex;
            align-items: center;
            font-size: 1.21rem;
            color: rgb(255, 255, 255);
            font-weight: bold;
            margin: 35px 0;
            padding: 15px 20px;
            text-decoration: none;
            text-transform: uppercase;
        }

        .bouton:hover {
            -webkit-box-shadow: 0 2px 3px #333;
            box-shadow: 0 2px 3px #333;
        }

        .flecheBlanche {
            border: 4px solid #FFF;
            border-bottom: 0;
            border-left: 0;
            height: 10px;
            margin-left: 20px;
            transform: rotate(45deg);
            width: 10px;
        }

        .socialNetwork {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-pack: start;
            -ms-flex-pack: start;
            justify-content: flex-start;
            margin: 22px 0;
            padding: 0;
        }

        .socialNetwork li {
            list-style-type: none;
            margin-right: 30px;
        }

    </style>
</head>
<body>
<div class="container">
    <div>
        <div class="circle">
            <div class="eyes">
                <div class="eye"></div>
                <div class="eye"></div>
            </div>
            <div class="mouth"></div>
        </div>
    </div>
    <div>
        <p class="Page_indisponible"><%= StringEscapeUtils.escapeHtml4(message) %></p>
        <a class="bouton" href="/">accueil du site<div class="flecheBlanche"></div></a>
        <div>
            Retrouvez-nous sur les réseaux sociaux&nbsp;!<br>
            <ul class="socialNetwork">
                <li><a href="https://www.facebook.com/leforemhe/" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/facebook-logo.png"></a></li>
                <li><a href="https://twitter.com/leforemhe" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/twitter-social-logotype.png"></a></li>
                <li><a href="https://www.youtube.com/user/Videosleforemhe/" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/youtube-logotype.png"></a></li>
                <li><a href="https://www.linkedin.com/company/le-forem" target="_blank"><img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/socialmedia/linkedin-logo.png"></a></li>
            </ul>

        </div>

    </div>
</div>

<div id="eid-chrome-extension-is-installed"></div></body>
<%
    if (isAuthorMode) {
        // write the exception message
        final PrintWriter escapingWriter = new PrintWriter(ResponseUtil.getXmlEscapingWriter(out));

        // dump the stack trace
        if (request.getAttribute(SlingConstants.ERROR_EXCEPTION) instanceof Throwable) {
            Throwable throwable = (Throwable) request.getAttribute(SlingConstants.ERROR_EXCEPTION);
            out.println("<h3>Exception:</h3>");
            out.println("<pre>");
            out.flush();
            printStackTrace(escapingWriter, throwable);
            escapingWriter.flush();
            out.println("</pre>");
        }

        // dump the request progress tracker
        if (slingRequest != null) {
            RequestProgressTracker tracker = slingRequest.getRequestProgressTracker();
            out.println("<h3>Request Progress:</h3>");
            out.println("<pre>");
            out.flush();
            tracker.dump(escapingWriter);
            escapingWriter.flush();
            out.println("</pre>");
        }
    }
%>

<hr>
<address><%= StringEscapeUtils.escapeHtml4(this.getServletConfig().getServletContext().getServerInfo()) %></address>
</body>
</html><%!

    /**
     * Print the stack trace for the root exception if the throwable is a
     * {@link ServletException}. If this does not contain an exception,
     * the throwable itself is printed.
     */
    private void printStackTrace(PrintWriter pw, Throwable t) {
        // nothing to do, if there is no exception
        if (t == null) {
            return;
        }

        // unpack a servlet exception
        if (t instanceof ServletException) {
            ServletException se = (ServletException) t;
            while (se.getRootCause() != null) {
                t = se.getRootCause();
                if (t instanceof ServletException) {
                    se = (ServletException) t;
                } else {
                    break;
                }
            }
        }

        // dump stack, including causes
        t.printStackTrace(pw);
    }

    public static String statusToString(int statusCode) {
        switch (statusCode) {
            case 100:
                return "Continue";
            case 101:
                return "Switching Protocols";
            case 102:
                return "Processing (WebDAV)";
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 202:
                return "Accepted";
            case 203:
                return "Non-Authoritative Information";
            case 204:
                return "No Content";
            case 205:
                return "Reset Content";
            case 206:
                return "Partial Content";
            case 207:
                return "Multi-Status (WebDAV)";
            case 300:
                return "Multiple Choices";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Found";
            case 303:
                return "See Other";
            case 304:
                return "Not Modified";
            case 305:
                return "Use Proxy";
            case 307:
                return "Temporary Redirect";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 402:
                return "Payment Required";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 406:
                return "Not Acceptable";
            case 407:
                return "Proxy Authentication Required";
            case 408:
                return "Request Time-out";
            case 409:
                return "Conflict";
            case 410:
                return "Gone";
            case 411:
                return "Length Required";
            case 412:
                return "Precondition Failed";
            case 413:
                return "Request Entity Too Large";
            case 414:
                return "Request-URI Too Large";
            case 415:
                return "Unsupported Media Type";
            case 416:
                return "Requested range not satisfiable";
            case 417:
                return "Expectation Failed";
            case 422:
                return "Unprocessable Entity (WebDAV)";
            case 423:
                return "Locked (WebDAV)";
            case 424:
                return "Failed Dependency (WebDAV)";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Service Unavailable";
            case 504:
                return "Gateway Time-out";
            case 505:
                return "HTTP Version not supported";
            case 507:
                return "Insufficient Storage (WebDAV)";
            case 510:
                return "Not Extended";
            default:
                return String.valueOf(statusCode);
        }
    }
%>
