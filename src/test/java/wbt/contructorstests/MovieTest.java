package wbt.contructorstests;

import java.util.List;

import org.junit.jupiter.api.Test;

import constructors.Movie;

public class MovieTest {

    @Test
    void movie_constructor_storesAllFields() {
        // Statement coverage: testing constructor creation and getter methods
        List<String> categories = List.of("Action", "Drama");
        Movie m = new Movie("The Dark Knight", "TDK001", categories);
        m.getTitle();
        m.getId();
        m.getCategories();
    }

}
