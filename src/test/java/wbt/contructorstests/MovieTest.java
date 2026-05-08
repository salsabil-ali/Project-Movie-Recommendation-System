package wbt.contructorstests;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import constructors.Movie;

public class MovieTest {

    @Test
    void movie_constructor_storesAllFields() {
        // Statement coverage: testing constructor creation and getter methods
        List<String> categories = List.of("Action", "Drama");
        Movie m = new Movie("The Dark Knight", "TDK001", categories);
        assertEquals("The Dark Knight", m.getTitle());
        assertEquals("TDK001", m.getId());
        assertEquals(categories, m.getCategories());
    }

}
