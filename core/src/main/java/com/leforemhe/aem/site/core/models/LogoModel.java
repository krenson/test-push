package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.services.RteHyperlinkService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

/**
 * Sling model for the logo component.
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LogoModel {
    @ValueMapValue(name = "fileReferenceLogo")
    private String fileLogo;

    @ValueMapValue(name = "link")
    private String link;

    @Inject
    private RteHyperlinkService hyperlinkService;

    public String getImageSrc() {
        return fileLogo;
    }

    public String getLink(){
        return hyperlinkService.getVanityPathIfNotAsset(link);
    }
}
