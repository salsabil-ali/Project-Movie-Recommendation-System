
package OutputWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class OutputWriter {

    // ─────────────────────────────────────────
    // WRITE RECOMMENDATIONS TO OUTPUT FILE
    // ─────────────────────────────────────────
    public void writeRecommendations(String content,
                                     String outputPath) throws Exception {

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        writer.write(content);
        writer.close();
    }

    // ─────────────────────────────────────────
    // WRITE ERROR TO OUTPUT FILE
    // ─────────────────────────────────────────
    public void writeError(String errorMessage,
                           String outputPath) throws Exception {

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        writer.write(errorMessage);
        writer.close();
    }
}
