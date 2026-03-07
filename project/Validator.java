import java.util.HashSet;
import java.util.Set;

public class Validator {

    //Ensure uniqueness for movie IDs
    private Set<String> usedMovieIdNumbers = new HashSet<>();
    
    //Ensure uniqueness for user IDs
    private Set<String> usedUserIds = new HashSet<>();

    // ─────────────────────────────────────────
    // MOVIE TITLE VALIDATION
    // null would be no error
    // ─────────────────────────────────────────
    public String validateMovieTitle(String title) {
        String[] words = title.split(" ");
        for (String word : words) {
            if (word.isEmpty() || !Character.isUpperCase(word.charAt(0))) {
                return "Movie Title ERROR: " + title + " is wrong";
            }
        }
        return null;
    }

    // ─────────────────────────────────────────
    // MOVIE ID VALIDATION
    // ─────────────────────────────────────────
    public String validateMovieId(String movieId, String movieTitle) {

        // Step 1: Extract the letters part and numbers part from the ID
        int splitIndex = movieId.length() - 3;
        if (splitIndex <= 0) {
            return "Movie Id letters ERROR: " + movieId + " are wrong";
        }

        String idLetters = movieId.substring(0, splitIndex);
        String idNumbers = movieId.substring(splitIndex);

        // Step 2: Check that the last 3 characters are digits
        if (!idNumbers.matches("\\d{3}")) {
            return "Movie Id numbers ERROR: " + movieId + " aren't unique";
        }

        // Step 3 Build the correct ID from the movie title
        StringBuilder expectedLetters = new StringBuilder();
        for (String word : movieTitle.split(" ")) {
            if (!word.isEmpty()) {
                expectedLetters.append(word.charAt(0));
            }
        }

        // Step 4: Compare the give ID with the correct ID from the previous step 
        if (!idLetters.equals(expectedLetters.toString())) {
            return "Movie Id letters ERROR: " + movieId + " are wrong";
        }

        // Step 5: Check that the 3 digits are unique (not used by another movie)
        if (usedMovieIdNumbers.contains(idNumbers)) {
            return "Movie Id numbers ERROR: " + movieId + " aren't unique";
        }

        // No error
        usedMovieIdNumbers.add(idNumbers);
        return null; 
    }

    // ─────────────────────────────────────────
    // USERNAME VALIDATION
    // ─────────────────────────────────────────
    public String validateUsername(String username) {
        if (username.startsWith(" ")) {
            return "Username ERROR: " + username + " is wrong";
        }
        if (!username.matches("[a-zA-Z ]+")) {
            return "Username ERROR: " + username + " is wrong";
        }
        return null; 
    }

    // ─────────────────────────────────────────
    // USER ID VALIDATION
    // ─────────────────────────────────────────
    public String validateUserId(String userId) {

        // Step 1: Must be exactly 9 characters
        if (userId.length() != 9) {
            return "User Id ERROR: " + userId + " is wrong";
        }

        // Step 2: Must be alphanumeric only
        if (!userId.matches("[a-zA-Z0-9]+")) {
            return "User Id ERROR: " + userId + " is wrong";
        }

        // Step 3: Count how many letters are at the END
        int letterCount = 0;
        for (int i = userId.length() - 1; i >= 0; i--) {
            if (Character.isLetter(userId.charAt(i))) {
                letterCount++;
            } else {
                break; // stop as soon as we hit a digit
            }
        }

        if (letterCount > 1) {
            return "User Id ERROR: " + userId + " is wrong";
        }

        // Step 4: Must START with at least one digit
        if (!Character.isDigit(userId.charAt(0))) {
            return "User Id ERROR: " + userId + " is wrong";
        }

        // Step 5: Must be unique
        if (usedUserIds.contains(userId)) {
            return "User Id ERROR: " + userId + " is wrong";
        }

        // All good — register this ID as used
        usedUserIds.add(userId);
        return null; // no error
    }
}