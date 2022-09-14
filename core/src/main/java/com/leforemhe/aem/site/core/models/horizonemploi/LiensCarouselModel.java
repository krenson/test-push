package com.leforemhe.aem.site.core.models.horizonemploi;

import com.leforemhe.aem.site.core.models.LiensLinkModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface LiensCarouselModel {
    @ChildResource
    List<LiensLinkModel> getLienLinkReferences();
}
