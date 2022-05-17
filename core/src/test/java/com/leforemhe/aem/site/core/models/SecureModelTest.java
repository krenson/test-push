package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@ForModel(
        models= { SecureModel.class },
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/SecureModelTest.json"
)
public class SecureModelTest extends AbstractModelTest {

    SecureModel secured;
    SecureModel unsecured;
    Page page;

    @Override
    public void setup() {
        context.addModelsForClasses(SecureModel.class);
        page = context.currentResource("/content/demo-page").adaptTo(Page.class);
        secured = context.currentResource("/content/demo-page/jcr:content/secured").adaptTo(SecureModel.class);
        unsecured = context.currentResource("/content/demo-page/jcr:content/unsecured").adaptTo(SecureModel.class);
    }

    @Test
    public void hasPageRoles(){
        assertEquals(2, page.getProperties().get("roles", String[].class).length, "Roles size should be 2");
    }


    @Test
    public void hasSecurityRoles(){
        assertEquals(1, page.getProperties().get("rolesSecurity", String[].class).length, "Roles size should be 1");
    }

    @Test
    public void isSecured(){
        assertTrue(secured.getSecured(), "Secure Component shouldn't be anonymous");
    }

    @Test
    public void isUnsecured() {
        assertFalse(unsecured.getSecured(), "Secure Component should be anonymous");
    }
}
