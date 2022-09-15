package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sling model of Secure Component
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SecureModel {
    @Inject
    private Page currentPage;

    @ValueMapValue
    @Default(booleanValues = false)
    private Boolean secured;

    public List<String> getRoles(){
        if(currentPage.getProperties().containsKey("roles")){
            return Arrays.stream(currentPage.getProperties().get("roles", String[].class))
                    .map(this::convertRole)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public List<String> getSecurityRoles(){
        if(currentPage.getProperties().containsKey("rolesSecurity")){
            return Arrays.stream(currentPage.getProperties().get("rolesSecurity", String[].class))
                    .map(this::convertRole)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Boolean getSecured() {
        return secured;
    }

    private String convertRole(String role){
        return "'" + role + "'";
    }
}
