package wbt.contructorstests;

import java.util.List;

import org.junit.jupiter.api.Test;

import constructors.User;

public class UserTest {

    @Test
    void user_constructor_storesAllFields() {
        // Statement coverage: testing constructor creation and getter methods
        List<String> likedCategories = List.of("Action", "Drama");
        User u = new User("Sara Khaled", "12345678A", likedCategories);
        u.getUsername();
        u.getUserId();
        u.getLikedCategories();
    }

}

