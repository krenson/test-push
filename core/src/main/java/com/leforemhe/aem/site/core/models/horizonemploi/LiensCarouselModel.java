package com.leforemhe.aem.site.core.models.horizonemploi;

import com.leforemhe.aem.site.core.models.LiensLinkModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.inject.Named;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LiensCarouselModel {
    @ChildResource
    private List<LiensLinkModel> lienLinkReferences;

    public List<LiensLinkModel> getLienLinkReferences() {
        return lienLinkReferences;
    }
}
