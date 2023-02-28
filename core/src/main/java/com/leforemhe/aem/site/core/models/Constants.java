package com.leforemhe.aem.site.core.models;

import com.adobe.cq.dam.cfm.ContentFragment;

import java.util.List;

public class Constants {
    public static final String EF_RESOURCE_TYPE = "leforemhe/components/commons/core/xfpage";
    public static final String CLE_METIER = "clemetier";
    public static final String CLE_METIER_PROPERTY_CF = "jcr:content/clemetier";
    public static final String CONTENT_ROOT_PATH = "/content/leforemhe";
    public static final String LAST_UPDATED = "lastUpdatedDate";
    public static final String UPDATE = "update";

    public static final String CQ_PAGE = "cq:Page";
    public static final String CQ_PAGECONTENT = "cq:PageContent";
    public static final String CQ_TAGS = "cq:tags";
    public static final String SLING_VANITY_PATH = "sling:vanityPath";
    public static final String DAM_ASSET = "dam:Asset";
    public static final String DC_FORMAT = "dc:format";

    public static final String IMAGE_RENDITION_MARKER = "vig";
    public static final String FALLBACK_RENDITION = "/jcr:content/renditions/cq5dam.web.1280.1280.jpeg";

    public static final String JOB_PAGE_RESOURCE_TYPE = "leforemhe/components/site/jobpage";

    public static final String JSON_EXTENSION = ".json";
    public static final String APPLICATION_JSON = "application/json";

    public static final List<String> ALLOWED_NAMESPACES = List.of("he-metier");
    public static final String CURRENT_JOB_REQUEST = "currentJobRequest";

}
