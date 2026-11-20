package test;

import exception.DuplicateUserException;
import exception.ExpenseNotFoundException;
import exception.InvalidExpenseException;
import exception.InvalidLoginException;
import model.Admin;
import model.Budget;
import model.Expense;
import model.RegularUser;
import model.User;
import service.AuthService;
import service.BudgetService;
import service.ExpenseService;
import service.ReportService;

import java.util.List;

/**
 * System Integration & Verification Test Suite
 * 
 * Verifies all 10 core requirements without interactive blocking.
 */
public class SystemIntegrationTest {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    RUNNING AUTOMATED SYSTEM INTEGRATION TESTS    ");
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        AuthService authService = new AuthService();
        ExpenseService expenseService = new ExpenseService();
        BudgetService budgetService = new BudgetService();
        ReportService reportService = new ReportService(expenseService, budgetService);

        // Test 1: Default Admin Login
        try {
            User admin = authService.login("admin", "admin123");
            assertCondition("ADMIN".equals(admin.getRole()), "Admin role verification");
            assertCondition(admin instanceof Admin, "Admin polymorphism instanceof check");
            System.out.println("[PASS] Test 1: Default Admin Login & Role Check");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 1: " + e.getMessage());
            failed++;
        }

        // Test 2: User Registration & Duplicate Prevention
        String testUser = "testuser_" + System.currentTimeMillis() % 10000;
        try {
            User reg = authService.register(testUser, "secret123", "Test User");
            assertCondition(reg instanceof RegularUser, "RegularUser polymorphism check");
            assertCondition("USER".equals(reg.getRole()), "User role verification");

            // Attempt duplicate
            boolean caughtDuplicate = false;
            try {
                authService.register(testUser, "another123", "Duplicate Attempt");
            } catch (DuplicateUserException ex) {
                caughtDuplicate = true;
            }
            assertCondition(caughtDuplicate, "DuplicateUserException correctly thrown");
            System.out.println("[PASS] Test 2: User Registration & Duplicate User Check");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 2: " + e.getMessage());
            failed++;
        }

        // Test 3: Invalid Login Exception Handling
        try {
            boolean caughtInvalid = false;
            try {
                authService.login(testUser, "wrong_pass");
            } catch (InvalidLoginException ex) {
                caughtInvalid = true;
            }
            assertCondition(caughtInvalid, "InvalidLoginException correctly thrown");
            System.out.println("[PASS] Test 3: Invalid Credentials Handling");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 3: " + e.getMessage());
            failed++;
        }

        // Test 4: Add Expense & Validation Constraints
        Expense createdExpense = null;
        try {
            // Test invalid amount (amount <= 0)
            boolean caughtInvalidAmt = false;
            try {
                expenseService.addExpense(testUser, "Invalid Exp", -50.0, "Food", "2026-11-20", "Cash");
            } catch (InvalidExpenseException ex) {
                caughtInvalidAmt = true;
            }
            assertCondition(caughtInvalidAmt, "Negative amount rejected");

            // Test invalid category
            boolean caughtInvalidCat = false;
            try {
                expenseService.addExpense(testUser, "Invalid Cat", 100.0, "Astronomy", "2026-11-20", "Cash");
            } catch (InvalidExpenseException ex) {
                caughtInvalidCat = true;
            }
            assertCondition(caughtInvalidCat, "Non-predefined category rejected");

            // Test valid expense creation
            createdExpense = expenseService.addExpense(testUser, "College Textbooks", 1450.0,
                    "Education", "2026-11-20", "UPI", "Semester 3 textbooks");
            assertCondition(createdExpense.getId() > 0, "Valid ID assigned");
            assertCondition(createdExpense.getAmount() == 1450.0, "Amount accurately preserved");
            System.out.println("[PASS] Test 4: Add Expense & Field Validation");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 4: " + e.getMessage());
            failed++;
        }

