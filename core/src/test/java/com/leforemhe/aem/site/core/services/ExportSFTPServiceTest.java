package com.leforemhe.aem.site.core.services;

import com.ghdiri.abdallah.sftp.DummySftpServerExtension;
import com.ghdiri.abdallah.sftp.SftpGateway;
import com.leforemhe.aem.site.core.AbstractAEMTest;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

public class ExportSFTPServiceTest extends AbstractAEMTest {

    private static final int fakePort = 1234;
    private static final String fakeUsername = "test";
    private static final String fakePassword = "password";
    private static final String fakePrivateKey = "privatekeypath";
    private static final String fakePassphrase = "passphrase";
    private static final String fakePath = "directory";
    private static final String fakeHost = "localhost";
    private static final String fakeActionPut = "ADD";
    private static final String fakeActionRemove = "REMOVE";
    private static final String fakeFilelockName = "file.lock";

    @RegisterExtension
    static final DummySftpServerExtension extension = DummySftpServerExtension.Builder.create()
            .port(fakePort)
            .addCredentials(fakeUsername, fakePassword)
            .build();

    private ExportSFTPService exportSftpService;

    @Mock
    ExportSFTPConfig config;

    @Override
    public void setup() {
        lenient().when(config.sftpHost()).thenReturn(fakeHost);
        lenient().when(config.sftpPort()).thenReturn(fakePort);
        lenient().when(config.sftpBasePath()).thenReturn(fakePath);
        lenient().when(config.sftpPassword()).thenReturn(fakePassword);
        lenient().when(config.sftpPassphrase()).thenReturn(fakePassphrase);
        lenient().when(config.sftpUsername()).thenReturn(fakeUsername);
        lenient().when(config.sftpPrivateKey()).thenReturn(fakePrivateKey);
        lenient().when(config.sftpActionPut()).thenReturn(fakeActionPut);
        lenient().when(config.sftpActionRemove()).thenReturn(fakeActionRemove);
        lenient().when(config.sftpFilelockName()).thenReturn(fakeFilelockName);

        exportSftpService = context.registerInjectActivateService(new ExportSFTPService());
    }

    @Test
    void exportFile(SftpGateway gateway) throws IOException {
        String content = "my content";
        String filename = "file.txt";
        gateway.createDirectories(fakePath);
        lenient().when(config.sftpAuthMethod()).thenReturn("password");
        exportSftpService.exportFileByPass("ADD", content, filename, fakePath, config);
        String remoteFileContent = gateway.getFile(fakePath + "/" + filename);
        assertEquals(content, remoteFileContent);
        lenient().when(config.sftpAuthMethod()).thenReturn("publickey");
        exportSftpService.exportFileByPublicKey("ADD", content, filename, fakePath, config);
        assertEquals(content, remoteFileContent);
        lenient().when(config.sftpAuthMethod()).thenReturn("password");
        exportSftpService.exportFileFromConfig("ADD", content, filename, fakePath, config);
        remoteFileContent = gateway.getFile(fakePath + "/" + filename);
        assertEquals(content, remoteFileContent);
        lenient().when(config.sftpAuthMethod()).thenReturn("publickey");
        exportSftpService.exportFileFromConfig("ADD", content, filename, fakePath, config);
        assertEquals(content, remoteFileContent);
    }

    @Test
    void noErrorOnWrongSftpChannel() {
        String content = "my content";
        String filename = "file.txt";
        lenient().when(config.sftpAuthMethod()).thenReturn("password");
        assertDoesNotThrow(() -> exportSftpService.exportFileByPass("ADD", content, filename, fakePath, config));
        lenient().when(config.sftpAuthMethod()).thenReturn("publickey");
        assertDoesNotThrow(() -> exportSftpService.exportFileByPublicKey("ADD", content, filename, fakePath, config));
    }
}