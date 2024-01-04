package android.security.checking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

import javax.crypto.SecretKey;


public class login extends AppCompatActivity {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    RelativeLayout nMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nMain = findViewById(R.id.main);
        CryptoUtils cryptoUtils = new CryptoUtils();

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device doesn't have fingerprint", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Not working", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No fingerPrint Assigned", Toast.LENGTH_SHORT).show();
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Login error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Use your biometric to authenticate")
                .setNegativeButtonText("Cancel")
                .build();

        findViewById(R.id.biometricButton).setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));

        // Handle login button click
        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String enteredUsername = ((EditText) findViewById(R.id.loginUserName)).getText().toString();
                String enteredPassword = ((EditText) findViewById(R.id.loginPassword)).getText().toString();

                // Retrieve stored data from SharedPreferences
                String storedUsername = getStoredUsername(login.this);
                String storedEncryptedPassword = getStoredEncryptedPassword(login.this);
                //SecretKey secretKey = cryptoUtils.getSecretKey();

                // Decrypt stored password
                String storedPassword = cryptoUtils.decryptData(storedEncryptedPassword);

                // Compare entered and stored credentials
                if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                    // Login successful
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    // Proceed with login logic...
                } else {
                    // Login failed
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    // Handle failed login...
                }
            }
        });
        // Handle registration button click
        Button registerButton = findViewById(R.id.regBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the RegistrationActivity
                Intent intent = new Intent(login.this, registration.class);

                // Start the RegistrationActivity
                startActivity(intent);
            }
        });

    }


    // Add these methods for getting stored data from SharedPreferences

    private String getStoredEncryptedPassword(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return preferences.getString("encryptedPassword", "");
    }

    private String getStoredUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return preferences.getString("userName", "");
    }

    // ... (add similar methods for other stored data)
}
