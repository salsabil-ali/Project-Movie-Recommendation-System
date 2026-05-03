package main;

import constructors.Movie;
import constructors.User;
import inputreader.InputReader;
import java.util.List;
import recommendation.RecommendationEngine;

public class Main {

    public static void main(String[] args) {

        String moviesFile = "resources/movies.txt";
        String usersFile = "resources/users.txt";
        String outputFile = "resources/recommendations.txt";

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
