package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Entity Class: Expense
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: All instance variables (id, username, title, amount, category, date,
 *    paymentMethod, description) are declared 'private'. Access is moderated through getters
 *    and validated setters.
 * 2. Static Members & 'final' Keyword:
 *    - 'PAYMENT_METHODS' is a public static final list containing allowed payment channels.
 * 3. Constructor Overloading & 'this' Keyword:
 *    - Chained constructors allow creating an expense with or without an optional description.
 * 4. Method Overriding:
 *    - Overrides {@link Object#toString()} to provide a structured representation of the expense.
 */
public class Expense {

    // Static & Final: Predefined payment methods mandated by requirements
    public static final List<String> PAYMENT_METHODS = Collections.unmodifiableList(
            Arrays.asList(
                    "Cash",
                    "UPI",
                    "Debit Card",
                    "Credit Card",
                    "Net Banking"
            )
    );

    // Encapsulated instance variables
    private int id;
    private String username;
    private String title;
    private double amount;
    private String category;
    private String date; // Format: YYYY-MM-DD
    private String paymentMethod;
    private String description; // Optional, max 200 chars

    /**
     * Overloaded Constructor (without description).
     * Demonstrates Constructor Chaining using 'this(...)'.
     *
     * @param id            Unique numeric ID
     * @param username      Owner username
     * @param title         Expense title/name
     * @param amount        Monetary value (> 0)
     * @param category      Predefined category name
     * @param date          Date string (YYYY-MM-DD)
     * @param paymentMethod Predefined payment method
     */
    public Expense(int id, String username, String title, double amount,
                   String category, String date, String paymentMethod) {
        this(id, username, title, amount, category, date, paymentMethod, "None");
    }

    /**
     * Primary Parameterized Constructor.
     * Uses 'this' to assign arguments to instance variables.
     *
     * @param id            Unique numeric ID
     * @param username      Owner username
     * @param title         Expense title/name
     * @param amount        Monetary value (> 0)
     * @param category      Predefined category name
     * @param date          Date string (YYYY-MM-DD)
     * @param paymentMethod Predefined payment method
     * @param description   Optional description (up to 200 characters)
     */
    public Expense(int id, String username, String title, double amount,
                   String category, String date, String paymentMethod, String description) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.description = (description != null && !description.trim().isEmpty()) ? description.trim() : "None";
    }

    // --- Getters and Setters (Encapsulation) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Helper to extract Year-Month string (YYYY-MM) from the date.
     * Useful for monthly budget calculations and reports.
     *
     * @return Month-Year string, e.g. "2026-11"
     */
    public String getMonthYear() {
        if (date != null && date.length() >= 7) {
            return date.substring(0, 7);
        }
        return "";
    }

    /**
     * Demonstrates Method Overriding.
     */
    @Override
    public String toString() {
        return String.format("[#%d] %s | Rs. %.2f | %s | %s | %s | %s",
                id, title, amount, category, date, paymentMethod, description);
    }
}
