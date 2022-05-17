package com.leforemhe.aem.site.core.servlet;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SuppressWarnings("WeakerAccess")
public class ContactFormServletTest extends AbstractAEMServletTest {

    Map<String, String[]> map;

    private ContactFormServlet contactFormServlet = null;

    public void setup(){
        contactFormServlet = context.registerInjectActivateService(new ContactFormServlet());

        map = new HashMap<>();
        map.put("parameter", new String[]{"value"});
        map.put("fichier", new String[]{"file.txt"});

        lenient().when(requestMock.getParameterMap()).thenReturn(map);

    }

    @Test
    public void doPostTest() throws Exception {
        try {
            contactFormServlet.doPost(requestMock, responseMock);
        } catch (IOException e){
            fail(e);
        }
        verify(requestMock, times(1)).getParameterMap();
        verify(responseMock, times(1)).sendRedirect("/content/leforemhe/fr/pages-non-places/formulaire-confirmation-envoi.html?wcmmode=disabled");
    }

}
