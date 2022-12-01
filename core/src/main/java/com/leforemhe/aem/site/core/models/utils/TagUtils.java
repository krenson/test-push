package com.leforemhe.aem.site.core.models.utils;

import com.day.cq.tagging.Tag;

import java.util.ArrayList;
import java.util.Iterator;

public class TagUtils {

    public static ArrayList<FilterModel> getParentAndChildTags(Iterator<Tag> parentTagIterator, Tag parentTag, ArrayList<FilterModel> tagsList) {
        ArrayList<AEMCheckbox> childTags = new ArrayList<>();
        while (parentTagIterator.hasNext()) {
            Tag childTag = parentTagIterator.next();
            childTags.add(new AEMCheckbox(childTag.getTitle(), childTag.getTagID()));
        }
        tagsList.add(new FilterModel(new AEMCheckbox(parentTag.getTitle(), parentTag.getTagID()), childTags));
        return tagsList;
    }
}
