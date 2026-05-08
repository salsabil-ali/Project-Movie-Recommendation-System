package wbt.outputwritertests;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import outputwriter.OutputWriter;

// This test case is the same as the BBT test case.
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
        File output = tempDir.resolve("recommendations.txt").toFile();
        writer.writeRecommendations("some content", output.getAbsolutePath());
        assertEquals("some content", Files.readString(output.toPath()));
    }

    // Statement coverage: every line in writeError() executes
    @Test
    void writeError_writesErrorMessageToFile() throws Exception {
        File output = tempDir.resolve("recommendations.txt").toFile();
        writer.writeError("some error", output.getAbsolutePath());
        assertEquals("some error", Files.readString(output.toPath()));
    }
}
