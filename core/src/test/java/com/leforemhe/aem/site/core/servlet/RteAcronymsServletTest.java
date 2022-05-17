package com.leforemhe.aem.site.core.servlet;

import com.google.gson.Gson;
import com.leforemhe.aem.site.core.config.RteAcronymConfig;
import com.leforemhe.aem.site.core.models.pojo.Acronym;
import com.leforemhe.aem.site.core.services.RteAcronymConfigService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RteAcronymsServletTest extends AbstractAEMServletTest {

    private static final String[] ACRONYMS_VAL = {"Formation professionnelle et Emploi=Le FOREM", "Mise en Situation Professionnelle=MISIP"};

    @Mock
    private RteAcronymConfigService configService;

    @Mock
    private RteAcronymConfig config;

    @Mock
    private PrintWriter writer;

    private RteAcronymsServlet servlet = null;

    @Override
    public void setup() throws Exception {
        lenient().when(configService.getConfig()).thenReturn(config);
        lenient().when(config.acronyms()).thenReturn(ACRONYMS_VAL);
        lenient().when(responseMock.getWriter()).thenReturn(writer);
        context.registerService(configService);
        servlet = context.registerInjectActivateService(new RteAcronymsServlet());
    }

    @Test
    void getJSONObject() throws ServletException, IOException, JSONException {
        ArgumentCaptor<String> jsonStringCaptor = ArgumentCaptor.forClass(String.class);
        servlet.doGet(requestMock, responseMock);
        verify(writer, times(1)).print(jsonStringCaptor.capture());
        List<String> capturedJSON = jsonStringCaptor.getAllValues();
        assertEquals(1, capturedJSON.size());
        String result = capturedJSON.get(0);
        Gson gson = new Gson();
        List results = gson.fromJson(result, ArrayList.class);
        List<Acronym> acronyms = new ArrayList<>();
        for(Object item : results) {
            Acronym acronym = gson.fromJson(gson.toJson(item), Acronym.class);
            acronyms.add(acronym);
        }
        assertFalse(acronyms.isEmpty());
        assertNotNull(acronyms.get(0));
        assertNotNull(acronyms.get(1));
        assertEquals("Formation professionnelle et Emploi", acronyms.get(0).getText());
        assertEquals("Le FOREM", acronyms.get(0).getAcronym());
        assertEquals("Mise en Situation Professionnelle", acronyms.get(1).getText());
        assertEquals("MISIP", acronyms.get(1).getAcronym());
    }
}
