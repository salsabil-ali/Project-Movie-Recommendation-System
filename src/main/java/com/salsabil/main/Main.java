/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.salsabil.main;

/**
 *
 * @author Salsbil
 */
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String moviesFile = "movies.txt";
        String usersFile = "users.txt";
        String outputFile = "recommendations.txt";

        InputReader reader = new InputReader(); 
        RecommendationEngine engine = new RecommendationEngine();

        try {

            // Reading movies
            System.out.println("Reading movies...");
            List<Movie> movies = reader.readMovies(moviesFile);
            System.out.println("Movies loaded: " + movies.size());

            // Reading users
            System.out.println("Reading users...");
            List<User> users = reader.readUsers(usersFile);
            System.out.println("Users loaded: " + users.size());

            // Generating recommendations
            System.out.println("Generating recommendations...");
            engine.generateRecommendations(movies, users, outputFile);
            System.out.println("Done! Check " + outputFile);

        } catch (Exception e) {

            System.out.println("Error found: " + e.getMessage());
            try {
                engine.writeError(e.getMessage(), outputFile);
            } catch (Exception ex) {
                System.out.println("Could not write error to file: " 
                                   + ex.getMessage());
            }
        }
    }
}