package com.leforemhe.aem.site.core.servlet;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.leforemhe.aem.site.core.config.RteAbbreviationConfig;
import com.leforemhe.aem.site.core.models.pojo.Abbreviation;
import com.leforemhe.aem.site.core.services.RteAbbreviationConfigService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RteAbbreviationServletTest extends AbstractAEMServletTest {

    private static final String[] ABBREVIATIONS_VAL = new String[]{"et cetera=etc", "Monsieur=Mr.", "fr:Madame=Mme", "en:by the way=btw"};

    @Mock
    private RteAbbreviationConfigService configService;

    @Mock
    private RteAbbreviationConfig config;

    @Mock
    private PrintWriter writer;

    private RteAbbreviationsServlet servlet = null;

    @Override
    public void setup() throws Exception {
        lenient().when(configService.getConfig()).thenReturn(config);
        lenient().when(config.abbreviations()).thenReturn(ABBREVIATIONS_VAL);
        lenient().when(responseMock.getWriter()).thenReturn(writer);
        context.registerService(configService);
        servlet = context.registerInjectActivateService(new RteAbbreviationsServlet());
    }

    @Test
    void getJSONObject() throws ServletException, IOException, JSONException {
        ArgumentCaptor<String> JSONStringCaptor = ArgumentCaptor.forClass(String.class);
        servlet.doGet(requestMock, responseMock);
        verify(writer, times(1)).print(JSONStringCaptor.capture());
        List<String> capturedJSON = JSONStringCaptor.getAllValues();
        assertEquals(1, capturedJSON.size());
        String result = capturedJSON.get(0);
        Gson gson = new Gson();
        Map resultMap = gson.fromJson(result, HashMap.class);
        Map<String, List<Abbreviation>> json = new HashMap<>();
        for(Object key : resultMap.keySet()) {
            List<Abbreviation> list = new ArrayList<>();
            for(LinkedTreeMap linkedTreeMap : ((List<LinkedTreeMap>) resultMap.get(key))) {
                list.add(gson.fromJson(gson.toJson(linkedTreeMap), Abbreviation.class));
            }
            json.put((String) key, list);
        }
        assertTrue(json.containsKey("fr"));
        assertTrue(json.containsKey("en"));
        List<Abbreviation> frenchJSON = json.get("fr");
        List<Abbreviation> englishJSON = json.get("en");
        assertEquals(3, frenchJSON.size());
        assertEquals(1, englishJSON.size());
        Abbreviation etcJSON = frenchJSON.get(0);
        assertEquals("etc", etcJSON.getAbbreviation());
        assertEquals("et cetera", etcJSON.getWord());
        Abbreviation btwJSON = englishJSON.get(0);
        assertEquals("btw", btwJSON.getAbbreviation());
        assertEquals("by the way", btwJSON.getWord());
    }
}
