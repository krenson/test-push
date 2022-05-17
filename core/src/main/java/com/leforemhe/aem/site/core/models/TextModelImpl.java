package com.leforemhe.aem.site.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import com.adobe.cq.wcm.style.ComponentStyleInfo;
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
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {TextModel.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = TextModelImpl.RESOURCE_TYPE)
public class TextModelImpl implements TextModel {
    public static final String RESOURCE_TYPE = "leforemhe/components/site/text";

    @Self
    private SlingHttpServletRequest request;

    @Self
    @Via(type = ResourceSuperType.class)
    private Text text;

    /**
     * The current style for this component.
     */
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @JsonIgnore
    @Nullable
    protected Style currentStyle;

    @Override
    public String getBackgroundCssClass() {
        return this.request.getResource().adaptTo(ComponentStyleInfo.class).getAppliedCssClasses();
    }

    @Override
    public String getText() {
        return this.text.getText();
    }

    @Override
    public boolean isRichText() {
        return this.text.isRichText();
    }
}
