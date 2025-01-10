package android.security.checking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText userNameField = findViewById(R.id.loginUserName);
        EditText passwordField = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.loginBtn);
        Button registerButton = findViewById(R.id.regBtn);
        Button viewUsersButton = findViewById(R.id.viewUsersBtn);

        loginButton.setOnClickListener(v -> {
            String username = userNameField.getText().toString();
            String password = passwordField.getText().toString();

            User user = UserManager.findUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, registration.class);
            startActivity(intent);
        });

        viewUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
        });
    }
}
