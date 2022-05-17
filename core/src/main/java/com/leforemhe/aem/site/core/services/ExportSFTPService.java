package com.leforemhe.aem.site.core.services;

import com.jcraft.jsch.*;
import com.leforemhe.aem.site.core.config.ExportSFTPConfig;
import com.leforemhe.aem.site.core.models.pojo.SftpUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.platform.commons.function.Try;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Service to export content to SFTP server
 */
@Component(
        service = ExportSFTPService.class,
        immediate = true
)
public class ExportSFTPService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportSFTPService.class);

    private static final String METHOD_PUBLIC_KEY = "publickey";
    private static final String METHOD_PASS = "password";

    private static Channel channel;
    private static Session session;

    /**
     * Export using directly a Export SFTP Configuration The purpose of this method
     * is to avoid duplicates when we compute the authentication way (user/password
     * or private key/passphrase) in the different Export processes
     *
     * @param action   Send Content
     * @param content  Content to export
     * @param filename Filename of the content
     * @param path     Path to where export the content
     * @param config   SFTP configuration
     */
    public void exportFileFromConfig(String action, String content, String filename, String path,
                                     ExportSFTPConfig config) {
        String authmethod = config.sftpAuthMethod();
        switch (authmethod) {
            case METHOD_PUBLIC_KEY:
                LOG.debug("Using the public key authentication method");
                this.exportFileByPublicKey(action, content, filename, path, config);
                break;
            case METHOD_PASS:
                LOG.debug("Using the password authentication method");
                this.exportFileByPass(action, content, filename, path, config);
                break;
            default:
                LOG.debug("Authentication method missing: {}", authmethod);
        }
    }

    /**
     * Exporting with username and password
     *
     * @param action   Send Content
     * @param content  File content
     * @param filename File name
     * @param path     SFTP server path to where exporting the file
     * @param config   SFTP configuration
     */
    public void exportFileByPass(String action, String content, String filename, String path, ExportSFTPConfig config) {
        LOG.debug("ExportSFTPService: Inside exportFileByPass");

        session = getSession(config);
        if (session != null) {
            LOG.debug("Session created");
            session.setPassword(config.sftpPassword());
            LOG.debug("Password set up");
            LOG.debug("Preparing to send file...");
            //LOG.debug("Content: {}", content);
            LOG.debug("Action: {}", action);
            if (StringUtils.equals(action, config.sftpActionPut()))
                sendFile(content, filename, path);
            if (StringUtils.equals(action, config.sftpActionRemove()))
                removeFile(content, filename, path);
        }
    }

    /**
     * Exporting the file with a private key and a passphrase
     *
     * @param action   Send Content
     * @param content  File content
     * @param filename File name
     * @param path     SFTP server path to where exporting the file
     * @param config   SFTP configuration
     */
    public void exportFileByPublicKey(String action, String content, String filename, String path,
                                      ExportSFTPConfig config) {
        LOG.debug("ExportSFTPService: Inside exportFileByPublicKey");

        session = getSession(config);
        if (session != null) {
            LOG.debug("Session created");
            UserInfo userInfo = new SftpUserInfo(config.sftpPassphrase());
            LOG.debug("Set up UserInfo");
            session.setUserInfo(userInfo);
            LOG.debug("Preparing to send file");
            //LOG.debug("Content: {}", content);
            LOG.debug("Action: {}", action);
            if (StringUtils.equals(action, config.sftpActionPut()))
                sendFile(content, filename, path);
            if (StringUtils.equals(action, config.sftpActionRemove()))
                removeFile(content, filename, path);
        }
    }

    private void sendFile(String content, String filename, String path) {
        try {
            channel = getChannel();
            if (channel != null && channel.isConnected()) {
                LOG.debug("Shell channel connected");
                ChannelSftp channelSftp = (ChannelSftp) channel;
                StringTokenizer tokenizer = new StringTokenizer(path, "/");
                while (tokenizer.hasMoreElements()) {
                    String currentPath = tokenizer.nextToken();
                    LOG.debug("currentPath: {}", currentPath);
                    try {
                        channelSftp.cd(currentPath);
                        LOG.debug("cd currentPath: {}", currentPath);
                    } catch (SftpException e) {
                        channelSftp.mkdir(currentPath);
                        LOG.debug("mk currentPath: {}", currentPath);
                        channelSftp.cd(currentPath);
                        LOG.debug("cd currentPath: {}", currentPath);
                    }
                    if (!channelSftp.pwd().endsWith(currentPath)) {
                        channelSftp.cd(currentPath);
                        LOG.debug("channel cd currentPath: {}", currentPath);
                    }
                }
                LOG.debug("Changed the directory: {}", path);
                InputStream initialStream = new ByteArrayInputStream(content.getBytes());
                LOG.debug("Uploading the file {}", filename);
                channelSftp.put(initialStream, filename);
                channelSftp.cd("/");
                LOG.debug("File uploaded");
            }
        } catch (SftpException e) {
            LOG.error("Something went wrong while uploading the file {} into {}: {}", filename, path, e.getMessage());
        }
    }

    private void removeFile(String content, String filename, String path) {
        try {
            channel = getChannel();
            if (channel != null && channel.isConnected()) {
                ChannelSftp channelSftp = (ChannelSftp) channel;
                if (!channelSftp.pwd().endsWith(path)) {
                    channelSftp.cd(path);
                }
                LOG.debug("Changed the directory: {}", path);
                LOG.debug("Uploading the file {}", filename);
                channelSftp.rm(filename);
                LOG.debug("File uploaded");
            }
        } catch (SftpException e) {
            LOG.error("Something went wrong while uploading the file {} into {}: {}", filename, path, e.getMessage());
        }
    }

    public Channel getChannel() {
        try {
            if (channel == null || !channel.isConnected()) {
                LOG.debug("Set up properties");
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                LOG.debug("Starting session");
                if (!session.isConnected()) {
                    session.connect();
                }
                LOG.debug("Session connected");
                channel = session.openChannel("sftp");
                channel.connect();
                return channel;
            }

        } catch (JSchException e) {
            LOG.error("Something went wrong while connecting the SFTP client via getChannel: {} {}", e.getMessage(), e.getStackTrace());
        }
        return channel;
    }

    public void closeConnections() {
        if (session != null && !session.isConnected()) {
            session.disconnect();
        }
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
    }

    public Session getSession(ExportSFTPConfig config) {
        try {
            if (session == null || !session.isConnected()) {
                JSch jSch = new JSch();
                LOG.debug("Setting up connection to SFTP server {}, username {} and port {}", config.sftpHost(),
                        config.sftpUsername(), config.sftpPort());
                if (config.sftpAuthMethod().equals(METHOD_PUBLIC_KEY)) {
                    jSch.addIdentity(config.sftpUsername(), config.sftpPrivateKey().getBytes(), null, config.sftpPassphrase().getBytes());
                    LOG.debug("Added private key");
                }
                session = jSch.getSession(config.sftpUsername(), config.sftpHost(), config.sftpPort());
            }
        } catch (JSchException e) {
            LOG.error("Something went wrong while connecting the SFTP client via getSession (public key auth): {}", e.getMessage());
        }
        return session;
    }
}