/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salsabil.main;

/**
 *
 * @author Salsbil
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class RecommendationEngine {

    // ─────────────────────────────────────────
    // GENERATE RECOMMENDATIONS
    // ─────────────────────────────────────────
    public void generateRecommendations(List<Movie> movies, 
                                        List<User> users, 
                                        String outputPath) throws Exception {

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

        for (User user : users) {

            // LINE 1: "For User: username, userId"
            writer.write("For User: " + user.getUsername() 
                         + ", " + user.getUserId());
            writer.newLine();

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

                // Only write the line if we found matching movies
                if (foundAny) {
                    writer.write(line.toString());
                    writer.newLine();
                }
            }

            writer.newLine(); // blank line between users
        }

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