package exception;

/**
 * Custom Checked Exception: InvalidLoginException
 * 
 * OOP Concept Demonstrated:
 * - Exception Handling & Custom Checked Exceptions:
 *   Extending {@link java.lang.Exception} creates a checked exception that must
 *   be either caught using try-catch or declared in the method signature using 'throws'.
 * - Used when authentication credentials (username or password) are incorrect.
 */
public class InvalidLoginException extends Exception {

    /**
     * Parameterized constructor passing custom error message to superclass.
     * Uses the 'super' keyword to invoke the Exception(String message) constructor.
     *
     * @param message Detailed reason for login failure
     */
    public InvalidLoginException(String message) {
        super(message);
    }

    /**
     * Overloaded constructor with default error message.
     * Demonstrates Constructor Overloading and 'this' keyword.
     */
    public InvalidLoginException() {
        this("Invalid username or password. Please try again.");
    }
}
