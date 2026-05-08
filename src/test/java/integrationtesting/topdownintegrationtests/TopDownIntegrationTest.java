package integrationtesting.topdownintegrationtests;

import constructors.Movie;
import constructors.User;
import inputreader.InputReader;
import recommendation.RecommendationEngine;
import outputwriter.OutputWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopDownIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    public void testMainComponentsInitialization() {
        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        assertNotNull(reader);
        assertNotNull(engine);
        assertNotNull(writer);
    }

    @Test
    public void testReadValidMoviesFile() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SM123\n" +
                "Action,Adventure\n" +
                "Toy Story,TS456\n" +
                "Animation,Comedy\n"
        );

        InputReader reader = new InputReader();
        List<Movie> movies = reader.readMovies(moviesFile.toString());

        assertEquals(2, movies.size());
        assertEquals("Spider Man", movies.get(0).getTitle());
        assertEquals("SM123", movies.get(0).getId());
        assertEquals("Toy Story", movies.get(1).getTitle());
        assertEquals("TS456", movies.get(1).getId());
    }

    @Test
    public void testInvalidMovieTitleFailFast() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");

        Files.writeString(
                moviesFile,
                "spider Man,SM123\n" +
                "Action,Adventure\n"
        );

        InputReader reader = new InputReader();

        Exception exception = assertThrows(
                Exception.class,
                () -> reader.readMovies(moviesFile.toString())
        );

        assertEquals("Movie Title ERROR: spider Man is wrong", exception.getMessage());
    }

    @Test
    public void testInvalidMovieIdFailFast() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SP123\n" +
                "Action,Adventure\n"
        );

        InputReader reader = new InputReader();

        Exception exception = assertThrows(
                Exception.class,
                () -> reader.readMovies(moviesFile.toString())
        );

        assertEquals("Movie Id letters ERROR: SP123 are wrong", exception.getMessage());
    }

    @Test
    public void testReadValidUsersFile() throws Exception {
        Path usersFile = tempDir.resolve("users.txt");

        Files.writeString(
                usersFile,
                "Ahmed Ali,123456789\n" +
                "Action,Comedy\n" +
                "Mona Hassan,987654321\n" +
                "SciFi\n"
        );

        InputReader reader = new InputReader();
        List<User> users = reader.readUsers(usersFile.toString());

        assertEquals(2, users.size());
        assertEquals("Ahmed Ali", users.get(0).getUsername());
        assertEquals("123456789", users.get(0).getUserId());
        assertEquals("Mona Hassan", users.get(1).getUsername());
        assertEquals("987654321", users.get(1).getUserId());
    }

    @Test
    public void testInvalidUsernameFailFast() throws Exception {
        Path usersFile = tempDir.resolve("users.txt");

        Files.writeString(
                usersFile,
                " Ahmed Ali,123456789\n" +
                "Action,Comedy\n"
        );

        InputReader reader = new InputReader();

        Exception exception = assertThrows(
                Exception.class,
                () -> reader.readUsers(usersFile.toString())
        );

        assertEquals("Username ERROR:  Ahmed Ali is wrong", exception.getMessage());
    }

    @Test
    public void testInvalidUserIdFailFast() throws Exception {
        Path usersFile = tempDir.resolve("users.txt");

        Files.writeString(
                usersFile,
                "Ahmed Ali,A23456789\n" +
                "Action,Comedy\n"
        );

        InputReader reader = new InputReader();

        Exception exception = assertThrows(
                Exception.class,
                () -> reader.readUsers(usersFile.toString())
        );

        assertEquals("User Id ERROR: A23456789 is wrong", exception.getMessage());
    }

    @Test
    public void testFullValidSystemPipeline() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");
        Path outputFile = tempDir.resolve("recommendations.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SM123\n" +
                "Action,Adventure\n" +
                "Toy Story,TS456\n" +
                "Animation,Comedy\n" +
                "The Matrix,TM789\n" +
                "Action,SciFi\n"
        );

        Files.writeString(
                usersFile,
                "Ahmed Ali,123456789\n" +
                "Action,Comedy\n" +
                "Mona Hassan,987654321\n" +
                "SciFi\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        List<Movie> movies = reader.readMovies(moviesFile.toString());
        List<User> users = reader.readUsers(usersFile.toString());

        String recommendations = engine.generateRecommendations(movies, users);
        writer.writeRecommendations(recommendations, outputFile.toString());

        String output = Files.readString(outputFile);

        assertTrue(output.contains("For User: Ahmed Ali, 123456789"));
        assertTrue(output.contains("Action: SM123-Spider Man, TM789-The Matrix"));
        assertTrue(output.contains("Comedy: TS456-Toy Story"));
        assertTrue(output.contains("For User: Mona Hassan, 987654321"));
        assertTrue(output.contains("SciFi: TM789-The Matrix"));
    }

    @Test
    public void testFullInvalidSystemPipelineFailFast() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");
        Path outputFile = tempDir.resolve("recommendations.txt");

        Files.writeString(
                moviesFile,
                "spider Man,SM123\n" +
                "Action,Adventure\n" +
                "Toy Story,TS456\n" +
                "Animation,Comedy\n"
        );

        Files.writeString(
                usersFile,
                "Ahmed Ali,123456789\n" +
                "Action,Comedy\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        try {
            List<Movie> movies = reader.readMovies(moviesFile.toString());
            List<User> users = reader.readUsers(usersFile.toString());
            String recommendations = engine.generateRecommendations(movies, users);
            writer.writeRecommendations(recommendations, outputFile.toString());
            fail("Expected validation error was not thrown.");
        } catch (Exception e) {
            writer.writeError(e.getMessage(), outputFile.toString());
        }

        String output = Files.readString(outputFile);

        assertEquals("Movie Title ERROR: spider Man is wrong", output);
        assertFalse(output.contains("Ahmed Ali"));
        assertFalse(output.contains("Toy Story"));
    }
}
