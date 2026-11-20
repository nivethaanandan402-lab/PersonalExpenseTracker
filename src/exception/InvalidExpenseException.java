package exception;

/**
 * Custom Checked Exception: InvalidExpenseException
 * 
 * OOP Concept Demonstrated:
 * - Exception Handling & Custom Checked Exceptions:
 *   Thrown when an expense fails validation rules (e.g., negative/zero amount,
 *   empty name, invalid category, or incorrect date format).
 */
public class InvalidExpenseException extends Exception {

    /**
     * Parameterized constructor invoking parent constructor with 'super'.
     *
     * @param message Detailed reason why the expense data is invalid
     */
    public InvalidExpenseException(String message) {
        super(message);
    }

    /**
     * Overloaded constructor demonstrating Constructor Overloading and 'this' keyword.
     */
    public InvalidExpenseException() {
        this("Invalid expense details provided. Please check the input constraints.");
    }
}
