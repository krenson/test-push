package com.leforemhe.aem.site.core.models.horizonemploi.impl;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Button;
import com.leforemhe.aem.site.core.models.horizonemploi.ButtonModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = ButtonModel.class,
        resourceType = "leforemhe/components/site/banniere/banniere-liens",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ButtonModelImpl implements ButtonModel {
    @Self
    @Via(type = ResourceSuperType.class)
    private Button button;

    @ValueMapValue
    private String link;

    @Override
    public String getText() {
        return button.getText();
    }

    @Override
    public String getUrlLink() {
        return link;
    }
}
