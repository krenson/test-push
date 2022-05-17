package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Job Search Configuration
 * Configuration for job search
 */
@ObjectClassDefinition(name="leforemhe - Job Search Configuration",
        description = "Configuration for job search")
public @interface JobSearchConfig {

    String DEFAULTLINKJOBCOUNT = "https://leforemhe-acc.forem.be/HotJob/servlet/Admin.JobOffCount?_json_=true";
    String DEFAULTLINKSEARCH = "https://leforemhe.be/recherche-offres-emploi/jsp/index.jsp";

    /**
     * Link for fetching job count
     * @return Link for fetching job count
     */
    @AttributeDefinition(
            name = "Link for fetching job count",
            type = AttributeType.STRING
    )
    String jobSearchCountLink() default DEFAULTLINKJOBCOUNT;

    /**
     * Link to post job search request
     * @return Link to post job search request
     */
    @AttributeDefinition(
            name = "Link to post job search request",
            type = AttributeType.STRING
    )
    String jobSearchRequestPath() default DEFAULTLINKSEARCH;

}