        // Test 5: Update Expense & Ownership Protection
        try {
            assertCondition(createdExpense != null, "Preceding expense exists");
            // Attempt update by a different user (ownership check)
            boolean caughtUnauthorized = false;
            try {
                expenseService.updateExpense(createdExpense.getId(), "unauthorized_user", "Hacked Title",
                        50.0, "Food", "2026-11-20", "Cash", "Desc");
            } catch (ExpenseNotFoundException ex) {
                caughtUnauthorized = true;
            }
            assertCondition(caughtUnauthorized, "Unauthorized update prevented by ownership check");

            // Legitimate update by owner
            Expense updated = expenseService.updateExpense(createdExpense.getId(), testUser, "Updated Textbooks",
                    1600.0, "Education", "2026-11-20", "UPI", "Added lab manual");
            assertCondition(updated.getAmount() == 1600.0, "Amount updated correctly");
            assertCondition("Updated Textbooks".equals(updated.getTitle()), "Title updated correctly");
            System.out.println("[PASS] Test 5: Expense Update & Ownership Protection");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 5: " + e.getMessage());
            failed++;
        }

        // Test 6: Category Filtering & Sorting
        try {
            expenseService.addExpense(testUser, "Cafeteria Lunch", 120.0, "Food", "2026-11-19", "Cash");
            expenseService.addExpense(testUser, "Bus Ticket", 50.0, "Transportation", "2026-11-18", "Cash");

            List<Expense> allUser = expenseService.getUserExpenses(testUser);
            assertCondition(allUser.size() >= 3, "All user expenses retrieved");

            List<Expense> foodOnly = expenseService.filterByCategory(allUser, "Food");
            assertCondition(!foodOnly.isEmpty(), "Food category filter not empty");
            for (Expense fe : foodOnly) {
                assertCondition("Food".equals(fe.getCategory()), "Filtered item matches Food category");
            }

            // Test sorting
            List<Expense> sortedDate = expenseService.sortExpensesByDate(allUser, true);
            assertCondition(sortedDate.get(0).getDate().compareTo(sortedDate.get(sortedDate.size() - 1).getDate()) <= 0,
                    "Ascending date sort verified");

            System.out.println("[PASS] Test 6: Category Filtering & Comparator Sorting");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 6: " + e.getMessage());
            failed++;
        }

        // Test 7: Budget Creation, Status & Threshold Warning
        try {
            Budget b = budgetService.setBudget(testUser, "2026-11", 2000.0);
            assertCondition(b.getAmount() == 2000.0, "Budget amount recorded");

            double totalSpent = expenseService.getTotalExpenses(testUser);
            String summary = budgetService.getBudgetStatusSummary(testUser, "2026-11", totalSpent);
            assertCondition(summary.contains("2026-11"), "Summary contains month");
            System.out.println("[PASS] Test 7: Monthly Budget Creation & Threshold Analytics");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 7: " + e.getMessage());
            failed++;
        }

        // Test 8: Admin Global Views & System Extremes
        try {
            List<Expense> allSys = expenseService.getAllExpenses();
            assertCondition(!allSys.isEmpty(), "System expenses non-empty");

            Expense high = expenseService.getHighestExpense(allSys);
            Expense low = expenseService.getLowestExpense(allSys);
            assertCondition(high != null && low != null, "Extremes calculated");
            assertCondition(high.getAmount() >= low.getAmount(), "Highest >= Lowest");
            System.out.println("[PASS] Test 8: Admin Global Analytics & Extremes");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 8: " + e.getMessage());
            failed++;
        }

        // Test 9: Polymorphic Reports Execution
        try {
            reportService.generateUserMonthlySummary(testUser, "2026-11");
            reportService.generateUserCategorySummary(testUser);
            reportService.generateAdminMonthlyReport(null);
            reportService.generateAdminCategoryReport();
            System.out.println("[PASS] Test 9: Polymorphic Reportable Execution");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 9: " + e.getMessage());
            failed++;
        }

        // Test 10: Delete Expense & Cleanup
        try {
            if (createdExpense != null) {
                boolean deleted = expenseService.deleteExpense(createdExpense.getId(), testUser);
                assertCondition(deleted, "Expense successfully deleted");
                assertCondition(expenseService.findUserExpenseById(createdExpense.getId(), testUser) == null,
                        "Deleted expense is no longer present");
            }
            System.out.println("[PASS] Test 10: Expense Deletion & Persistence Sync");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 10: " + e.getMessage());
            failed++;
        }

        System.out.println("==================================================");
        System.out.printf("  TEST RESULTS: %d Passed | %d Failed%n", passed, failed);
        System.out.println("==================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException("Assertion Failed: " + message);
        }
    }
}
