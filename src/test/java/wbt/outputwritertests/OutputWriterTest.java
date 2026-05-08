package wbt.outputwritertests;

import OutputWriter.OutputWriter;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class OutputWriterTest {

    private OutputWriter outputWriter;
    private String tempFilePath;

    // ─────────────────────────────────────────
    // SETUP & TEARDOWN
    // ─────────────────────────────────────────

    @BeforeEach
    void setUp() throws Exception {
        outputWriter = new OutputWriter();
        // Creates a real temporary file before each test
        File tempFile = File.createTempFile("output_test", ".txt");
        tempFilePath = tempFile.getAbsolutePath();
    }

    @AfterEach
    void tearDown() {
        // Deletes the temp file after each test to keep things clean
        File f = new File(tempFilePath);
        if (f.exists()) f.delete();
    }


    // ══════════════════════════════════════════
    // TESTS FOR writeRecommendations()
    // ══════════════════════════════════════════


    // Covers: BufferedWriter creation, writer.write(), writer.close()
    // This is the core happy path — all 4 lines inside the method execute.
    @Test
    void testWriteRecommendations_writesContentToFile() throws Exception {
        String content = "For User: Alice,123456789\naction: ACT001-Inception";

        outputWriter.writeRecommendations(content, tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals(content, written);
        System.out.println("Expected: [" + content + "]");
        System.out.println("Actual:   [" + written + "]");
    }


    // Verifies that write() handles empty string without throwing,
    // and that the file ends up empty (not null, not skipped).
    @Test
    void testWriteRecommendations_emptyContent_writesEmptyFile() throws Exception {
        outputWriter.writeRecommendations("", tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals("", written);
    }


    // Confirms the method does NOT trim or alter the content in any way.
    @Test
    void testWriteRecommendations_multilineContent_preservedExactly() throws Exception {
        String content = "For User: Bob,987654321\nhorror: HOR001-TheShining\ndrama: DRA001-Forrest Gump";

        outputWriter.writeRecommendations(content, tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals(content, written);
    }



    // Internal path: FileWriter opens fine, but write(null) is called.
    // BufferedWriter.write(null) throws a NullPointerException internally.
    @Test
    void testWriteRecommendations_nullContent_throwsException() {
        assertThrows(Exception.class, () -> {
            outputWriter.writeRecommendations(null, tempFilePath);
        });
    }




    // ══════════════════════════════════════════
    // TESTS FOR writeError()
    // ══════════════════════════════════════════


   
    // Confirms the error message is written to the file exactly as passed.
    @Test
    void testWriteError_writesErrorMessageToFile() throws Exception {
        String errorMessage = "Movie Title ERROR: the dark knight is wrong";

        outputWriter.writeError(errorMessage, tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals(errorMessage, written);
    }


    // check specific error format 
    @Test
    void testWriteError_movieIdLettersError_writtenExactly() throws Exception {
        String errorMessage = "Movie Id letters ERROR: TDK001 are wrong";

        outputWriter.writeError(errorMessage, tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals(errorMessage, written);
    }


    // Ensures write() does not treat different error message formats differently.
    @Test
    void testWriteError_userIdError_writtenExactly() throws Exception {
        String errorMessage = "User Id ERROR: 12345678A is wrong";

        outputWriter.writeError(errorMessage, tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals(errorMessage, written);
    }

    // Covers: writer.write(null) inside writeError()
    @Test
    void testWriteError_nullMessage_throwsException() {
        assertThrows(Exception.class, () -> {
            outputWriter.writeError(null, tempFilePath);
        });
    }

    // Covers: writeRecommendations() then writeError() on the SAME file path
    // This tests that FileWriter(outputPath) OVERWRITES the file, not appends.
    // Internally, FileWriter is constructed with append=false by default,
    // so the second call should erase what the first wrote.
    @Test
    void testWriteError_overwritesPreviousContent() throws Exception {
        outputWriter.writeRecommendations("old recommendations", tempFilePath);
        outputWriter.writeError("Movie Title ERROR: bad title is wrong", tempFilePath);

        String written = Files.readString(Path.of(tempFilePath));
        assertEquals("Movie Title ERROR: bad title is wrong", written);
        assertFalse(written.contains("old recommendations"));
    }
}