package android.security.checking;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ListView userListView = findViewById(R.id.userListView);

        List<String> userInfoList = new ArrayList<>();
        for (User user : UserManager.getUsers()) {
            userInfoList.add("Username: " + user.getUsername() + "\nEmail: " + user.getEmail());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userInfoList);
        userListView.setAdapter(adapter);
    }
}
