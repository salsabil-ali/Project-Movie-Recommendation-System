/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salsabil.main;

/**
 *
 * @author Salsbil
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputReader {

    private Validator validator = new Validator();

    // ─────────────────────────────────────────
    // READ MOVIES FILE
    // ─────────────────────────────────────────
    public List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            // LINE 1: title,id
            String[] parts = line.split(",");
            String title = parts[0].trim();
            String id = parts[1].trim();

            // Validate title
            String titleError = validator.validateMovieTitle(title);
            if (titleError != null) {
                reader.close();
                throw new Exception(titleError);
            }

            // Validate movie ID
            String idError = validator.validateMovieId(id, title);
            if (idError != null) {
                reader.close();
                throw new Exception(idError);
            }

            // LINE 2: category1,category2,...
            String categoriesLine = reader.readLine();
            List<String> categories = new ArrayList<>(
                Arrays.asList(categoriesLine.split(","))
            );
            categories.replaceAll(String::trim);

            movies.add(new Movie(title, id, categories));
        }

        reader.close();
        return movies;
    }

    // ─────────────────────────────────────────
    // READ USERS FILE
    // ─────────────────────────────────────────
    public List<User> readUsers(String filePath) throws Exception {
        List<User> users = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            // LINE 1: username,userId
            String[] parts = line.split(",");

            // ─────────────────────────────────────────
            // FIX: Keep original username WITHOUT trim
            // so leading space is preserved for validation
            // ─────────────────────────────────────────
            String username = parts[0]; // ← no trim here
            String userId = parts[1].trim();

            // Validate username BEFORE trimming
            String usernameError = validator.validateUsername(username);
            if (usernameError != null) {
                reader.close();
                throw new Exception(usernameError);
            }

            // Trim AFTER validation passes
            username = username.trim();

            // Validate user ID
            String userIdError = validator.validateUserId(userId);
            if (userIdError != null) {
                reader.close();
                throw new Exception(userIdError);
            }

            // LINE 2: likedCategory1,likedCategory2,...
            String categoriesLine = reader.readLine();
            List<String> likedCategories = new ArrayList<>(
                Arrays.asList(categoriesLine.split(","))
            );
            likedCategories.replaceAll(String::trim);

            users.add(new User(username, userId, likedCategories));
        }

        reader.close();
        return users;
    }
}
