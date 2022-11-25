package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import com.adobe.cq.wcm.core.components.models.Title;
import com.adobe.cq.wcm.style.ComponentStyleInfo;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {TitleModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = TitleModelImpl.RESOURCE_TYPE)
public class TitleModelImpl implements TitleModel {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/title";

    @Self
    @Via(type = ResourceSuperType.class)
    private Title title;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL, name = JcrConstants.JCR_TITLE)
    @Nullable
    private String titleField;

    @Override
    public String getText() {
        return titleField;
    }

    @Override
    public String getType() {
        return title.getType();
    }
}
