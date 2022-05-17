package com.leforemhe.aem.site.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Export SFTP Configuration
 * Configuration for exports to SFTP server
 */
@ObjectClassDefinition(name="leforemhe - Exports to SFTP server",
        description = "Configuration for exports to SFTP server")
public @interface ExportSFTPConfig {

    String DEFAULTRESOURCESERVER = "localhost:4502";

    /**
     * SFTP server host
     * @return SFTP server host
     */
    @AttributeDefinition(
            name = "SFTP server host",
            type = AttributeType.STRING
    )
    String sftpHost() default "localhost";

    /**
     * SFTP server port
     * @return SFTP server port
     */
    @AttributeDefinition(
            name = "SFTP server port",
            type = AttributeType.INTEGER
    )
    int sftpPort() default 22;

    /**
     * SFTP server username
     * @return SFTP server username
     */
    @AttributeDefinition(
            name = "SFTP server username",
            type = AttributeType.STRING
    )
    String sftpUsername() default "tester";

    /**
     * SFTP server authentication method
     * @return SFTP server authentication method
     */
    @AttributeDefinition(
            name = "SFTP server authentication method. Write password or publickey",
            type = AttributeType.STRING
    )
    String sftpAuthMethod() default "publickey";

    /**
     * SFTP server password
     * @return SFTP server password
     */
    @AttributeDefinition(
            name = "SFTP server password. Ignore it whether you are using public key method",
            type = AttributeType.PASSWORD
    )
    String sftpPassword() default "password";

    /**
     * Path to upload files
     * @return Path to upload files
     */
    @AttributeDefinition(
            name = "Base path to upload files",
            type = AttributeType.STRING
    )
    String sftpBasePath() default "";

    /**
     * Private key path on the server
     * @return Private key path on the server
     */
    @AttributeDefinition(
            name = "Private key path on the server. Ignore it whether you are using password method",
            type = AttributeType.STRING
    )
    String sftpPrivateKey() default "/home/crx/.ssh/aem_sftp_dev";

    /**
     * Private key passphrase
     * @return Private key passphrase
     */
    @AttributeDefinition(
            name = "Private key passphrase. Ignore it whether you are using password method",
            type = AttributeType.PASSWORD
    )
    String sftpPassphrase() default "";
    /**
     * Private key actionput
     * @return Private key actionput
     */
    @AttributeDefinition(
            name = "Private key actionput",
            type = AttributeType.STRING
    )
    String sftpActionPut() default "ADD";
    /**
     * Private key actionremove
     * @return Private key actionremove
     */
    @AttributeDefinition(
            name = "Private key action remove",
            type = AttributeType.STRING
    )
    String sftpActionRemove() default "REMOVE";
    /**
     * Private key filelockname
     * @return Private key filelockname
     */
    @AttributeDefinition(
            name = "Private key filelockname",
            type = AttributeType.STRING
    )
    String sftpFilelockName() default "file.lock";
}
