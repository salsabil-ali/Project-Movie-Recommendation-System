package recommendation;

import constructors.Movie;
import constructors.User;
import OutputWriter.OutputWriter;
import java.util.List;

public class RecommendationEngine {

    private OutputWriter outputWriter = new OutputWriter();

    // ─────────────────────────────────────────
    // GENERATE RECOMMENDATIONS
    // ─────────────────────────────────────────
    public String generateRecommendations(List<Movie> movies, List<User> users, String outputPath) throws Exception {

        StringBuilder output = new StringBuilder();

        for (User user : users) {
            output.append("For User: ")
                  .append(user.getUsername())
                  .append(", ")
                  .append(user.getUserId())
                  .append("\n");

            for (String likedCategory : user.getLikedCategories()) {
                StringBuilder line = new StringBuilder();
                line.append(likedCategory.trim()).append(":");
                boolean foundAny = false;

                for (Movie movie : movies) {
                    for (String movieCategory : movie.getCategories()) {
                        if (movieCategory.trim().equalsIgnoreCase(likedCategory.trim())) {
                            if (foundAny) line.append(",");
                            line.append(" ").append(movie.getId()).append("-").append(movie.getTitle());
                            foundAny = true;
                            break;
                        }
                    }
                }

                if (foundAny) output.append(line.toString()).append("\n");
            }
            output.append("\n");
        }

        String result = output.toString();
        outputWriter.writeRecommendations(result, outputPath);
        return result;
    }

    // ─────────────────────────────────────────
    // WRITE ERROR
    // ─────────────────────────────────────────
    public void writeError(String errorMessage, String outputPath) throws Exception {
        outputWriter.writeError(errorMessage, outputPath);
    }
}