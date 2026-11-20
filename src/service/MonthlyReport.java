package service;

import model.Expense;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Concrete Report Class: MonthlyReport
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface Implementation (@Override): Implements {@link Reportable}.
 * 2. Polymorphism: Can be held in a 'Reportable' reference variable.
 * 3. Collections (Map, TreeMap): Uses TreeMap to automatically sort spending by date/month.
 */
public class MonthlyReport implements Reportable {

    private final String targetUser; // null or empty implies all users (Admin view)
    private final String monthYear;  // e.g. "2026-11" or null for all months
    private final List<Expense> expenses;

    public MonthlyReport(String targetUser, String monthYear, List<Expense> expenses) {
        this.targetUser = targetUser;
        this.monthYear = monthYear;
        this.expenses = expenses;
    }

    @Override
    public String getReportTitle() {
        return "Monthly Expense Report (" + (monthYear != null ? monthYear : "All Months") + ")";
    }

    @Override
    public void generateReport() {
        System.out.println("\n========================================================");
        System.out.println("  " + getReportTitle());
        System.out.println("  User: " + (targetUser == null || targetUser.isEmpty() ? "All Users (Admin View)" : targetUser));
        System.out.println("========================================================");

        // Group expenses by Month-Year (YYYY-MM)
        Map<String, Double> monthlyTotals = new TreeMap<>();
        Map<String, Integer> monthlyCounts = new TreeMap<>();

        double grandTotal = 0.0;
        int matchedCount = 0;

        for (Expense e : expenses) {
            if (targetUser != null && !targetUser.isEmpty() && !e.getUsername().equalsIgnoreCase(targetUser)) {
                continue;
            }
            String my = e.getMonthYear();
            if (monthYear != null && !monthYear.isEmpty() && !my.equals(monthYear)) {
                continue;
            }

            monthlyTotals.put(my, monthlyTotals.getOrDefault(my, 0.0) + e.getAmount());
            monthlyCounts.put(my, monthlyCounts.getOrDefault(my, 0) + 1);
            grandTotal += e.getAmount();
            matchedCount++;
        }

        if (monthlyTotals.isEmpty()) {
            System.out.println("  No expense records found for the selected period.");
        } else {
            System.out.printf("  %-12s | %-12s | %-15s%n", "Month (YYYY-MM)", "Transactions", "Total Spending");
            System.out.println("  ------------------------------------------------------");
            for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
                System.out.printf("  %-15s | %-12d | Rs. %12.2f%n",
                        entry.getKey(),
                        monthlyCounts.get(entry.getKey()),
                        entry.getValue());
            }
            System.out.println("  ------------------------------------------------------");
            System.out.printf("  TOTAL: %d record(s) | Grand Total: Rs. %.2f%n", matchedCount, grandTotal);
        }
        System.out.println("========================================================\n");
    }
}
