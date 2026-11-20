package exception;

/**
 * Custom Checked Exception: ExpenseNotFoundException
 * 
 * OOP Concept Demonstrated:
 * - Exception Handling & Custom Checked Exceptions:
 *   Thrown when an expense with a requested ID does not exist or does not belong
 *   to the logged-in user attempting to update or delete it.
 */
public class ExpenseNotFoundException extends Exception {

    /**
     * Parameterized constructor invoking parent constructor with 'super'.
     *
     * @param message Detailed reason why the expense was not found
     */
    public ExpenseNotFoundException(String message) {
        super(message);
    }

    /**
     * Overloaded constructor demonstrating Constructor Overloading and 'this' keyword.
     */
    public ExpenseNotFoundException() {
        this("The requested expense record could not be found.");
    }
}
