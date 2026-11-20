package service;

import model.Budget;
import model.Expense;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Concrete Report Class: UserSummaryReport
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface Implementation: Third class implementing {@link Reportable}.
 * 2. Polymorphism: Conforms to common contract while producing specialized summary analytics.
 * 3. Collections (Comparator & Sorting): Uses Comparator to identify highest and lowest expenses.
 */
public class UserSummaryReport implements Reportable {

    private final String username;
    private final List<Expense> userExpenses;
    private final Budget currentBudget;

    public UserSummaryReport(String username, List<Expense> userExpenses, Budget currentBudget) {
        this.username = username;
        this.userExpenses = userExpenses;
        this.currentBudget = currentBudget;
    }

    @Override
    public String getReportTitle() {
        return "Comprehensive User Financial Summary: " + username;
    }

    @Override
    public void generateReport() {
        System.out.println("\n========================================================");
        System.out.println("  " + getReportTitle());
        System.out.println("========================================================");

        if (userExpenses.isEmpty()) {
            System.out.println("  No expense records on file for user: " + username);
            System.out.println("========================================================\n");
            return;
        }

        double totalSpent = 0.0;
        for (Expense e : userExpenses) {
            totalSpent += e.getAmount();
        }

        double averageExpense = totalSpent / userExpenses.size();

        // Find highest and lowest expense records using Comparator
        Expense highest = Collections.max(userExpenses, Comparator.comparingDouble(Expense::getAmount));
        Expense lowest = Collections.min(userExpenses, Comparator.comparingDouble(Expense::getAmount));

        System.out.printf("  Total Transactions Recorded : %d%n", userExpenses.size());
        System.out.printf("  Cumulative Total Spending   : Rs. %.2f%n", totalSpent);
        System.out.printf("  Average Spending per Entry  : Rs. %.2f%n", averageExpense);
        System.out.printf("  Highest Single Expense      : %s (Rs. %.2f on %s)%n",
                highest.getTitle(), highest.getAmount(), highest.getDate());
        System.out.printf("  Lowest Single Expense       : %s (Rs. %.2f on %s)%n",
                lowest.getTitle(), lowest.getAmount(), lowest.getDate());

        if (currentBudget != null) {
            double remaining = currentBudget.getAmount() - totalSpent;
            double percentUsed = (totalSpent / currentBudget.getAmount()) * 100.0;
            System.out.println("  ------------------------------------------------------");
            System.out.printf("  Active Monthly Budget (%s) : Rs. %.2f%n", currentBudget.getMonthYear(), currentBudget.getAmount());
            System.out.printf("  Remaining Budget            : Rs. %.2f (%.1f%% used)%n", remaining, percentUsed);
            if (remaining < 0) {
                System.out.printf("  STATUS                      : [!] EXCEEDED BY Rs. %.2f!%n", Math.abs(remaining));
            } else if (percentUsed >= 80.0) {
                System.out.println("  STATUS                      : [!] WARNING - Nearing Budget Limit (>= 80%)");
            } else {
                System.out.println("  STATUS                      : [OK] Within Healthy Budget Limits");
            }
        }
        System.out.println("========================================================\n");
    }
}
