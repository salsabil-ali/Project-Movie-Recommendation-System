/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package bbt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import validator.Validator;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Salsbil
 */


class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    // Testing Movie titles

    // Partioning (Valid Titles)
    @Test
    void testValidateMovieTitle_Valid() {
        assertNull(validator.validateMovieTitle("The Matrix"));
        assertNull(validator.validateMovieTitle("Inception"));
        assertNull(validator.validateMovieTitle("A Beautiful Mind"));
    }

    // Partioning (Invalid Titles)
    @Test
    void testValidateMovieTitle_Invalid() {
        assertNotNull(validator.validateMovieTitle("the Matrix"));
        assertNotNull(validator.validateMovieTitle("The matrix"));
        assertNotNull(validator.validateMovieTitle("The  Matrix"));
    }


    // Partioning (Valid IDs)
    @Test
    void testValidateMovieId_Valid() {
        assertNull(validator.validateMovieId("TM123", "The Matrix"));
    }

    // Partioning (Invalid IDs)
    @Test
    void testValidateMovieId_WrongLetters() {
        assertNotNull(validator.validateMovieId("TX123", "The Matrix"));
    }

    @Test
    void testValidateMovieId_InvalidDigits() {
        assertNotNull(validator.validateMovieId("TM12", "The Matrix"));
        assertNotNull(validator.validateMovieId("TMA123", "The Matrix"));
        assertNotNull(validator.validateMovieId("TM1234", "The Matrix"));
    }

    @Test
    void testValidateMovieId_Uniqueness() {
        validator.validateMovieId("TM123", "The Matrix");
        assertNotNull(validator.validateMovieId("TS123", "Toy Story"));
    }


    // Testing User Names

    // Valid User Names
    @Test
    void testValidateUsername_Valid() {
        assertNull(validator.validateUsername("John Doe"));
        assertNull(validator.validateUsername("Alice"));
    }

    // Invalid User Names
    @Test
    void testValidateUsername_Invalid() {
        assertNotNull(validator.validateUsername(" John"));
        assertNotNull(validator.validateUsername("John123"));
        assertNotNull(validator.validateUsername("John_Doe"));
    }

    // Testing User IDs

    // Valid IDs
    @Test
    void testValidateUserId_Valid() {
        assertNull(validator.validateUserId("123456789"));
        assertNull(validator.validateUserId("12345678A"));
    }

    // Invalid IDs
    @Test
    void testValidateUserId_Length() {
        assertNotNull(validator.validateUserId("12345678"));
        assertNotNull(validator.validateUserId("1234567890"));
    }

    @Test
    void testValidateUserId_StartChar() {
        assertNotNull(validator.validateUserId("A23456789"));
    }

    @Test
    void testValidateUserId_TrailingLetters() {
        assertNotNull(validator.validateUserId("1234567AB"));
    }

    @Test
    void testValidateUserId_SpecialChars() {
        assertNotNull(validator.validateUserId("12345678!"));
    }

    @Test
    void testValidateUserId_Uniqueness() {
        validator.validateUserId("12345678A");
        assertNotNull(validator.validateUserId("12345678A")); // Duplicate
    }
}