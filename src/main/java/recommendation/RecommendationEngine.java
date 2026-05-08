package recommendation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import constructors.Movie;
import constructors.User;

public class RecommendationEngine {

    // ─────────────────────────────────────────
    // GENERATE RECOMMENDATIONS (returns content as String)
    // ─────────────────────────────────────────
    public void writeError(String message, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(message);
        }
    }

    public String generateRecommendations(List<Movie> movies, List<User> users) throws IOException {
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

                if (foundAny) {
                    output.append(line.toString()).append("\n");
                }
            }

            output.append("\n");
        }

        return output.toString();
    }

    public void generateRecommendations(List<Movie> movies, List<User> users, String outputPath) throws IOException {
        String content = generateRecommendations(movies, users);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(content);
        }
    }
}