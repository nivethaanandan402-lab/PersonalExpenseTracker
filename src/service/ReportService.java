package service;

import model.Budget;
import model.Expense;

import java.util.List;

/**
 * Service Class: ReportService
 * 
 * OOP Concepts Demonstrated:
 * 1. Polymorphism via Interface (Reportable):
 *    - Delegates report execution polymorphically through the {@link Reportable} interface.
 *    - Calls 'report.generateReport()' seamlessly across MonthlyReport, CategoryWiseReport,
 *      and UserSummaryReport.
 * 2. High Cohesion & Single Responsibility Principle:
 *    - Separates presentation/reporting logic from data access and storage services.
 */
public class ReportService {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;

    public ReportService(ExpenseService expenseService, BudgetService budgetService) {
        this.expenseService = expenseService;
        this.budgetService = budgetService;
    }

    /**
     * Executes report polymorphically.
     *
     * @param report Instance of any class implementing Reportable
     */
    public void printReport(Reportable report) {
        if (report != null) {
            report.generateReport();
        }
    }

    /**
     * Generates a monthly expense summary for a user.
     */
    public void generateUserMonthlySummary(String username, String monthYear) {
        List<Expense> list = expenseService.getUserExpenses(username);
        Reportable report = new MonthlyReport(username, monthYear, list);
        printReport(report);
    }

    /**
     * Generates a category-wise spending summary for a user.
     */
    public void generateUserCategorySummary(String username) {
        List<Expense> list = expenseService.getUserExpenses(username);
        Reportable report = new CategoryWiseReport(username, list);
        printReport(report);
    }

    /**
     * Generates a full financial overview report for a user.
     */
    public void generateUserComprehensiveSummary(String username, String monthYear) {
        List<Expense> list = expenseService.getUserExpenses(username);
        Budget budget = budgetService.getBudget(username, monthYear);
        Reportable report = new UserSummaryReport(username, list, budget);
        printReport(report);
    }

    /**
     * Admin Feature: Generates system-wide monthly report across all users.
     */
    public void generateAdminMonthlyReport(String monthYear) {
        List<Expense> all = expenseService.getAllExpenses();
        Reportable report = new MonthlyReport(null, monthYear, all);
        printReport(report);
    }

    /**
     * Admin Feature: Generates system-wide category breakdown across all users.
     */
    public void generateAdminCategoryReport() {
        List<Expense> all = expenseService.getAllExpenses();
        Reportable report = new CategoryWiseReport(null, all);
        printReport(report);
    }
}
