package service;

import model.Budget;
import storage.FileStorage;
import storage.Storage;
import util.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Class: BudgetService
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: Protects budget calculations, threshold tracking, and CSV persistence.
 * 2. Polymorphism: Utilizes 'Storage<Budget>' interface for file storage.
 * 3. Exception Handling: Validates input and throws IllegalArgumentException on invalid budget rules.
 */
public class BudgetService {

    private final Storage<Budget> budgetStorage;
    private final List<Budget> budgets;

    public BudgetService() {
        this.budgetStorage = new FileStorage.BudgetFileStorage();
        this.budgets = new ArrayList<>();
        reloadBudgets();
    }

    /**
     * Reloads budgets from CSV storage.
     */
    public synchronized void reloadBudgets() {
        budgets.clear();
        try {
            budgets.addAll(budgetStorage.loadAll());
        } catch (IOException e) {
            System.err.println("Error loading budgets: " + e.getMessage());
        }
    }

    /**
     * Sets or updates a monthly budget for a user.
     * Rule: "Monthly budget must be greater than 0."
     *
     * @param username  Owner username
     * @param monthYear Year and month in YYYY-MM format
     * @param amount    Budget amount (> 0)
     */
    public synchronized Budget setBudget(String username, String monthYear, double amount) {
        if (!Validator.isValidMonthYear(monthYear)) {
            throw new IllegalArgumentException("Invalid month format. Please use YYYY-MM (e.g. 2026-11).");
        }
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Monthly budget must be strictly greater than 0.");
        }

        // Check if an existing budget exists for this user and month
        for (Budget b : budgets) {
            if (b.getUsername().equalsIgnoreCase(username) && b.getMonthYear().equals(monthYear)) {
                b.setAmount(amount);
                persistBudgets();
                return b;
            }
        }

        // If not found, create a new one
        Budget newBudget = new Budget(username, monthYear, amount);
        budgets.add(newBudget);
        persistBudgets();
        return newBudget;
    }

    /**
     * Retrieves the budget for a specific user and month.
     */
    public Budget getBudget(String username, String monthYear) {
        for (Budget b : budgets) {
            if (b.getUsername().equalsIgnoreCase(username) && b.getMonthYear().equals(monthYear)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Retrieves all budgets configured for a specific user.
     */
    public List<Budget> getUserBudgets(String username) {
        List<Budget> result = new ArrayList<>();
        for (Budget b : budgets) {
            if (b.getUsername().equalsIgnoreCase(username)) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Admin Feature: Retrieves all user budgets across the entire system.
     */
    public List<Budget> getAllBudgets() {
        return new ArrayList<>(budgets);
    }

    /**
     * Formats and prints the budget status for a given user and month.
     *
     * @param username   User's username
     * @param monthYear  Month-Year string (YYYY-MM)
     * @param totalSpent Total expenses incurred in that month
     */
    public String getBudgetStatusSummary(String username, String monthYear, double totalSpent) {
        Budget b = getBudget(username, monthYear);
        if (b == null) {
            return String.format("No budget has been set for %s (Month: %s). Total spent: Rs. %.2f",
                    username, monthYear, totalSpent);
        }

        double limit = b.getAmount();
        double remaining = limit - totalSpent;
        double percentUsed = (totalSpent / limit) * 100.0;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Budget for %s (%s):%n", username, monthYear));
        sb.append(String.format("  - Allocated Budget : Rs. %.2f%n", limit));
        sb.append(String.format("  - Total Spent      : Rs. %.2f%n", totalSpent));
        sb.append(String.format("  - Remaining Budget : Rs. %.2f (%.1f%% used)%n", remaining, percentUsed));

        if (remaining < 0) {
            sb.append(String.format("  - STATUS           : [!] ALERT: Budget EXCEEDED by Rs. %.2f!%n", Math.abs(remaining)));
        } else if (percentUsed >= 80.0) {
            sb.append("  - STATUS           : [!] WARNING: 80% or more of your budget is used.%n");
        } else {
            sb.append("  - STATUS           : [OK] Spending is within comfortable limits.%n");
        }

        return sb.toString();
    }

    /**
     * Persists budgets to CSV.
     */
    public synchronized void persistBudgets() {
        try {
            budgetStorage.saveAll(budgets);
        } catch (IOException e) {
            System.err.println("Error saving budgets: " + e.getMessage());
        }
    }
}
