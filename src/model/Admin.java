package model;

/**
 * Concrete Subclass: Admin
 * 
 * OOP Concepts Demonstrated:
 * 1. Inheritance: Extends the abstract class {@link User}.
 * 2. 'super' Keyword: Uses 'super(...)' to delegate user property initialization to the base class.
 * 3. Method Overriding: Overrides 'getRole()' returning "ADMIN" and customizes 'displayUserTypeInfo()'.
 * 4. Principle of Least Privilege: Admin is designed exclusively for monitoring and reporting.
 *    Admin cannot modify or delete users' personal expenses.
 */
public class Admin extends User {

    /**
     * Parameterized Constructor with explicit creation date.
     *
     * @param username  Admin account username
     * @param password  Admin account password
     * @param fullName  Admin display name
     * @param createdAt Creation date string
     */
    public Admin(String username, String password, String fullName, String createdAt) {
        super(username, password, fullName, createdAt);
    }

    /**
     * Overloaded Constructor with auto-generated creation date.
     * Demonstrates Constructor Chaining using 'super'.
     *
     * @param username Admin account username
     * @param password Admin account password
     * @param fullName Admin display name
     */
    public Admin(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    /**
     * Polymorphic implementation of getRole().
     * Overrides abstract method in User.
     *
     * @return "ADMIN"
     */
    @Override
    public String getRole() {
        return "ADMIN";
    }

    /**
     * Displays admin-specific privileges.
     * Overrides abstract method in User.
     */
    @Override
    public void displayUserTypeInfo() {
        System.out.println("Role: Administrator | Access: Global Reports, User Auditing & Spending Analytics (Read-Only)");
    }

    /**
     * Demonstrates Method Overriding.
     */
    @Override
    public String toString() {
        return "AdminUser: " + super.toString();
    }
}
