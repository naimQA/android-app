package android.security.checking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        EditText userName = findViewById(R.id.userName);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText rePassword = findViewById(R.id.RePassword);
        Button signUpButton = findViewById(R.id.signUpBtn);

        signUpButton.setOnClickListener(v -> {
            String usernameValue = userName.getText().toString();
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            String rePasswordValue = rePassword.getText().toString();

            if (usernameValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!passwordValue.equals(rePasswordValue)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (UserManager.findUserByUsername(usernameValue) != null) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(usernameValue, emailValue, passwordValue);
                UserManager.addUser(user);
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
            }
        });
    }
}
