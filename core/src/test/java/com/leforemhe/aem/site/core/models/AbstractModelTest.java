package com.leforemhe.aem.site.core.models;

import com.leforemhe.aem.site.core.AbstractAEMTest;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public abstract class AbstractModelTest extends AbstractAEMTest {

    @Mock
    protected ModelFactory modelFactory;

    @BeforeEach
    public void setupGeneric() throws Exception {
        ForModel forModel = this.getClass().getAnnotation(ForModel.class);
        if(forModel != null){
            for(Class modelClass : forModel.models()){
                context.addModelsForClasses(modelClass);
            }
            // workaround: isBlank() was deleted because Cloud Manger pipeline is working with Java 8 instead of Java 11.
            // Paid attention if this change could affect your unit test.
            if(!forModel.jsonToLoadPath().trim().isEmpty()) context.load().json(forModel.jsonToLoadPath(), "/content/demo-page");
            context.registerService(ModelFactory.class, modelFactory,
                    org.osgi.framework.Constants.SERVICE_RANKING, Integer.MAX_VALUE);
        }
        super.setupGeneric();
    }

}
