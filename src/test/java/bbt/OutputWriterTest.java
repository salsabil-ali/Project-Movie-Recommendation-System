package bbt;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import outputwriter.OutputWriter;

class OutputWriterTest {

    @Test
    void testWriteRecommendations_Success() throws Exception {
        File tempFile = File.createTempFile("test_output", ".txt");
        String path = tempFile.getAbsolutePath();
        String content = "For User: Nouran, 123\nAction: I123-Inception\n";

        OutputWriter writer = new OutputWriter();

        writer.writeRecommendations(content, path);

        String actualContent = Files.readString(tempFile.toPath());
        assertEquals(content, actualContent, "The file content should match the input string.");

        tempFile.delete();
    }

    @Test
    void testWriteError_Success() throws Exception {
        File tempFile = File.createTempFile("test_error", ".txt");
        String path = tempFile.getAbsolutePath();
        String errorMessage = "Error: Input file 'movies.txt' not found.";

        OutputWriter writer = new OutputWriter();

        writer.writeError(errorMessage, path);

        String actualContent = Files.readString(tempFile.toPath());
        assertEquals(errorMessage, actualContent, "The error message written to the file is incorrect.");

        tempFile.delete();
    }
}