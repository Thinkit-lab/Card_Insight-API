package com.mintyn.cardInsight.security;

import com.mintyn.cardInsight.constants.ResponseStatus;
import com.mintyn.cardInsight.exceptions.EncryptionException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.UUID;

@Component
public class UUIDEncryptor {

    private final SecretKey secretKey;
    private static UUIDEncryptor instance;

    public UUIDEncryptor() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException(ResponseStatus.ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public static UUIDEncryptor getInstance() {
        if (instance == null) {
            synchronized (UUIDEncryptor.class) {
                if (instance == null) {
                    instance = new UUIDEncryptor();
                }
            }
        }
        return instance;
    }

    public String encryptUUID(String uuid) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(uuid.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException(ResponseStatus.ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public String decryptUUID(String encryptedUUID) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedUUID);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException(ResponseStatus.ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }
}
