package main;

import exception.DuplicateUserException;
import exception.ExpenseNotFoundException;
import exception.InvalidExpenseException;
import exception.InvalidLoginException;
import model.Admin;
import model.Budget;
import model.Category;
import model.Expense;
import model.RegularUser;
import model.User;
import service.AuthService;
import service.AutoSaveDaemon;
import service.BudgetService;
import service.ExpenseService;
import service.ReportService;
import ui.PersonalExpenseTrackerGUI;
import util.InputHelper;
import util.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application Class: Main
 * 
 * Console-based menu-driven application using java.util.Scanner.
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: Coordinates services (AuthService, ExpenseService, BudgetService, ReportService).
 * 2. Polymorphism: Handles logged-in 'User' polymorphically (Admin vs RegularUser).
 * 3. Exception Handling: Employs comprehensive try-catch blocks to catch custom checked exceptions
 *    (InvalidLoginException, DuplicateUserException, InvalidExpenseException, ExpenseNotFoundException).
 * 4. Multithreading Demo: Initiates an 'AutoSaveDaemon' thread running in the background.
 * 5. Swing GUI Interoperability: Offers direct option to launch the JFrame application.
 */
public class Main {

    private final AuthService authService;
    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final ReportService reportService;
    private final AutoSaveDaemon autoSaveDaemon;

    public Main() {
        this.authService = new AuthService();
        this.expenseService = new ExpenseService();
        this.budgetService = new BudgetService();
        this.reportService = new ReportService(expenseService, budgetService);

        // Multithreading Demo: Start periodic auto-save daemon thread (every 60 seconds)
        this.autoSaveDaemon = new AutoSaveDaemon(expenseService, budgetService, 60);
        this.autoSaveDaemon.start();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.runConsoleApp();
    }

    /**
     * Main application loop.
     */
    public void runConsoleApp() {
        // Try-with-resources: Ensures Scanner is automatically closed on program exit
        try (Scanner scanner = new Scanner(System.in)) {
            boolean exitApp = false;

            while (!exitApp) {
                printMainMenuHeader();
                System.out.println("1. Login");
                System.out.println("2. Sign Up (Register New Account)");
                System.out.println("3. Launch Desktop GUI (JFrame Window)");
                System.out.println("4. Exit Application");
                System.out.println("--------------------------------------------------");

                int choice = InputHelper.readInt(scanner, "Enter your choice (1-4): ", 1, 4);

                switch (choice) {
                    case 1:
                        handleLogin(scanner);
                        break;
                    case 2:
                        handleRegistration(scanner);
                        break;
                    case 3:
                        launchGuiMode();
                        break;
                    case 4:
                        exitApp = true;
                        System.out.println("\nSaving all records and exiting... Thank you for using Personal Expense Tracker!");
                        break;
                }
            }
        } finally {
            // Stop daemon thread gracefully
            if (autoSaveDaemon != null) {
                autoSaveDaemon.stopDaemon();
            }
        }
    }

    private void printMainMenuHeader() {
        System.out.println("\n==================================================");
        System.out.println("       PERSONAL EXPENSE TRACKER (OOP LAB)         ");
        System.out.println("==================================================");
        System.out.println("Default Admin Account: admin / admin123");
        System.out.println("--------------------------------------------------");
    }

    // =========================================================================
    // AUTHENTICATION FLOWS
    // =========================================================================

