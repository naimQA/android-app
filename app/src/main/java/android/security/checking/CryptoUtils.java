package android.security.checking;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoUtils {

    private static final String KEY_ALIAS = "MyKeyAlias";
    private static final String ALGORITHM_AES = KeyProperties.KEY_ALGORITHM_AES;
    private static final String BLOCK_MODE_CBC = KeyProperties.BLOCK_MODE_CBC;
    private static final String PADDING_PKCS7 = KeyProperties.ENCRYPTION_PADDING_PKCS7;

    private static final String TRANSFORMATION = ALGORITHM_AES + "/" + BLOCK_MODE_CBC + "/" + PADDING_PKCS7;
    private Cipher cipher;

    public CryptoUtils() {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    SecretKey getSecretKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES, "AndroidKeyStore");
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(BLOCK_MODE_CBC)
                        .setEncryptionPaddings(PADDING_PKCS7)
                        .build());
                keyGenerator.generateKey();
            }

            return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] encryptData(String data) {
        try {
            SecretKey secretKey = getSecretKey();

            if (secretKey != null) {
                // Generate a random IV
                SecureRandom secureRandom = new SecureRandom();
                byte[] iv = new byte[cipher.getBlockSize()];
                secureRandom.nextBytes(iv);

                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
                byte[] encryptedBytes = cipher.doFinal(data.getBytes());

                // Combine IV and encrypted data
                byte[] combined = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, combined, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

                return combined;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptData(String encryptedData) {
        try {
            SecretKey secretKey = getSecretKey();

            if (secretKey != null) {
                // Extract IV from the combined data
                byte[] combined = Base64.decode(encryptedData, Base64.DEFAULT);
                byte[] iv = Arrays.copyOfRange(combined, 0, cipher.getBlockSize());

                // Initialize Cipher for decryption with the extracted IV
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

                // Decrypt the data (excluding the IV)
                byte[] encryptedBytes = Arrays.copyOfRange(combined, cipher.getBlockSize(), combined.length);
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

                return new String(decryptedBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


    /*public static String decryptData(String encryptedData, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            // Decode the Base64 string to get the combined data
            byte[] combined = Base64.decode(encryptedData, Base64.DEFAULT);

            // Extract IV from the combined data
            byte[] iv = Arrays.copyOfRange(combined, 0, cipher.getBlockSize());

            // Initialize the cipher for decryption with the extracted IV
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            // Decrypt the rest of the data (excluding IV)
            byte[] encryptedBytes = Arrays.copyOfRange(combined, cipher.getBlockSize(), combined.length);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getStoredEncryptedPassword(Context context) {
        try {
            // Access the context to getSharedPreferences
            SharedPreferences preferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);

            // Retrieve the encrypted password
            return preferences.getString("encryptedPassword", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

