package android.security.checking;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static List<User> userList = new ArrayList<>();

    public static void addUser(User user) {
        userList.add(user);
    }

    public static List<User> getUsers() {
        return userList;
    }

    public static User findUserByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
}
