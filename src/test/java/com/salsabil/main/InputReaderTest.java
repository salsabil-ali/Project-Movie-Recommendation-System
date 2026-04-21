/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.salsabil.main;

import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Salsbil
 */
public class InputReaderTest {
    
    private InputReader instance;

    @TempDir
    Path tempDir; // Automatically creates and cleans up temporary test files

    @BeforeEach
    public void setUp() {
        instance = new InputReader();
    }

    /**
     * Test Case 1: Entering a valid name and valid ID [cite: 1]
     * Requirement 2.2: Username (alpha/spaces), ID (9 alphanumeric, starts with numbers) [cite: 65, 66]
     */
    @Test
    public void testReadUsersValid() throws Exception {
        File tempUsers = tempDir.resolve("users.txt").toFile();
        try (PrintWriter out = new PrintWriter(tempUsers)) {
            out.println("Nour Khaled, 12345678A"); // Line 1: username, user_id [cite: 75]
            out.println("action, horror");         // Line 2: liked_categories [cite: 76]
        }

        List<User> result = instance.readUsers(tempUsers.getAbsolutePath());
        assertEquals(1, result.size());
        assertEquals("Nour Khaled", result.get(0).getUsername());
        assertEquals("12345678A", result.get(0).getUserId());
    }

    /**
     * Test Case 2: Username starting with a space [cite: 2]
     * Requirement: Username must not begin with a space [cite: 65]
     */
    @Test
    public void testReadUsersInvalidUsername() throws Exception {
        File tempUsers = tempDir.resolve("invalid_users.txt").toFile();
        try (PrintWriter out = new PrintWriter(tempUsers)) {
            out.println(" Nour Khaled, 12345678A");
            out.println("action");
        }

        Exception exception = assertThrows(Exception.class, () -> {
            instance.readUsers(tempUsers.getAbsolutePath());
        });
        
        // Requirement 5: Error message format [cite: 89]
        assertTrue(exception.getMessage().contains("Username ERROR:  Nour Khaled is wrong"));
    }

    /**
     * Test Case 25: Movie Title starts with lowercase [cite: 30]
     * Requirement 2.1: Every word must start with a capital letter [cite: 61]
     */
    @Test
    public void testReadMoviesInvalidTitle() throws Exception {
        File tempMovies = tempDir.resolve("invalid_movies.txt").toFile();
        try (PrintWriter out = new PrintWriter(tempMovies)) {
            out.println("inception, I123"); // Line 1: movie_title, movie_id [cite: 70]
            out.println("action");          // Line 2: category [cite: 71]
        }

        Exception exception = assertThrows(Exception.class, () -> {
            instance.readMovies(tempMovies.getAbsolutePath());
        });

        // Requirement 5: Error message format 
        assertEquals("Movie Title ERROR: inception is wrong", exception.getMessage());
    }

    /**
     * Test Case 13: ID greater than 9 characters [cite: 13]
     * Requirement 2.2: User ID must be exactly 9 characters [cite: 66]
     */
    @Test
    public void testReadUsersInvalidIdLength() throws Exception {
        File tempUsers = tempDir.resolve("long_id.txt").toFile();
        try (PrintWriter out = new PrintWriter(tempUsers)) {
            out.println("Nour Khaled, 123456789A");
            out.println("horror");
        }

        Exception exception = assertThrows(Exception.class, () -> {
            instance.readUsers(tempUsers.getAbsolutePath());
        });

        // Requirement 5: Error message format [cite: 90]
        assertEquals("User Id ERROR: 123456789A is wrong", exception.getMessage());
    }
}