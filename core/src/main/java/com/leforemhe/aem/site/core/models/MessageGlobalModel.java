package com.leforemhe.aem.site.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sling Model of the Global Message Javascript
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MessageGlobalModel {

    @ValueMapValue
    private String message;
    @ValueMapValue
    private String dateStart;
    @ValueMapValue
    private String dateEnd;
    private LocalDateTime dateS;
    private LocalDateTime dateE;

    /**
     * Initiating the LocalDateTime fields.
     */
    @PostConstruct
    public void init() {
        this.dateS = LocalDateTime.parse(dateStart.substring(0,dateStart.indexOf('+')));
        this.dateE = LocalDateTime.parse(dateEnd.substring(0,dateEnd.indexOf('+')));
    }

    public String getMessage() {
        return message;
    }

    public String getStartYear() {
        return dateS.format(DateTimeFormatter.ofPattern("YYYY"));
    }

    public String getStartMonth() {
        return String.valueOf(Integer.parseInt(dateS.format(DateTimeFormatter.ofPattern("MM"))) - 1);
    }

    public String getStartDay() {
        return dateS.format(DateTimeFormatter.ofPattern("dd"));
    }

    public String getStartHour() {
        return dateS.format(DateTimeFormatter.ofPattern("HH"));
    }

    public String getStartMinute() {
        return dateS.format(DateTimeFormatter.ofPattern("mm"));
    }

    public String getStartSecond() {
        return dateS.format(DateTimeFormatter.ofPattern("ss"));
    }

    public String getEndYear() {
        return dateE.format(DateTimeFormatter.ofPattern("YYYY"));
    }

    public String getEndMonth() {
        return String.valueOf(Integer.parseInt(dateE.format(DateTimeFormatter.ofPattern("MM"))) - 1);
    }

    public String getEndDay() {
        return dateE.format(DateTimeFormatter.ofPattern("dd"));
    }

    public String getEndHour() {
        return dateE.format(DateTimeFormatter.ofPattern("HH"));
    }

    public String getEndMinute() {
        return dateE.format(DateTimeFormatter.ofPattern("mm"));
    }

    public String getEndSecond() {
        return dateE.format(DateTimeFormatter.ofPattern("ss"));
    }
}
