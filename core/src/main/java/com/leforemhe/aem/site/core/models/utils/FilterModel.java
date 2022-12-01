package com.leforemhe.aem.site.core.models.utils;

import java.util.ArrayList;

public class FilterModel {
    AEMCheckbox parentTag;
    ArrayList<AEMCheckbox> childTags;

    public FilterModel(AEMCheckbox parentTag, ArrayList<AEMCheckbox> childTags) {
        this.parentTag = parentTag;
        this.childTags = childTags;
    }

    public AEMCheckbox getParentTag() {
        return parentTag;
    }

    public ArrayList<AEMCheckbox> getChildTags() {
        return childTags;
    }
}
