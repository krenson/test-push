package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.services.GoogleServicesConfigService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Sling model of the Resultat Rechercher Component
 */
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ResultatRechercheModel {
    private final GoogleServicesConfigService googleServicesConfigService;

    /**
     * Constructor injecting the Google Services Configuration Service
     * @param googleServicesConfigService Google Services Configuration Service
     */
    @Inject
    public ResultatRechercheModel(GoogleServicesConfigService googleServicesConfigService) {
        this.googleServicesConfigService = googleServicesConfigService;
    }

    @SuppressWarnings("WeakerAccess")
    public String getGCSEKey(){
        return googleServicesConfigService.getConfig().googleCustomSearchEngineKey();
    }

    public String getGCSEKeyParameterName() { return googleServicesConfigService.getConfig().googleCustomSearchEngineKeyParameterName(); }

    public String getGCSESource() { return googleServicesConfigService.getConfig().googleCustomSearchEngineSource(); }
}
