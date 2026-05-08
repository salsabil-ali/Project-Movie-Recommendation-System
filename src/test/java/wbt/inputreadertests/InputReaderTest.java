package wbt.inputreadertests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import constructors.Movie;
import constructors.User;
import inputreader.InputReader;

public class InputReaderTest {

    InputReader reader;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        reader = new InputReader();
    }

    private String write(String filename, String content) throws IOException { // helper method to write test files
        Path f = tempDir.resolve(filename);
        Files.writeString(f, content);
        return f.toString();
    }

    // ─────────────────────────────────────────
    // readMovies()
    // ─────────────────────────────────────────

    @Test
    void readMovies_emptyFile_returnsEmptyList() throws Exception {
        // Statement coverage: while loop never entered
        String path = write("movies.txt", "");
        assertTrue(reader.readMovies(path).isEmpty());
    }

    @Test
    void readMovies_blankLine_isSkipped() throws Exception {
        // Branch coverage: line.trim().isEmpty() = true → continue taken
        String path = write("movies.txt",
                "\n" +
                        "The Dark Knight, TDK001\n" +
                        "Action\n");
        assertEquals(1, reader.readMovies(path).size());
    }

    @Test
    void readMovies_invalidTitle_throwsException() throws Exception {
        // Branch coverage: titleError != null → exception thrown, reader closed
        String path = write("movies.txt",
                "the Dark Knight, TDK001\n" +
                        "Action\n");
        assertThrows(Exception.class, () -> reader.readMovies(path));
    }

    @Test
    void readMovies_invalidId_throwsException() throws Exception {
        // Branch coverage: idError != null → exception thrown, reader closed
        String path = write("movies.txt",
                "The Dark Knight, XYZ001\n" +
                        "Action\n");
        assertThrows(Exception.class, () -> reader.readMovies(path));
    }

    @Test
    void readMovies_validMovie_fieldsCorrect() throws Exception {
        // Branch coverage: valid path — movie added with correct title, id, categories
        String path = write("movies.txt",
                "The Dark Knight, TDK001\n" +
                        "Action\n");
        List<Movie> movies = reader.readMovies(path);
        assertEquals(1, movies.size());
        assertEquals("The Dark Knight", movies.get(0).getTitle());
        assertEquals("TDK001", movies.get(0).getId());
        assertEquals(List.of("Action"), movies.get(0).getCategories());
    }

    @Test
    void readMovies_multipleCategories_allParsed() throws Exception {
        // Statement coverage: categories split by comma, trimmed, all stored
        String path = write("movies.txt",
                "The Dark Knight, TDK001\n" +
                        "Action, Drama\n");
        List<String> cats = reader.readMovies(path).get(0).getCategories();
        assertEquals(2, cats.size());
        assertTrue(cats.contains("Action"));
        assertTrue(cats.contains("Drama"));
    }

    @Test
    void readMovies_threeValidMovies_allLoaded() throws Exception {
        // Path coverage: loop runs 3 times, all valid, all added
        String path = write("movies.txt",
                "The Dark Knight, TDK001\n" +
                        "Action\n" +
                        "Catch Me If You Can, CMIYC002\n" +
                        "Crime\n" +
                        "It, I003\n" +
                        "Horror\n");
        assertEquals(3, reader.readMovies(path).size());
    }

    // ─────────────────────────────────────────
    // readUsers()
    // ─────────────────────────────────────────

    @Test
    void readUsers_emptyFile_returnsEmptyList() throws Exception {
        // Statement: while loop never entered
        String path = write("users.txt", "");
        assertTrue(reader.readUsers(path).isEmpty());
    }

    @Test
    void readUsers_blankLine_isSkipped() throws Exception {
        // Branch: line.trim().isEmpty() = true → continue taken
        String path = write("users.txt",
                "\n" +
                        "Sara Khaled, 12345678A\n" +
                        "Action\n");
        assertEquals(1, reader.readUsers(path).size());
    }

    @Test
    void readUsers_leadingSpaceInUsername_throwsException() throws Exception {
        // Branch: usernameError != null → exception thrown
        // Condition: username NOT trimmed before validation
        String path = write("users.txt",
                " Sara Khaled, 12345678A\n" +
                        "Action\n");
        assertThrows(Exception.class, () -> reader.readUsers(path));
    }

    @Test
    void readUsers_usernameTrimmedAfterValidation() throws Exception {
        // Condition: trim() called on username only after validateUsername() passes
        String path = write("users.txt",
                "Sara Khaled, 12345678A\n" +
                        "Action\n");
        // No leading space → valid → trimmed → stored without extra whitespace
        assertEquals("Sara Khaled", reader.readUsers(path).get(0).getUsername());
    }

    @Test
    void readUsers_invalidUserId_throwsException() throws Exception {
        // Branch: userIdError != null → exception thrown
        String path = write("users.txt",
                "Sara Khaled, 1234567AB\n" +
                        "Action\n");
        assertThrows(Exception.class, () -> reader.readUsers(path));
    }

    @Test
    void readUsers_validUser_fieldsCorrect() throws Exception {
        // Statement: valid path — user added with correct name, id, categories
        String path = write("users.txt",
                "Sara Khaled, 12345678A\n" +
                        "Action, Drama\n");
        List<User> users = reader.readUsers(path);
        assertEquals(1, users.size());
        assertEquals("Sara Khaled", users.get(0).getUsername());
        assertEquals("12345678A", users.get(0).getUserId());
        assertEquals(List.of("Action", "Drama"), users.get(0).getLikedCategories());
    }

    @Test
    void readUsers_twoValidUsers_allLoaded() throws Exception {
        // Path: loop runs twice, both valid, both added
        String path = write("users.txt",
                "Sara Khaled, 12345678A\n" +
                        "Action, Drama\n" +
                        "Mariam Umar, 987654321\n" +
                        "Crime\n");
        assertEquals(2, reader.readUsers(path).size());
    }

}
