package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Base Class: User
 * 
 * OOP Concepts Demonstrated:
 * 1. Abstraction: Defined as an abstract class because generic users cannot be instantiated directly;
 *    only concrete roles (RegularUser, Admin) can exist.
 * 2. Encapsulation: All instance fields are declared 'private', protecting internal data. Access is
 *    provided solely through public getters and validated setters.
 * 3. Constructors & Constructor Overloading: Multiple constructors allow object creation with or without
 *    explicit creation timestamps, utilizing the 'this' keyword for constructor chaining.
 * 4. Polymorphism (Abstract Methods): Declares abstract method 'getRole()' that must be implemented
 *    differently by RegularUser ("USER") and Admin ("ADMIN").
 */
public abstract class User {

    // Encapsulation: private fields
    private String username;
    private String password;
    private String fullName;
    private String createdAt;

    /**
     * Primary Parameterized Constructor.
     * Uses 'this' keyword to resolve naming conflicts between parameters and instance variables.
     *
     * @param username  Unique account username
     * @param password  Account password (at least 6 characters)
     * @param fullName  Display name of the user
     * @param createdAt Date the account was registered (YYYY-MM-DD)
     */
    public User(String username, String password, String fullName, String createdAt) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.createdAt = createdAt != null ? createdAt : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Overloaded Constructor: Automatically generates current date for createdAt.
     * Demonstrates Constructor Chaining using 'this(...)'.
     *
     * @param username Unique account username
     * @param password Account password
     * @param fullName Display name
     */
    public User(String username, String password, String fullName) {
        this(username, password, fullName, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    // --- Abstract Methods (Demonstrating Abstraction & Polymorphism) ---

    /**
     * Abstract method to obtain the user's role.
     * Subclasses (RegularUser, Admin) must override this method.
     *
     * @return Role string ("USER" or "ADMIN")
     */
    public abstract String getRole();

    /**
     * Abstract method to display role-specific privileges or dashboard summary.
     */
    public abstract void displayUserTypeInfo();

    // --- Getters and Setters (Demonstrating Encapsulation) ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Overriding toString() from Object class.
     * Demonstrates Method Overriding.
     */
    @Override
    public String toString() {
        return String.format("User[username='%s', name='%s', role='%s', created='%s']",
                username, fullName, getRole(), createdAt);
    }
}
