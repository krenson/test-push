package com.leforemhe.aem.site.core.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ForModel(
        models = {MessageGlobalModel.class},
        jsonToLoadPath = "/com/leforemhe/aem/site/core/models/MessageGlobalTest.json"
)
class MessageGlobalTest extends AbstractModelTest {

    @Test
    void getMessageModel() {
        String expectedMessage = "This is my message !";
        String expectedYear = "2021";
        String expectedStartDay = "24";
        String expectedEndDay= "28";
        String expectedStartMonth= "1";
        String expectedEndMonth = "2";
        String expectedTime = "00";

        context.currentResource("/content/demo-page/jcr:content");
        MessageGlobalModel model = context.request().getResource().adaptTo(MessageGlobalModel.class);

        assertNotNull(model);
        assertEquals(expectedMessage, model.getMessage());

        assertEquals(expectedYear, model.getStartYear());
        assertEquals(expectedStartDay, model.getStartDay());
        assertEquals(expectedStartMonth, model.getStartMonth());
        assertEquals(expectedTime, model.getStartHour());
        assertEquals(expectedTime, model.getStartMinute());
        assertEquals(expectedTime, model.getStartSecond());

        assertEquals(expectedYear, model.getEndYear());
        assertEquals(expectedEndDay, model.getEndDay());
        assertEquals(expectedEndMonth, model.getEndMonth());
        assertEquals(expectedTime, model.getEndHour());
        assertEquals(expectedTime, model.getEndMinute());
        assertEquals(expectedTime, model.getEndSecond());
    }
}
