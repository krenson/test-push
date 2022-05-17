package com.leforemhe.aem.site.core.services;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("WeakerAccess")
public class ExportSFTPConfigServiceTest extends AbstractAEMConfigServiceTest<ExportSFTPConfigService> {

    private static final String HOST = "sftpHost";
    private static final String PORT = "sftpPort";
    private static final String USERNAME = "sftpUsername";
    private static final String PASSWORD = "sftpPassword";
    private static final String ACTIONPUT = "sftpActionPut";
    private static final String ACTIONREMOVE = "sftpActionRemove";
    private static final String FILELOCKNAME = "sftpFilelockName";

    @Override
    protected Map<String, Object> setupConfigAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(HOST, "localhost");
        map.put(PORT,22);
        map.put(USERNAME, "user");
        map.put(PASSWORD, "password");
        map.put(ACTIONPUT,"ADD");
        map.put(ACTIONREMOVE,"REMOVE");
        map.put(FILELOCKNAME,"file.lock");
        return map;
    }
}
