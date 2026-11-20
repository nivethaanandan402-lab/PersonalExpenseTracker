package main;

import service.AuthService;
import service.BudgetService;
import service.ExpenseService;
import service.ReportService;
import ui.PersonalExpenseTrackerGUI;

/**
 * Direct Desktop GUI Launcher: GuiMain
 * 
 * Runs the Swing JFrame GUI directly.
 * Command to run:
 *   java -cp bin main.GuiMain
 */
public class GuiMain {

    public static void main(String[] args) {
        System.out.println("Starting Personal Expense Tracker Desktop GUI (Swing JFrame)...");
        AuthService authService = new AuthService();
        ExpenseService expenseService = new ExpenseService();
        BudgetService budgetService = new BudgetService();
        ReportService reportService = new ReportService(expenseService, budgetService);

        PersonalExpenseTrackerGUI.launch(authService, expenseService, budgetService, reportService);
    }
}
