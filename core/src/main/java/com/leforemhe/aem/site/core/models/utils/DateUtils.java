package com.leforemhe.aem.site.core.models.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class DateUtils {

    public static String getDateFormat(GregorianCalendar calendar) {
        if(calendar != null) {
            SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
            fmt.setCalendar(calendar);
            return fmt.format(calendar.getTime());
        }
        return StringUtils.EMPTY;
    }
}
