package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.config.JobSearchConfig;
import com.leforemhe.aem.site.core.services.JobSearchConfigService;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ForModel(
        models = {RechercheEmploiModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/RechercheEmploiTest.json"
)
@SuppressWarnings("WeakerAccess")
public class RechercheEmploiModelTests extends AbstractModelTest {

    private static final String JOB_SEARCH_COUNT_LINK = "https://leforemhe-dev.forem.be/HotJob/servlet/Admin.JobOffCount?_json_=true";
    private static final String JOB_SEARCH_REQUEST_PATH = "https://leforemhe.be/recherche-offres-emploi/jsp/index.jsp";

    @Mock
    JobSearchConfigService jobSearchConfigService;
    @Mock
    JobSearchConfig jobSearchConfig;

    @Override
    public void setup() {
        context.registerService(jobSearchConfigService);
        lenient().when(jobSearchConfigService.getConfig()).thenReturn(jobSearchConfig);
        lenient().when(jobSearchConfig.jobSearchCountLink()).thenReturn(JOB_SEARCH_COUNT_LINK);
        lenient().when(jobSearchConfig.jobSearchRequestPath()).thenReturn(JOB_SEARCH_REQUEST_PATH);
    }

    @Test
    public void getHelpUrl() {
        String valueExpected = "/content/demo-page/help";
        context.currentResource("/content/demo-page/jcr:content/rechercheEmploi");
        Resource resource = context.request().getResource();
        RechercheEmploiModel emploiModel = resource.adaptTo(RechercheEmploiModel.class);
        assertNotNull(emploiModel);
        assertEquals(valueExpected, emploiModel.getHelpUrl());
        assertEquals(JOB_SEARCH_COUNT_LINK, emploiModel.getJobCountUrl());
        assertEquals(JOB_SEARCH_REQUEST_PATH, emploiModel.getJobSearchRequestPath());
    }

}
