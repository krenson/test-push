package com.leforemhe.aem.site.core.models;

import java.util.Collection;
import java.util.Collections;

import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

/**
 * Sling model of the Reference Multi Combo Component
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class, List.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ReferenceMultiComboModel {

    @ValueMapValue(name = JcrConstants.JCR_TITLE)
    private String title;
    @ValueMapValue
    private String description;

    @Self @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.List referencesList;

    /**
     * Constructor initiating the list of reference
     * @param referencesList List of reference
     */
    @SuppressWarnings("WeakerAccess")
    public ReferenceMultiComboModel(List referencesList) {
        this.referencesList = referencesList;
    }

    public ReferenceMultiComboModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return referencesList.getId();
    }

    public Collection<ListItem> getReferences() {
        return referencesList!=null ?
                referencesList.getListItems() :
                Collections.emptyList();
    }
}
