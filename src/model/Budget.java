package model;

/**
 * Entity Class: Budget
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: Protects budget fields (username, monthYear, amount) via private access.
 * 2. Constructors & 'this' keyword: Parameterized initialization ensuring clean object creation.
 * 3. Method Overriding: Overrides 'toString()' to print clear budget information.
 */
public class Budget {

    private String username;
    private String monthYear; // Format: YYYY-MM (e.g., 2026-11)
    private double amount;    // Budget amount in currency units (> 0)

    /**
     * Parameterized Constructor using 'this'.
     *
     * @param username  Owner's username
     * @param monthYear Year and month in YYYY-MM format
     * @param amount    Budget limit amount
     */
    public Budget(String username, String monthYear, double amount) {
        this.username = username;
        this.monthYear = monthYear;
        this.amount = amount;
    }

    // --- Getters and Setters (Encapsulation) ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Demonstrates Method Overriding.
     */
    @Override
    public String toString() {
        return String.format("Budget[User: %s | Month: %s | Limit: Rs. %.2f]",
                username, monthYear, amount);
    }
}
