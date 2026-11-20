package exception;

/**
 * Custom Checked Exception: DuplicateUserException
 * 
 * OOP Concept Demonstrated:
 * - Exception Handling & Custom Checked Exceptions:
 *   Subclasses {@link java.lang.Exception}. Enforces compile-time exception checking.
 * - Used during user registration when a given username already exists in the system.
 */
public class DuplicateUserException extends Exception {

    /**
     * Parameterized constructor invoking parent constructor with 'super'.
     *
     * @param message Explanatory message indicating which username is duplicate
     */
    public DuplicateUserException(String message) {
        super(message);
    }

    /**
     * Overloaded constructor demonstrating Constructor Overloading and 'this' keyword.
     */
    public DuplicateUserException() {
        this("A user with this username already exists. Please choose a different username.");
    }
}
