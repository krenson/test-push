package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.config.GoogleServicesConfig;
import com.leforemhe.aem.site.core.services.GoogleServicesConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith({MockitoExtension.class})
@SuppressWarnings("WeakerAccess")
public class ResultatRechercheModelTest {
    @Mock
    private GoogleServicesConfigService googleServicesConfigServiceMock;
    @Mock
    private GoogleServicesConfig googleServicesConfigMock;

    @BeforeEach
    public void setUp(){
        lenient().when(googleServicesConfigServiceMock.getConfig()).thenReturn(googleServicesConfigMock);
    }

    @Test
    public void getGCSEKeyTest(){
        ResultatRechercheModel resultatRechercheModel = new ResultatRechercheModel(googleServicesConfigServiceMock);
        assertEquals(resultatRechercheModel.getGCSEKey(), googleServicesConfigMock.googleCustomSearchEngineKey());
        assertEquals(resultatRechercheModel.getGCSEKeyParameterName(), googleServicesConfigMock.googleCustomSearchEngineKeyParameterName());
        assertEquals(resultatRechercheModel.getGCSESource(), googleServicesConfigMock.googleCustomSearchEngineSource());
    }
}
