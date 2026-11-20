package model;

/**
 * Concrete Subclass: RegularUser
 * 
 * OOP Concepts Demonstrated:
 * 1. Inheritance: Extends the abstract class {@link User}, inheriting fields and behaviors.
 * 2. 'super' Keyword: Calls the superclass constructor using 'super(username, password, fullName, createdAt)'.
 * 3. Method Overriding (@Override): Provides concrete implementations for 'getRole()' and 'displayUserTypeInfo()'.
 * 4. Encapsulation: Maintains state for regular user account behaviors.
 */
public class RegularUser extends User {

    /**
     * Parameterized Constructor with explicit creation date.
     * Uses 'super' to pass parameters to the parent class {@link User}.
     *
     * @param username  Unique account username
     * @param password  Account password
     * @param fullName  Display name
     * @param createdAt Creation date string
     */
    public RegularUser(String username, String password, String fullName, String createdAt) {
        super(username, password, fullName, createdAt);
    }

    /**
     * Overloaded Constructor with auto-generated creation date.
     * Demonstrates Constructor Chaining via 'super'.
     *
     * @param username Unique account username
     * @param password Account password
     * @param fullName Display name
     */
    public RegularUser(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    /**
     * Polymorphic implementation of getRole().
     * Overrides abstract method in User.
     *
     * @return "USER"
     */
    @Override
    public String getRole() {
        return "USER";
    }

    /**
     * Displays user-specific privileges.
     * Overrides abstract method in User.
     */
    @Override
    public void displayUserTypeInfo() {
        System.out.println("Role: Standard User | Access: Personal Expenses & Monthly Budgets");
    }

    /**
     * Demonstrates Method Overriding.
     */
    @Override
    public String toString() {
        return "RegularUser: " + super.toString();
    }
}
