package com.leforemhe.aem.site.core.models.pojo;

import com.jcraft.jsch.UserInfo;

/**
 * Model to represent the info of an SFTP User
 */
public class SftpUserInfo implements UserInfo {

    private String passphrase = "";

    /**
     * Instantiating the user info based on a given passphrase
     * @param passphrase User info passphrase
     */
    public SftpUserInfo (String passphrase) {
        setPassphrase(passphrase);
    }

    private void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    public String getPassphrase() {
        return passphrase;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return true;
    }

    @Override
    public boolean promptPassword(String message) {
        return false;
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
        // Not required
    }
}
