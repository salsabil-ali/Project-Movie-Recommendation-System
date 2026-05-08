package wbt.outputwritertests;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import outputwriter.OutputWriter;

public class OutputWriterTest {

    OutputWriter writer;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        writer = new OutputWriter();
    }

    // Statement coverage: every line in writeRecommendations() executes
    @Test
    void writeRecommendations_writesContentToFile() throws Exception {
        Path output = tempDir.resolve("recommendations.txt");
        writer.writeRecommendations("some content", output.toString());
        assertEquals("some content", Files.readString(output));
    }

    // Statement coverage: every line in writeError() executes
    @Test
    void writeError_writesErrorMessageToFile() throws Exception {
        Path output = tempDir.resolve("recommendations.txt");
        writer.writeError("some error", output.toString());
        assertEquals("some error", Files.readString(output));
    }
}