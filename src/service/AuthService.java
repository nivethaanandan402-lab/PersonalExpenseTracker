package service;

import exception.DuplicateUserException;
import exception.InvalidLoginException;
import model.Admin;
import model.RegularUser;
import model.User;
import storage.FileStorage;
import storage.Storage;
import util.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Class: AuthService
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: User persistence and cache management are fully encapsulated within this service.
 * 2. Polymorphism & Interfaces: Uses 'Storage<User>' interface to load and persist accounts.
 * 3. Exception Handling: Explicitly throws checked exceptions:
 *    - 'DuplicateUserException' if registration fails uniqueness rule.
 *    - 'InvalidLoginException' if authentication credentials do not match.
 * 4. Collections:
 *    - 'HashMap<String, User>' provides fast O(1) lookup for authentication and uniqueness checks.
 *    - 'ArrayList<User>' for sequential iteration.
 */
public class AuthService {

    private final Storage<User> userStorage;
    // Collections: HashMap for fast user credential and profile lookups
    private final Map<String, User> userCache;

    public AuthService() {
        this.userStorage = new FileStorage.UserFileStorage();
        this.userCache = new HashMap<>();
        reloadUsers();
    }

    /**
     * Synchronizes memory cache with CSV storage.
     */
    public synchronized void reloadUsers() {
        userCache.clear();
        try {
            List<User> list = userStorage.loadAll();
            for (User u : list) {
                userCache.put(u.getUsername().toLowerCase(), u);
            }
        } catch (IOException e) {
            System.err.println("Error loading users from file: " + e.getMessage());
        }
    }

    /**
     * User registration with validation and duplicate prevention.
     * Rule: "Username must be unique."
     * Rule: "Password must contain at least 6 characters."
     *
     * @param username Desired username
     * @param password Desired password (>= 6 chars)
     * @param fullName Full display name
     * @return Newly created User object
     * @throws DuplicateUserException If username is already taken
     * @throws IllegalArgumentException If format rules are violated
     */
    public synchronized User register(String username, String password, String fullName)
            throws DuplicateUserException, IllegalArgumentException {
        if (!Validator.isValidUsername(username)) {
            throw new IllegalArgumentException("Username must be alphanumeric and between 3-20 characters.");
        }
        if (!Validator.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }

        String lowerKey = username.trim().toLowerCase();
        if (userCache.containsKey(lowerKey)) {
            throw new DuplicateUserException("The username '" + username.trim() + "' is already registered.");
        }

        RegularUser newUser = new RegularUser(username.trim(), password, fullName.trim());
        userCache.put(lowerKey, newUser);

        persistUsers();
        return newUser;
    }

    /**
     * Authenticates user credentials.
     *
     * @param username Entered username
     * @param password Entered password
     * @return Authenticated User object (Polymorphic: can be RegularUser or Admin)
     * @throws InvalidLoginException If credentials fail verification
     */
    public User login(String username, String password) throws InvalidLoginException {
        if (username == null || password == null) {
            throw new InvalidLoginException("Username and password must not be empty.");
        }

        String lowerKey = username.trim().toLowerCase();
        User user = userCache.get(lowerKey);

        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidLoginException("Invalid username or password. Please verify and try again.");
        }

        return user;
    }

    /**
     * Persists all cached users to the CSV file.
     */
    private void persistUsers() {
        try {
            userStorage.saveAll(new ArrayList<>(userCache.values()));
        } catch (IOException e) {
            System.err.println("Error saving users to storage: " + e.getMessage());
        }
    }

    /**
     * Admin Feature: Returns list of all registered users.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userCache.values());
    }

    /**
     * Admin Feature: Returns total number of registered users.
     */
    public int getTotalUsersCount() {
        return userCache.size();
    }

    /**
     * Checks if a user exists by username.
     */
    public boolean userExists(String username) {
        if (username == null) return false;
        return userCache.containsKey(username.trim().toLowerCase());
    }

    /**
     * Retrieves user by username.
     */
    public User getUserByUsername(String username) {
        if (username == null) return null;
        return userCache.get(username.trim().toLowerCase());
    }
}
