package com.tobilko;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

/**
 * Created by Andrew Tobilko on 10/28/18.
 */
@Service
public class SecurityService {

    private boolean encryptResponses = true;
    private String encryptionAlgorithm = "PBEWithMD5AndTripleDES";
    private String baseServerURL = "http://localhost:8080";

    @JsonIgnore
    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    {
        encryptor.setAlgorithm(encryptionAlgorithm);
        encryptor.setPassword("tobilko");
    }

    public boolean isEncryptResponses() {
        return encryptResponses;
    }

    public void setEncryptResponses(boolean encryptResponses) {
        this.encryptResponses = encryptResponses;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        encryptor.setAlgorithm(encryptionAlgorithm);
    }

    public String getBaseServerURL() {
        return baseServerURL;
    }

    public void setBaseServerURL(String baseServerURL) {
        this.baseServerURL = baseServerURL;
    }

    @JsonIgnore
    public StandardPBEStringEncryptor getEncryptor() {
        return encryptor;
    }
}
