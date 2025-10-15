package com.Appzia.enclosure.Model;

import java.io.Serializable;

public class EncryptedMessageModel implements Serializable {

    private byte[] encryptedModel; // Encrypted data (ciphertext)
    private byte[] iv; // Initialization Vector used for encryption

    // Constructor to initialize encrypted data and IV
    public EncryptedMessageModel(byte[] encryptedModel, byte[] iv) {
        this.encryptedModel = encryptedModel;
        this.iv = iv;
    }

    // Getter for encrypted data
    public byte[] getEncryptedModel() {
        return encryptedModel;
    }

    // Setter for encrypted data
    public void setEncryptedModel(byte[] encryptedModel) {
        this.encryptedModel = encryptedModel;
    }

    // Getter for the Initialization Vector (IV)
    public byte[] getIv() {
        return iv;
    }

    // Setter for the Initialization Vector (IV)
    public void setIv(byte[] iv) {
        this.iv = iv;
    }
}
