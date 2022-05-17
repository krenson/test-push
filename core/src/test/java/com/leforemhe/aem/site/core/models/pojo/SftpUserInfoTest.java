package com.leforemhe.aem.site.core.models.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
public class SftpUserInfoTest {

    private String passphrase = "passphrase";

    @Test
    public void testSftpUserInfo() {

        SftpUserInfo sftpUserInfo = new SftpUserInfo(passphrase);
        assertEquals("passphrase", sftpUserInfo.getPassphrase());
        assertNull(sftpUserInfo.getPassword());
        assertTrue(sftpUserInfo.promptPassphrase(""));
        assertFalse(sftpUserInfo.promptPassword(""));
        assertTrue(sftpUserInfo.promptYesNo(""));
    }
}
