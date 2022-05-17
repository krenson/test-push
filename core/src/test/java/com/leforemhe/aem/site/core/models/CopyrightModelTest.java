package com.leforemhe.aem.site.core.models;

import com.day.cq.wcm.api.PageManager;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static junitx.framework.ComparableAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {CopyrightModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/CopyrightTest.json"
)
@SuppressWarnings("WeakerAccess")
public class CopyrightModelTest extends AbstractModelTest {

    public void setup() {
        context.registerService(ResourceResolver.class);
        context.registerService(PageManager.class);
    }

    @Test
    public void getLabelTest() {
        final String expected = "Tests Copyright";
        context.currentResource("/content/demo-page/jcr:content/copyright");
        CopyrightModel copyrightModel = context.request().getResource().adaptTo(CopyrightModel.class);
        assertNotNull(copyrightModel);
        assertEquals(expected, copyrightModel.getLabel());
    }

    @Test
    public void getYearTest() {
        final String expected = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        context.currentResource("/content/demo-page/jcr:content/copyright");
        CopyrightModel copyrightModel = context.request().getResource().adaptTo(CopyrightModel.class);
        assertNotNull(copyrightModel);
        assertEquals(expected, copyrightModel.getYear());
    }
}
