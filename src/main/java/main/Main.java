package main;

import constructors.Movie;
import constructors.User;
import inputreader.InputReader;
import java.util.List;
import recommendation.RecommendationEngine;
import OutputWriter.OutputWriter; // Matches your package name

public class Main {

    public static void main(String[] args) {
        String moviesFile = "src/main/java/resources/movies.txt";
        String usersFile  = "src/main/java/resources/users.txt";
        String outputFile = "src/main/java/resources/recommendations.txt";

        InputReader reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter writer = new OutputWriter();

        try {

            System.out.println("Reading movies...");
            List<Movie> movies = reader.readMovies(moviesFile);

            System.out.println("Reading users...");
            List<User> users = reader.readUsers(usersFile);

            System.out.println("Generating recommendations...");
            String content = engine.generateRecommendations(movies, users);

            writer.writeRecommendations(content, outputFile);
            System.out.println("Success! Results saved to: " + outputFile);

        } catch (Exception e) {

            System.err.println("Process failed: " + e.getMessage());
            try {
                writer.writeError("Error during execution: " + e.getMessage(), outputFile);
                System.out.println("Error details written to: " + outputFile);
            } catch (Exception ex) {
                System.err.println("Critical Failure: Could not write error log. " + ex.getMessage());
            }
        }
    }
}