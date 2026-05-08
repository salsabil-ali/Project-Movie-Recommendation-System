package main;

import java.util.List;

import constructors.Movie;
import constructors.User;
import inputreader.InputReader;
import outputwriter.OutputWriter;
import recommendation.RecommendationEngine;

public class Main {

    public static void main(String[] args) {

        String moviesFile = "resources/movies.txt";
        String usersFile  = "resources/users.txt";
        String outputFile = "resources/recommendations.txt";

        InputReader          reader = new InputReader();
        RecommendationEngine engine = new RecommendationEngine();
        OutputWriter         writer = new OutputWriter();

        try {

            System.out.println("Reading movies...");
            List<Movie> movies = reader.readMovies(moviesFile);
            System.out.println("Movies loaded: " + movies.size());

            System.out.println("Reading users...");
            List<User> users = reader.readUsers(usersFile);
            System.out.println("Users loaded: " + users.size());

            System.out.println("Generating recommendations...");
            engine.generateRecommendations(movies, users, outputFile);

            System.out.println("Done! Check " + outputFile);

        } catch (Exception e) {

            System.out.println("Error found: " + e.getMessage());
            try {
                writer.writeError(e.getMessage(), outputFile);
            } catch (Exception ex) {
                System.out.println("Could not write error to file: " + ex.getMessage());
            }
        }
    }
}
