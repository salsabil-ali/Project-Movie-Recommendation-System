package wbt.recommendationtests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import recommendation.RecommendationEngine;
import constructors.Movie;
import constructors.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecommendationEngineTest {

    private RecommendationEngine engine;
    private Path outputFile;

    // ─────────────────────────────────────────
    // SETUP
    // ─────────────────────────────────────────

    @BeforeEach
    void setUp() throws IOException {
        engine = new RecommendationEngine();
        outputFile = Files.createTempFile("recommendations_test", ".txt");
    }

    // Helper: reads the output file into one String
    private String readOutput() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(outputFile.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────
    // writeError tests
    // ─────────────────────────────────────────

    @Test
    void writeError_writesMessageToFile() throws Exception {
        engine.writeError("Movie Title ERROR: the dark is wrong",
                outputFile.toString());
        String content = readOutput();
        assertTrue(content.contains("Movie Title ERROR: the dark is wrong"));
    }

    @Test
    void writeError_emptyMessage_writesEmptyFile() throws Exception {
        engine.writeError("", outputFile.toString());
        String content = readOutput();
        assertEquals("", content.trim());
    }

    // ─────────────────────────────────────────
    // generateRecommendations — outer user loop
    // ─────────────────────────────────────────

    @Test
    void generateRecommendations_emptyUserList_writesNothing() throws Exception {
        List<Movie> movies = Arrays.asList(
            new Movie("The Dark Knight", "TDK001",
                      Arrays.asList("action"))
        );
        List<User> users = Collections.emptyList();

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        assertEquals("", content.trim());
    }

    @Test
    void generateRecommendations_singleUser_headerIsWritten() throws Exception {
        List<Movie> movies = Collections.emptyList();
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789",
                     Collections.emptyList())
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        assertTrue(content.contains("For User: Ahmed Ali, 123456789"));
    }

    @Test
    void generateRecommendations_multipleUsers_allHeadersWritten() throws Exception {
        List<Movie> movies = Collections.emptyList();
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Collections.emptyList()),
            new User("Sara Hassan", "987654321", Collections.emptyList())
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        assertTrue(content.contains("For User: Ahmed Ali, 123456789"));
        assertTrue(content.contains("For User: Sara Hassan, 987654321"));
    }

    // ─────────────────────────────────────────
    // generateRecommendations — inner category loop + foundAny branch
    // ─────────────────────────────────────────

    @Test
    void generateRecommendations_noMatchingMovies_categoryLineNotWritten() throws Exception {
        List<Movie> movies = Arrays.asList(
            new Movie("Scary Movie", "SM002", Arrays.asList("horror"))
        );
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Arrays.asList("action"))
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        // The user header must appear
        assertTrue(content.contains("For User: Ahmed Ali, 123456789"));
        // But the category line must NOT appear because foundAny stays false
        assertFalse(content.contains("action:"));
    }

    @Test
    void generateRecommendations_oneMatchingMovie_categoryLineWritten() throws Exception {
        List<Movie> movies = Arrays.asList(
            new Movie("The Dark Knight", "TDK001", Arrays.asList("action"))
        );
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Arrays.asList("action"))
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        assertTrue(content.contains("action: TDK001-The Dark Knight"));
    }

    @Test
    void generateRecommendations_multipleMatchingMovies_allListedWithComma() throws Exception {
        List<Movie> movies = Arrays.asList(
            new Movie("The Dark Knight", "TDK001", Arrays.asList("action")),
            new Movie("Avengers", "A002", Arrays.asList("action"))
        );
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Arrays.asList("action"))
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        // Both movies should appear on the same line separated by comma
        assertTrue(content.contains("TDK001-The Dark Knight"));
        assertTrue(content.contains("A002-Avengers"));
        assertTrue(content.contains(","));
    }

    // ─────────────────────────────────────────
    // generateRecommendations — case-insensitivity branch
    // ─────────────────────────────────────────

    @Test
    void generateRecommendations_categoryMatchIsCaseInsensitive() throws Exception {
        // Movie stores "Action" (capital A), user likes "action" (lowercase)
        List<Movie> movies = Arrays.asList(
            new Movie("The Dark Knight", "TDK001", Arrays.asList("Action"))
        );
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Arrays.asList("action"))
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        // equalsIgnoreCase branch is hit and the movie IS recommended
        assertTrue(content.contains("TDK001-The Dark Knight"));
    }

    // ─────────────────────────────────────────
    // generateRecommendations — movie with multiple categories
    // ─────────────────────────────────────────

    @Test
    void generateRecommendations_movieHasMultipleCategories_matchesCorrectOne() throws Exception {
        // Movie belongs to both action and drama
        List<Movie> movies = Arrays.asList(
            new Movie("Inception", "I001", Arrays.asList("action", "drama"))
        );
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Arrays.asList("drama"))
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        assertTrue(content.contains("I001-Inception"));
    }

    // ─────────────────────────────────────────
    // generateRecommendations — user with multiple liked categories
    // ─────────────────────────────────────────

    @Test
    void generateRecommendations_userLikesMultipleCategories_allCategoryLinesWritten()
            throws Exception {
        List<Movie> movies = Arrays.asList(
            new Movie("The Dark Knight", "TDK001", Arrays.asList("action")),
            new Movie("The Notebook",    "TN003",  Arrays.asList("drama"))
        );
        List<User> users = Arrays.asList(
            new User("Ahmed Ali", "123456789", Arrays.asList("action", "drama"))
        );

        engine.generateRecommendations(movies, users, outputFile.toString());
        String content = readOutput();
        assertTrue(content.contains("action: TDK001-The Dark Knight"));
        assertTrue(content.contains("drama: TN003-The Notebook"));
    }
}