    private void handleLogin(Scanner scanner) {
        System.out.println("\n--- User / Admin Login ---");
        String username = InputHelper.readNonEmptyString(scanner, "Enter Username: ");
        String password = InputHelper.readNonEmptyString(scanner, "Enter Password: ");

        try {
            User user = authService.login(username, password);
            System.out.println("\n[+] Login Successful! Welcome, " + user.getFullName() + ".");
            user.displayUserTypeInfo();

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                handleAdminDashboard(scanner, (Admin) user);
            } else {
                handleUserDashboard(scanner, (RegularUser) user);
            }
        } catch (InvalidLoginException e) {
            System.out.println("\n[!] Authentication Error: " + e.getMessage());
        }
    }

    private void handleRegistration(Scanner scanner) {
        System.out.println("\n--- Register New User Account ---");
        String fullName = InputHelper.readNonEmptyString(scanner, "Enter Your Full Name: ");
        String username = InputHelper.readNonEmptyString(scanner, "Enter Desired Username (3-20 alphanumeric): ");
        String password = InputHelper.readNonEmptyString(scanner, "Enter Password (min 6 characters): ");

        try {
            User newUser = authService.register(username, password, fullName);
            System.out.println("\n[+] Registration Successful! Account created for: " + newUser.getUsername());
            System.out.println("    You can now log in using your credentials.");
        } catch (DuplicateUserException e) {
            System.out.println("\n[!] Registration Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\n[!] Validation Error: " + e.getMessage());
        }
    }

    private void launchGuiMode() {
        System.out.println("\n[+] Launching Java Swing JFrame Window...");
        PersonalExpenseTrackerGUI.launch(authService, expenseService, budgetService, reportService);
        System.out.println("    (The GUI window is now open on your desktop)");
    }

    // =========================================================================
    // REGULAR USER DASHBOARD
    // =========================================================================

    private void handleUserDashboard(Scanner scanner, RegularUser user) {
        boolean logout = false;
        String username = user.getUsername();

        while (!logout) {
            System.out.println("\n==================================================");
            System.out.printf("  USER DASHBOARD: %s (%s)%n", user.getFullName(), username);
            System.out.println("==================================================");
            System.out.println("1.  Add New Expense");
            System.out.println("2.  View All My Expenses");
            System.out.println("3.  Update An Existing Expense");
            System.out.println("4.  Delete An Expense");
            System.out.println("5.  View Expenses Filtered by Category");
            System.out.println("6.  View Total Expenses");
            System.out.println("7.  View Monthly Expense Summary");
            System.out.println("8.  View Category-Wise Expense Summary");
            System.out.println("9.  Set Monthly Budget");
            System.out.println("10. View Remaining Budget & Budget Status");
            System.out.println("11. View Personal Expense History (Chronological)");
            System.out.println("12. View Highest & Lowest Expense Records");
            System.out.println("13. Launch Desktop GUI (JFrame Window)");
            System.out.println("14. Logout to Main Menu");
            System.out.println("--------------------------------------------------");

            int choice = InputHelper.readInt(scanner, "Choose an option (1-14): ", 1, 14);

            switch (choice) {
                case 1:
                    userAddExpense(scanner, username);
                    break;
                case 2:
                    userViewAllExpenses(username);
                    break;
                case 3:
                    userUpdateExpense(scanner, username);
                    break;
                case 4:
                    userDeleteExpense(scanner, username);
                    break;
                case 5:
                    userFilterByCategory(scanner, username);
                    break;
                case 6:
                    userViewTotal(username);
                    break;
                case 7:
                    userMonthlySummary(scanner, username);
                    break;
                case 8:
                    reportService.generateUserCategorySummary(username);
                    break;
                case 9:
                    userSetBudget(scanner, username);
                    break;
                case 10:
                    userViewBudgetStatus(scanner, username);
                    break;
                case 11:
                    userViewExpenseHistory(scanner, username);
                    break;
                case 12:
                    userViewHighestLowest(username);
                    break;
                case 13:
                    launchGuiMode();
                    break;
                case 14:
                    logout = true;
                    System.out.println("\n[+] Logged out successfully. Returning to main menu.");
                    break;
            }
        }
    }

    private void userAddExpense(Scanner scanner, String username) {
        System.out.println("\n--- Add New Expense ---");
        String title = InputHelper.readNonEmptyString(scanner, "Expense Name / Title: ");
        double amount = InputHelper.readPositiveDouble(scanner, "Amount (Rs.): ", 0.0);
        String category = InputHelper.selectFromList(scanner, "Select Category:", Category.PREDEFINED_CATEGORIES);
        String date = InputHelper.readDate(scanner, "Date (YYYY-MM-DD): ");
        String payment = InputHelper.selectFromList(scanner, "Select Payment Method:", Expense.PAYMENT_METHODS);
        String desc = InputHelper.readOptionalString(scanner, "Description (Optional, press Enter for None): ");

        try {
            Expense added = expenseService.addExpense(username, title, amount, category, date, payment, desc);
            System.out.println("\n[+] Expense successfully added! Assigned ID: #" + added.getId());
        } catch (InvalidExpenseException e) {
            System.out.println("\n[!] Error adding expense: " + e.getMessage());
        }
    }

    private void userViewAllExpenses(String username) {
        System.out.println("\n--- All Expenses for User: " + username + " ---");
        List<Expense> list = expenseService.getUserExpenses(username);
        printExpenseTable(list);
    }

    private void userUpdateExpense(Scanner scanner, String username) {
        System.out.println("\n--- Update Existing Expense ---");
        List<Expense> list = expenseService.getUserExpenses(username);
        if (list.isEmpty()) {
            System.out.println("You have no expenses recorded yet to update.");
            return;
        }

        printExpenseTable(list);
        int id = InputHelper.readInt(scanner, "Enter the ID of the expense to update: ", 1, Integer.MAX_VALUE);

        Expense target = expenseService.findUserExpenseById(id, username);
        if (target == null) {
            System.out.println("[!] Expense #" + id + " not found or you do not have permission to modify it.");
            return;
        }

        System.out.println("Updating Expense: " + target);
        String title = InputHelper.readNonEmptyString(scanner, "New Expense Name [" + target.getTitle() + "]: ");
        double amount = InputHelper.readPositiveDouble(scanner, "New Amount (Rs.) [" + target.getAmount() + "]: ", 0.0);
        String category = InputHelper.selectFromList(scanner, "Select New Category [" + target.getCategory() + "]:", Category.PREDEFINED_CATEGORIES);
        String date = InputHelper.readDate(scanner, "New Date (YYYY-MM-DD) [" + target.getDate() + "]: ");
        String payment = InputHelper.selectFromList(scanner, "Select New Payment Method [" + target.getPaymentMethod() + "]:", Expense.PAYMENT_METHODS);
        String desc = InputHelper.readOptionalString(scanner, "New Description [" + target.getDescription() + "]: ");

        try {
            Expense updated = expenseService.updateExpense(id, username, title, amount, category, date, payment, desc);
            System.out.println("\n[+] Expense #" + id + " successfully updated!");
            System.out.println("    " + updated);
        } catch (InvalidExpenseException | ExpenseNotFoundException e) {
            System.out.println("\n[!] Error updating expense: " + e.getMessage());
        }
    }

    private void userDeleteExpense(Scanner scanner, String username) {
        System.out.println("\n--- Delete An Expense ---");
        List<Expense> list = expenseService.getUserExpenses(username);
        if (list.isEmpty()) {
            System.out.println("You have no expenses recorded yet to delete.");
            return;
        }

        printExpenseTable(list);
        int id = InputHelper.readInt(scanner, "Enter the ID of the expense to delete: ", 1, Integer.MAX_VALUE);

        String confirm = InputHelper.readNonEmptyString(scanner, "Are you sure you want to permanently delete Expense #" + id + "? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y") || confirm.equalsIgnoreCase("YES")) {
            try {
                expenseService.deleteExpense(id, username);
                System.out.println("\n[+] Expense #" + id + " successfully deleted.");
            } catch (ExpenseNotFoundException e) {
                System.out.println("\n[!] Error: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void userFilterByCategory(Scanner scanner, String username) {
        System.out.println("\n--- Filter Expenses by Category ---");
        String category = InputHelper.selectFromList(scanner, "Choose Category to Filter:", Category.PREDEFINED_CATEGORIES);
        List<Expense> allUserExpenses = expenseService.getUserExpenses(username);
        List<Expense> filtered = expenseService.filterByCategory(allUserExpenses, category);

        System.out.println("\nFiltered Results for Category: " + category);
        printExpenseTable(filtered);
    }

    private void userViewTotal(String username) {
        double total = expenseService.getTotalExpenses(username);
        List<Expense> list = expenseService.getUserExpenses(username);
        System.out.println("\n==================================================");
        System.out.printf("  Total Expenses Recorded : %d entries%n", list.size());
        System.out.printf("  Cumulative Total Spent  : Rs. %.2f%n", total);
        System.out.println("==================================================");
    }

    private void userMonthlySummary(Scanner scanner, String username) {
        System.out.println("\n--- Monthly Expense Summary ---");
        System.out.println("1. View Current Month (" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + ")");
        System.out.println("2. Enter Specific Month (YYYY-MM)");
        System.out.println("3. View All Months Consolidated");
        int opt = InputHelper.readInt(scanner, "Choose an option (1-3): ", 1, 3);

        String my = null;
        if (opt == 1) {
            my = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        } else if (opt == 2) {
            my = InputHelper.readMonthYear(scanner, "Enter Month-Year (YYYY-MM): ");
        }

        reportService.generateUserMonthlySummary(username, my);
    }

    private void userSetBudget(Scanner scanner, String username) {
        System.out.println("\n--- Set Monthly Budget ---");
        String my = InputHelper.readMonthYear(scanner, "Enter Month-Year (YYYY-MM, e.g. " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + "): ");
        double amount = InputHelper.readPositiveDouble(scanner, "Enter Monthly Budget Limit (Rs.): ", 0.0);

        try {
            Budget b = budgetService.setBudget(username, my, amount);
            System.out.println("\n[+] Budget successfully configured: " + b);
        } catch (IllegalArgumentException e) {
            System.out.println("\n[!] Error configuring budget: " + e.getMessage());
        }
    }

    private void userViewBudgetStatus(Scanner scanner, String username) {
        System.out.println("\n--- View Budget & Remaining Status ---");
        String defaultMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String my = InputHelper.readMonthYear(scanner, "Enter Month-Year (YYYY-MM, default " + defaultMonth + "): ");

        List<Expense> userExpenses = expenseService.getUserExpenses(username);
        double monthSpent = 0.0;
        for (Expense e : userExpenses) {
            if (my.equals(e.getMonthYear())) {
                monthSpent += e.getAmount();
            }
        }

        System.out.println("\n" + budgetService.getBudgetStatusSummary(username, my, monthSpent));
    }

    private void userViewExpenseHistory(Scanner scanner, String username) {
        System.out.println("\n--- Personal Expense History (Chronological) ---");
        System.out.println("1. Newest First (Descending)");
        System.out.println("2. Oldest First (Ascending)");
        int sortOpt = InputHelper.readInt(scanner, "Choose sort order (1-2): ", 1, 2);

        List<Expense> list = expenseService.getUserExpenses(username);
        list = expenseService.sortExpensesByDate(list, sortOpt == 2);

        printExpenseTable(list);
    }

    private void userViewHighestLowest(String username) {
        List<Expense> list = expenseService.getUserExpenses(username);
        if (list.isEmpty()) {
            System.out.println("\nNo expenses found to calculate highest and lowest records.");
            return;
        }

        Expense high = expenseService.getHighestExpense(list);
        Expense low = expenseService.getLowestExpense(list);

        System.out.println("\n==================================================");
        System.out.println("  HIGHEST & LOWEST PERSONAL EXPENSE RECORDS       ");
        System.out.println("==================================================");
        System.out.println("Highest Single Expense:");
        System.out.println("  " + high);
        System.out.println("\nLowest Single Expense:");
        System.out.println("  " + low);
        System.out.println("==================================================");
    }

    // =========================================================================
    // ADMIN DASHBOARD (MONITORING & REPORTING ONLY)
    // =========================================================================

    private void handleAdminDashboard(Scanner scanner, Admin admin) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n==================================================");
            System.out.println("   ADMIN MONITORING DASHBOARD (READ-ONLY)         ");
            System.out.println("==================================================");
            System.out.println("1.  View All Registered Users");
            System.out.println("2.  View Total Number of Registered Users");
            System.out.println("3.  View All Expenses Across All Users");
            System.out.println("4.  Search Expenses by Username");
            System.out.println("5.  Filter Expenses by Category");
            System.out.println("6.  View Total Expenses Across All Users");
            System.out.println("7.  View Monthly Expense Reports");
            System.out.println("8.  View Category-Wise Spending Reports");
            System.out.println("9.  View Highest & Lowest Expense Records (System-Wide)");
            System.out.println("10. View Users' Monthly Budget Details");
            System.out.println("11. Launch Desktop GUI (JFrame Window)");
            System.out.println("12. Logout to Main Menu");
            System.out.println("--------------------------------------------------");

            int choice = InputHelper.readInt(scanner, "Choose an option (1-12): ", 1, 12);

            switch (choice) {
                case 1:
                    adminViewAllUsers();
                    break;
                case 2:
                    System.out.printf("%n[+] Total Registered Users in System: %d%n", authService.getTotalUsersCount());
                    break;
                case 3:
                    adminViewAllExpenses();
                    break;
                case 4:
                    adminSearchByUsername(scanner);
                    break;
                case 5:
                    adminFilterByCategory(scanner);
                    break;
                case 6:
                    System.out.printf("%n[+] Total Expenses Across All Users: Rs. %.2f%n",
                            expenseService.getTotalExpensesAllUsers());
                    break;
                case 7:
                    adminMonthlyReports(scanner);
                    break;
                case 8:
                    reportService.generateAdminCategoryReport();
                    break;
                case 9:
                    adminHighestLowest();
                    break;
                case 10:
                    adminViewAllBudgets();
                    break;
                case 11:
                    launchGuiMode();
                    break;
                case 12:
                    logout = true;
                    System.out.println("\n[+] Admin logged out. Returning to main menu.");
                    break;
            }
        }
    }

    private void adminViewAllUsers() {
        System.out.println("\n--- All Registered Users ---");
        List<User> users = authService.getAllUsers();
        System.out.printf("%-18s | %-24s | %-10s | %-12s%n", "Username", "Full Name", "Role", "Registered");
        System.out.println("-------------------------------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-18s | %-24s | %-10s | %-12s%n",
                    u.getUsername(), u.getFullName(), u.getRole(), u.getCreatedAt());
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("Total Users: %d%n", users.size());
    }

    private void adminViewAllExpenses() {
        System.out.println("\n--- All Expenses Across All Users (Monitoring Only) ---");
        List<Expense> all = expenseService.getAllExpenses();
        printExpenseTable(all, true);
    }

    private void adminSearchByUsername(Scanner scanner) {
        System.out.println("\n--- Search Expenses by Username ---");
        String query = InputHelper.readNonEmptyString(scanner, "Enter username search term: ");
        List<Expense> matches = expenseService.searchByUsername(query);
        System.out.printf("%nFound %d records matching username '%s':%n", matches.size(), query);
        printExpenseTable(matches, true);
    }

    private void adminFilterByCategory(Scanner scanner) {
        System.out.println("\n--- Filter All Expenses by Category ---");
        String category = InputHelper.selectFromList(scanner, "Choose Category:", Category.PREDEFINED_CATEGORIES);
        List<Expense> filtered = expenseService.filterByCategory(expenseService.getAllExpenses(), category);
        System.out.println("\nSystem-wide Expenses for Category: " + category);
        printExpenseTable(filtered, true);
    }

    private void adminMonthlyReports(Scanner scanner) {
        System.out.println("\n--- System-Wide Monthly Expense Reports ---");
        System.out.println("1. Consolidated View (All Months)");
        System.out.println("2. Specific Month (YYYY-MM)");
        int choice = InputHelper.readInt(scanner, "Choose option (1-2): ", 1, 2);

        String my = null;
        if (choice == 2) {
            my = InputHelper.readMonthYear(scanner, "Enter Month-Year (YYYY-MM): ");
        }
        reportService.generateAdminMonthlyReport(my);
    }

    private void adminHighestLowest() {
        List<Expense> all = expenseService.getAllExpenses();
        if (all.isEmpty()) {
            System.out.println("\nNo expense records on file in the system.");
            return;
        }

        Expense high = expenseService.getHighestExpense(all);
        Expense low = expenseService.getLowestExpense(all);

        System.out.println("\n==================================================");
        System.out.println("  SYSTEM-WIDE HIGHEST & LOWEST EXPENSE RECORDS    ");
        System.out.println("==================================================");
        System.out.printf("Highest Expense: %s by user '%s'%n", high.getTitle(), high.getUsername());
        System.out.printf("  Amount: Rs. %.2f | Category: %s | Date: %s | Payment: %s%n",
                high.getAmount(), high.getCategory(), high.getDate(), high.getPaymentMethod());

        System.out.printf("%nLowest Expense: %s by user '%s'%n", low.getTitle(), low.getUsername());
        System.out.printf("  Amount: Rs. %.2f | Category: %s | Date: %s | Payment: %s%n",
                low.getAmount(), low.getCategory(), low.getDate(), low.getPaymentMethod());
        System.out.println("==================================================");
    }

    private void adminViewAllBudgets() {
        System.out.println("\n--- Registered Monthly Budgets Across All Users ---");
        List<Budget> list = budgetService.getAllBudgets();
        if (list.isEmpty()) {
            System.out.println("No monthly budgets have been set by any users yet.");
            return;
        }

        System.out.printf("%-18s | %-12s | %-15s%n", "Username", "Month (YYYY-MM)", "Budget Limit");
        System.out.println("---------------------------------------------------------");
        for (Budget b : list) {
            System.out.printf("%-18s | %-15s | Rs. %12.2f%n",
                    b.getUsername(), b.getMonthYear(), b.getAmount());
        }
        System.out.println("---------------------------------------------------------");
        System.out.printf("Total Configured Budgets: %d%n", list.size());
    }

    // =========================================================================
    // HELPER TABLE PRINTERS
    // =========================================================================

    private void printExpenseTable(List<Expense> list) {
        printExpenseTable(list, false);
    }

    private void printExpenseTable(List<Expense> list, boolean showUsername) {
        if (list == null || list.isEmpty()) {
            System.out.println("  No expense records found.");
            return;
        }

        if (showUsername) {
            System.out.printf("  %-4s | %-12s | %-20s | %-12s | %-16s | %-10s | %-12s | %s%n",
                    "ID", "User", "Title", "Amount (Rs.)", "Category", "Date", "Payment", "Description");
            System.out.println("  ------------------------------------------------------------------------------------------------------------------------");
            for (Expense e : list) {
                System.out.printf("  #%-3d | %-12s | %-20s | Rs. %9.2f | %-16s | %-10s | %-12s | %s%n",
                        e.getId(), e.getUsername(), truncate(e.getTitle(), 20),
                        e.getAmount(), e.getCategory(), e.getDate(), e.getPaymentMethod(), e.getDescription());
            }
            System.out.println("  ------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.printf("  %-4s | %-22s | %-12s | %-16s | %-10s | %-12s | %s%n",
                    "ID", "Title", "Amount (Rs.)", "Category", "Date", "Payment", "Description");
            System.out.println("  ---------------------------------------------------------------------------------------------------------------");
            for (Expense e : list) {
                System.out.printf("  #%-3d | %-22s | Rs. %9.2f | %-16s | %-10s | %-12s | %s%n",
                        e.getId(), truncate(e.getTitle(), 22),
                        e.getAmount(), e.getCategory(), e.getDate(), e.getPaymentMethod(), e.getDescription());
            }
            System.out.println("  ---------------------------------------------------------------------------------------------------------------");
        }
        System.out.printf("  Total Records: %d%n", list.size());
    }

    private String truncate(String str, int max) {
        if (str == null) return "";
        if (str.length() <= max) return str;
        return str.substring(0, max - 3) + "...";
    }
}
