package service;

import model.Category;
import model.Expense;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete Report Class: CategoryWiseReport
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface Implementation (@Override): Implements {@link Reportable}.
 * 2. Polymorphism: Provides concrete implementation for 'generateReport()'.
 * 3. Collections (HashMap): Aggregates totals by category key with O(1) lookup.
 */
public class CategoryWiseReport implements Reportable {

    private final String targetUser; // null or empty implies all users
    private final List<Expense> expenses;

    public CategoryWiseReport(String targetUser, List<Expense> expenses) {
        this.targetUser = targetUser;
        this.expenses = expenses;
    }

    @Override
    public String getReportTitle() {
        return "Category-Wise Spending Breakdown Report";
    }

    @Override
    public void generateReport() {
        System.out.println("\n========================================================");
        System.out.println("  " + getReportTitle());
        System.out.println("  Scope: " + (targetUser == null || targetUser.isEmpty() ? "All Users (Admin View)" : "User: " + targetUser));
        System.out.println("========================================================");

        // Map category -> total spent (Collections: HashMap)
        Map<String, Double> categoryTotals = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();

        // Initialize predefined categories with 0.0
        for (String cat : Category.PREDEFINED_CATEGORIES) {
            categoryTotals.put(cat, 0.0);
            categoryCounts.put(cat, 0);
        }

        double grandTotal = 0.0;
        int totalTransactions = 0;

        for (Expense e : expenses) {
            if (targetUser != null && !targetUser.isEmpty() && !e.getUsername().equalsIgnoreCase(targetUser)) {
                continue;
            }
            String cat = e.getCategory();
            categoryTotals.put(cat, categoryTotals.getOrDefault(cat, 0.0) + e.getAmount());
            categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
            grandTotal += e.getAmount();
            totalTransactions++;
        }

        System.out.printf("  %-20s | %-12s | %-15s | %-8s%n", "Category", "Transactions", "Total (Rs.)", "Share (%)");
        System.out.println("  ------------------------------------------------------------------");

        for (String cat : Category.PREDEFINED_CATEGORIES) {
            double amount = categoryTotals.getOrDefault(cat, 0.0);
            int count = categoryCounts.getOrDefault(cat, 0);
            double percentage = grandTotal > 0 ? (amount / grandTotal) * 100.0 : 0.0;

            System.out.printf("  %-20s | %-12d | Rs. %12.2f | %6.1f%%%n",
                    cat, count, amount, percentage);
        }

        System.out.println("  ------------------------------------------------------------------");
        System.out.printf("  TOTAL SPENT ACROSS ALL CATEGORIES: Rs. %.2f (%d transactions)%n",
                grandTotal, totalTransactions);
        System.out.println("==================================================================\n");
    }
}
