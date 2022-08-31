package com.leforemhe.aem.site.core.models.utils;

import org.apache.commons.lang3.ObjectUtils;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;

public class ContentFragmentUtils {

    public static <T> T getSingleValue(ContentFragment contentFragment, String propertyName, Class<T> type) {
        if (ObjectUtils.isNotEmpty(contentFragment)) {
            if (contentFragment.hasElement(propertyName)) {
                FragmentData dataElement = contentFragment.getElement(propertyName).getValue();
                if (dataElement.isTypeSupported(type)) {
                    return dataElement.getValue(type);
                }
            }
        }
        return null;
    }

    public static <T> T[] getMultifieldValue(ContentFragment contentFragment, String propertyName, Class<T> type) {
        if (ObjectUtils.isNotEmpty(contentFragment)) {
            if (contentFragment.hasElement(propertyName)) {
                FragmentData dataElement = contentFragment.getElement(propertyName).getValue();
                if (dataElement.isTypeSupported(type) && dataElement.getDataType().isMultiValue()) {
                    return (T[]) dataElement.getValue();
                }
            }
        }
        return null;
    }

    public static String getElementTitle(ContentFragment contentFragment, String propertyName) {
        if (ObjectUtils.isNotEmpty(contentFragment)) {
            if (contentFragment.hasElement(propertyName)) {
                ContentElement element = contentFragment.getElement(propertyName);
                return element.getTitle();
            }
        }
        return null;
    }

}
