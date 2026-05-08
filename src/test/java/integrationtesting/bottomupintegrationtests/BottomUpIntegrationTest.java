package integrationtesting.bottomupintegrationtests;

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

public class BottomUpIntegrationTest {

    @TempDir
    Path tempDir;

    /*
     * BU-01
     * Full valid integration:
     * InputReader + Validator + Movie + User + RecommendationEngine + OutputWriter
     */
    @Test
    public void testBottomUpFullValidSystemIntegration() throws Exception {
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
                "Action,Comedy\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        List<Movie> movies = reader.readMovies(moviesFile.toString());
        List<User> users = reader.readUsers(usersFile.toString());

        String result = engine.generateRecommendations(movies, users);

        writer.writeRecommendations(result, outputFile.toString());

        String output = Files.readString(outputFile);

        assertTrue(output.contains("For User: Ahmed Ali, 123456789"));
        assertTrue(output.contains("Action: SM123-Spider Man, TM789-The Matrix"));
        assertTrue(output.contains("Comedy: TS456-Toy Story"));
    }

    /*
     * BU-02
     * Full valid integration with multiple users.
     */
    @Test
    public void testBottomUpFullSystemWithMultipleUsers() throws Exception {
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

        String result = engine.generateRecommendations(movies, users);

        writer.writeRecommendations(result, outputFile.toString());

        String output = Files.readString(outputFile);

        assertTrue(output.contains("For User: Ahmed Ali, 123456789"));
        assertTrue(output.contains("Action: SM123-Spider Man, TM789-The Matrix"));
        assertTrue(output.contains("Comedy: TS456-Toy Story"));

        assertTrue(output.contains("For User: Mona Hassan, 987654321"));
        assertTrue(output.contains("SciFi: TM789-The Matrix"));
    }

    /*
     * BU-03
     * Integration when user likes a category that has no matching movie.
     */
    @Test
    public void testBottomUpNoMatchingCategory() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");
        Path outputFile = tempDir.resolve("recommendations.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SM123\n" +
                "Action,Adventure\n" +
                "Toy Story,TS456\n" +
                "Animation,Comedy\n"
        );

        Files.writeString(
                usersFile,
                "Ahmed Ali,123456789\n" +
                "Horror\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        List<Movie> movies = reader.readMovies(moviesFile.toString());
        List<User> users = reader.readUsers(usersFile.toString());

        String result = engine.generateRecommendations(movies, users);

        writer.writeRecommendations(result, outputFile.toString());

        String output = Files.readString(outputFile);

        assertTrue(output.contains("For User: Ahmed Ali, 123456789"));
        assertFalse(output.contains("Horror:"));
    }

    /*
     * BU-04
     * Integration fail-fast when movie title is invalid.
     */
    @Test
    public void testBottomUpInvalidMovieTitleFailFast() throws Exception {
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
                "Action\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        try {
            List<Movie> movies = reader.readMovies(moviesFile.toString());
            List<User> users = reader.readUsers(usersFile.toString());

            String result = engine.generateRecommendations(movies, users);

            writer.writeRecommendations(result, outputFile.toString());

            fail("Expected movie title validation error was not thrown.");
        } catch (Exception e) {
            writer.writeError(e.getMessage(), outputFile.toString());
        }

        String output = Files.readString(outputFile);

        assertEquals("Movie Title ERROR: spider Man is wrong", output);
        assertFalse(output.contains("Ahmed Ali"));
        assertFalse(output.contains("Toy Story"));
    }

    /*
     * BU-05
     * Integration fail-fast when movie ID is invalid.
     */
    @Test
    public void testBottomUpInvalidMovieIdFailFast() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");
        Path outputFile = tempDir.resolve("recommendations.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SP123\n" +
                "Action,Adventure\n"
        );

        InputReader reader = new InputReader();
        OutputWriter writer = new OutputWriter();

        try {
            reader.readMovies(moviesFile.toString());

            fail("Expected movie ID validation error was not thrown.");
        } catch (Exception e) {
            writer.writeError(e.getMessage(), outputFile.toString());
        }

        String output = Files.readString(outputFile);

        assertEquals("Movie Id letters ERROR: SP123 are wrong", output);
    }

    /*
     * BU-06
     * Integration fail-fast when username is invalid.
     */
    @Test
    public void testBottomUpInvalidUsernameFailFast() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");
        Path outputFile = tempDir.resolve("recommendations.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SM123\n" +
                "Action,Adventure\n"
        );

        Files.writeString(
                usersFile,
                " Ahmed Ali,123456789\n" +
                "Action\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        try {
            List<Movie> movies = reader.readMovies(moviesFile.toString());
            List<User> users = reader.readUsers(usersFile.toString());

            String result = engine.generateRecommendations(movies, users);

            writer.writeRecommendations(result, outputFile.toString());

            fail("Expected username validation error was not thrown.");
        } catch (Exception e) {
            writer.writeError(e.getMessage(), outputFile.toString());
        }

        String output = Files.readString(outputFile);

        assertEquals("Username ERROR:  Ahmed Ali is wrong", output);
        assertFalse(output.contains("Action: SM123-Spider Man"));
    }

    /*
     * BU-07
     * Integration fail-fast when user ID is invalid.
     */
    @Test
    public void testBottomUpInvalidUserIdFailFast() throws Exception {
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");
        Path outputFile = tempDir.resolve("recommendations.txt");

        Files.writeString(
                moviesFile,
                "Spider Man,SM123\n" +
                "Action,Adventure\n"
        );

        Files.writeString(
                usersFile,
                "Ahmed Ali,A23456789\n" +
                "Action\n"
        );

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        try {
            List<Movie> movies = reader.readMovies(moviesFile.toString());
            List<User> users = reader.readUsers(usersFile.toString());

            String result = engine.generateRecommendations(movies, users);

            writer.writeRecommendations(result, outputFile.toString());

            fail("Expected user ID validation error was not thrown.");
        } catch (Exception e) {
            writer.writeError(e.getMessage(), outputFile.toString());
        }

        String output = Files.readString(outputFile);

        assertEquals("User Id ERROR: A23456789 is wrong", output);
        assertFalse(output.contains("Action: SM123-Spider Man"));
    }
}