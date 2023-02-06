package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.ExperienceFragment;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LiensLinkModel {
    private static final String XF_TEMPLATE_PARENT = "/libs/cq/experience-fragments/components/experiencefragment/template";

    @Inject
    private ResourceResolver resourceResolver;
    @ValueMapValue
    String linkReference;

    public String getLinkReference() {
        return linkReference;
    }

    public boolean isParentExperienceFragment() {
        return !resourceResolver.getResource(linkReference + "/jcr:content").getValueMap().get("cq:template").equals(XF_TEMPLATE_PARENT);
    }
}
