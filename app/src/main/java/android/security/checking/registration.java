package android.security.checking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Arrays;

import javax.crypto.SecretKey;

public class registration extends AppCompatActivity {

    private static final String KEY_ALIAS = "myKeyAlias"; // Unique identifier for the key in KeyStore
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        CryptoUtils cryptoUtils = new CryptoUtils();

        // Your UI initialization code...

        // Handle registration button click
        Button signUpButton = findViewById(R.id.signUpBtn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                // Generate or retrieve the secret key from KeyStore
                //SecretKey secretKey = cryptoUtils.getSecretKey();

                // Encrypt the user data
                byte[] encryptedBytes = cryptoUtils.encryptData(password);
                String encryptedPassword = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
                // Store the encrypted data (e.g., in SharedPreferences)
                saveUserData(userName, email, encryptedPassword);

                // Proceed with registration logic...
                // Handle registration button click
                Button registerButton = findViewById(R.id.signUpBtn);
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();

                        // Create an Intent to start the RegistrationActivity
                        Intent intent = new Intent(registration.this, login.class);

                        // Start the RegistrationActivity
                        startActivity(intent);
                    }
                });

            }
        });
    }






    private void saveUserData(String userName, String email, String encryptedPassword) {
        // Save the encrypted data (e.g., in SharedPreferences)
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userName);
        editor.putString("email", email);
        editor.putString("encryptedPassword", encryptedPassword);
        editor.apply();
    }
}
