package com.leforemhe.aem.site.core.models.horizonemploi;

import com.day.cq.wcm.api.Page;
import com.leforemhe.aem.site.core.models.Constants;
import com.leforemhe.aem.site.core.models.utils.DateUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.GregorianCalendar;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LastUpdatedDateImpl {

    @Inject
    private Page currentPage;

    @ValueMapValue
    private String dateLabel;

    private String date;

    public String getDateLabel() {
        return dateLabel;
    }

    public String getDate(){
        GregorianCalendar date = (GregorianCalendar)currentPage.getProperties().get(Constants.LAST_UPDATED);
        return DateUtils.getDateFormat(date);
    }
}
