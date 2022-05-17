package com.leforemhe.aem.site.core.models.pojo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbbreviationTest {

    @Test
    void getAbbreviation() {
        Abbreviation abbreviation = new Abbreviation("word", "abbreviation");
        assertNotNull(abbreviation);
        assertEquals("word", abbreviation.getWord());
        assertEquals("abbreviation", abbreviation.getAbbreviation());
    }
}
