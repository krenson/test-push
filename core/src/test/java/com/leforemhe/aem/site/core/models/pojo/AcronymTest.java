package com.leforemhe.aem.site.core.models.pojo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AcronymTest {

    @Test
    public void getAcronym() {
        Acronym acronym = new Acronym("text", "acronym");
        assertNotNull(acronym);
        assertEquals("text", acronym.getText());
        assertEquals("acronym", acronym.getAcronym());
    }
}
