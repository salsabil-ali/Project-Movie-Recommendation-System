package wbt.validatortests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import validator.Validator;

public class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // A fresh Validator before each test to clear the used IDs sets
        validator = new Validator();
    }

    // ─────────────────────────────────────────
    // validateMovieTitle tests
    // ─────────────────────────────────────────

    @Test
    void movieTitle_allWordsCapitalized_returnsNull() {
        // Branch: all words pass → null
        assertNull(validator.validateMovieTitle("The Dark Knight"));
    }

    @Test
    void movieTitle_wordStartsWithLowercase_returnsError() {
        // Branch: first char is NOT uppercase → error
        String result = validator.validateMovieTitle("the Dark Knight");
        assertEquals("Movie Title ERROR: the Dark Knight is wrong", result);
    }

    @Test
    void movieTitle_wordIsEmpty_returnsError() {
        // Branch: word.isEmpty() → error
        // Double space creates an empty word after split
        String result = validator.validateMovieTitle("The  Knight");
        assertEquals("Movie Title ERROR: The  Knight is wrong", result);
    }

    @Test
    void movieTitle_singleCapitalizedWord_returnsNull() {
        //single word, valid->null
        assertNull(validator.validateMovieTitle("Titanic"));
    }

    // ─────────────────────────────────────────
    // validateMovieId tests
    // ─────────────────────────────────────────

    @Test
    void movieId_validId_returnsNull() {
        // Branch: everything passes → null
        assertNull(validator.validateMovieId("TDK001", "The Dark Knight"));
    }

    @Test
    void movieId_tooShort_splitIndexZeroOrNegative_returnsLettersError() {
        // Branch: splitIndex <= 0 (ID is 3 chars or less, no room for letters)
        String result = validator.validateMovieId("001", "Titanic");
        assertEquals("Movie Id letters ERROR: 001 are wrong", result);
    }

    @Test
    void movieId_lastThreeCharsNotDigits_returnsNumbersError() {
        // Branch: last 3 chars are not digits
        String result = validator.validateMovieId("TDKABC", "The Dark Knight");
        assertEquals("Movie Id numbers ERROR: TDKABC aren't unique", result);
    }

    @Test
    void movieId_lettersDoNotMatchTitleInitials_returnsLettersError() {
        // Branch: idLetters don't match expected initials
        String result = validator.validateMovieId("XYZ001", "The Dark Knight");
        assertEquals("Movie Id letters ERROR: XYZ001 are wrong", result);
    }

    @Test
    void movieId_duplicateNumbers_returnsNumbersError() {
        // Branch: digits already used by another movie
        validator.validateMovieId("TDK001", "The Dark Knight");  // register 001
        String result = validator.validateMovieId("T001", "Titanic");  // 001 already used
        assertEquals("Movie Id numbers ERROR: T001 aren't unique", result);
    }

    @Test
    void movieId_uniqueNumbersRegisteredCorrectly() {
        // Branch: two different movies with different numbers → both pass
        assertNull(validator.validateMovieId("TDK001", "The Dark Knight"));
        assertNull(validator.validateMovieId("T002", "Titanic"));
    }

    // ─────────────────────────────────────────
    // validateUsername tests
    // ─────────────────────────────────────────

    @Test
    void username_valid_returnsNull() {
        // Branch: valid username → null
        assertNull(validator.validateUsername("Doaa Mohamed"));
    }

    @Test
    void username_startsWithSpace_returnsError() {
        // Branch: starts with space → error
        String result = validator.validateUsername(" Doaa");
        assertEquals("Username ERROR:  Doaa is wrong", result);
    }

    @Test
    void username_containsDigits_returnsError() {
        // Branch: contains non-alphabetic/non-space char → error
        String result = validator.validateUsername("Doaa123");
        assertEquals("Username ERROR: Doaa123 is wrong", result);
    }

    @Test
    void username_containsSpecialChar_returnsError() {
        // Branch: contains special character → error
        String result = validator.validateUsername("Doaa@Mohamed");
        assertEquals("Username ERROR: Doaa@Mohamed is wrong", result);
    }

    @Test
    void username_singleWord_returnsNull() {
        // Edge case: single valid word
        assertNull(validator.validateUsername("Doaa"));
    }

    // ─────────────────────────────────────────
    // validateUserId tests
    // ─────────────────────────────────────────

    @Test
    void userId_valid_noLetterAtEnd_returnsNull() {
        // Branch: all digits, exactly 9 chars → null
        assertNull(validator.validateUserId("123456789"));
    }

    @Test
    void userId_valid_oneLetterAtEnd_returnsNull() {
        // Branch: ends with exactly 1 letter → null
        assertNull(validator.validateUserId("12345678A"));
    }

    @Test
    void userId_tooShort_returnsError() {
        // Branch: length != 9 (too short)
        String result = validator.validateUserId("12345");
        assertEquals("User Id ERROR: 12345 is wrong", result);
    }

    @Test
    void userId_tooLong_returnsError() {
        // Branch: length != 9 (too long)
        String result = validator.validateUserId("1234567890");
        assertEquals("User Id ERROR: 1234567890 is wrong", result);
    }

    @Test
    void userId_containsSpecialChar_returnsError() {
        // Branch: non-alphanumeric character
        String result = validator.validateUserId("1234@678A");
        assertEquals("User Id ERROR: 1234@678A is wrong", result);
    }

    @Test
    void userId_endsWithTwoLetters_returnsError() {
        // Branch: more than 1 letter at the end
        String result = validator.validateUserId("1234567AB");
        assertEquals("User Id ERROR: 1234567AB is wrong", result);
    }

    @Test
    void userId_startsWithLetter_returnsError() {
        // Branch: does NOT start with a digit
        String result = validator.validateUserId("A12345678");
        assertEquals("User Id ERROR: A12345678 is wrong", result);
    }

    @Test
    void userId_duplicate_returnsError() {
        // Branch: same ID used twice
        validator.validateUserId("123456789");  // first use — registers it
        String result = validator.validateUserId("123456789");  // second use
        assertEquals("User Id ERROR: 123456789 is wrong", result);
    }

    @Test
    void userId_twoUniqueIds_bothPass() {
        // Branch: two different valid IDs → both null
        assertNull(validator.validateUserId("123456789"));
        assertNull(validator.validateUserId("987654321"));
    }
}