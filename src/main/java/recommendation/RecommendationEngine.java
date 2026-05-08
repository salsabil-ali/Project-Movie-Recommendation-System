/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recommendation;

/**
 *
 * @author Salsbil
 */
import java.util.List;

import constructors.Movie;
import constructors.User;

public class RecommendationEngine {

    // ─────────────────────────────────────────
    // GENERATE RECOMMENDATIONS (returns content as String)
    // ─────────────────────────────────────────
    public String generateRecommendations(List<Movie> movies, List<User> users) {

        StringBuilder output = new StringBuilder();

        for (User user : users) {

            // LINE 1: "For User: username, userId"
            output.append("For User: ")
                  .append(user.getUsername())
                  .append(", ")
                  .append(user.getUserId())
                  .append("\n");

            // For each category the user likes
            for (String likedCategory : user.getLikedCategories()) {

                StringBuilder line = new StringBuilder();
                line.append(likedCategory.trim()).append(":");

                boolean foundAny = false;

                // Find all movies that belong to this category
                for (Movie movie : movies) {
                    for (String movieCategory : movie.getCategories()) {
                        if (movieCategory.trim()
                                .equalsIgnoreCase(likedCategory.trim())) {

                            if (foundAny) {
                                line.append(",");
                            }

                            line.append(" ")
                                .append(movie.getId())
                                .append("-")
                                .append(movie.getTitle());

                            foundAny = true;
                            break;
                        }
                    }
                }

                // Only append the line if we found matching movies
                if (foundAny) {
                    output.append(line.toString()).append("\n");
                }
            }

            output.append("\n"); // blank line between users
        }

        return output.toString();
    }
}