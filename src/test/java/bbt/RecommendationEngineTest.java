package bbt;
import recommendation.RecommendationEngine;
import constructors.Movie;
import constructors.User;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class RecommendationEngineTest {
    @Test
    void testGenerateRecommendations_ValidMatch() throws IOException {
        Movie m1 = new Movie("Inception", "I123", Arrays.asList("Sci-Fi", "Action"));
        Movie m2 = new Movie("Toy Story", "TS322", Arrays.asList("Comedy"));
        List<Movie> movieList = Arrays.asList(m1, m2);
        User u1 = new User("Nouran Awad", "98712312r", Arrays.asList("Action"));
        List<User> userList = Arrays.asList(u1);
        RecommendationEngine engine = new RecommendationEngine();
        String result = engine.generateRecommendations(movieList, userList);
        assertTrue(result.contains("For User: Nouran Awad, 98712312r"));
        assertTrue(result.contains("Action: I123-Inception"));
    }
    @Test
    void testGenerateRecommendations_NoValidMatch() throws IOException {
        Movie m1 = new Movie("Inception", "I123", Arrays.asList("Sci-Fi", "Action"));
        List<Movie> movieList = Arrays.asList(m1);
        User u1 = new User("Nouran Awad", "98712312r", Arrays.asList("Horror"));
        List<User> userList = Arrays.asList(u1);
        RecommendationEngine engine = new RecommendationEngine();
        String result = engine.generateRecommendations(movieList, userList);
        assertTrue(result.contains("For User: Nouran Awad, 98712312r"));
        assertFalse(result.contains("Horror:"));
    }
}
