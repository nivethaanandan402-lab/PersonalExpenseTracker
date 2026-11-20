package service;

import exception.ExpenseNotFoundException;
import exception.InvalidExpenseException;
import model.Expense;
import storage.FileStorage;
import storage.Storage;
import util.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Class: ExpenseService
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: Manages expense collections, automatic ID generation, and disk persistence.
 * 2. Method Overloading:
 *    - addExpense(String, String, double, String, String, String)
 *    - addExpense(String, String, double, String, String, String, String)
 * 3. Exception Handling:
 *    - Throws checked {@link InvalidExpenseException} on validation failure.
 *    - Throws checked {@link ExpenseNotFoundException} when updating/deleting non-existent/unauthorized records.
 * 4. Collections, Comparator & Sorting:
 *    - ArrayList for storing expenses.
 *    - Comparator.comparing() for chronological and monetary sorting.
 *    - Collections.max() / Collections.min() to determine extreme values.
 */
public class ExpenseService {

    private final Storage<Expense> expenseStorage;
    private final List<Expense> expenses;
    private int nextId;

    public ExpenseService() {
        this.expenseStorage = new FileStorage.ExpenseFileStorage();
        this.expenses = new ArrayList<>();
        reloadExpenses();
    }

    /**
     * Loads expenses from CSV and calculates next auto-increment ID.
     */
    public synchronized void reloadExpenses() {
        expenses.clear();
        try {
            List<Expense> loaded = expenseStorage.loadAll();
            expenses.addAll(loaded);

            int maxId = 0;
            for (Expense e : loaded) {
                if (e.getId() > maxId) {
                    maxId = e.getId();
                }
            }
            this.nextId = maxId + 1;
        } catch (IOException e) {
            System.err.println("Error loading expenses from storage: " + e.getMessage());
            this.nextId = 1;
        }
    }

    /**
     * Method Overloading (Variant 1: without optional description).
     * Demonstrates Method Overloading by redirecting to Variant 2 with default "None".
     */
    public synchronized Expense addExpense(String username, String title, double amount,
                                           String category, String date, String paymentMethod)
            throws InvalidExpenseException {
        return addExpense(username, title, amount, category, date, paymentMethod, "None");
    }

    /**
     * Method Overloading (Variant 2: with full parameters including description).
     * Validates input, creates new Expense entity, appends to collection, and persists.
     */
    public synchronized Expense addExpense(String username, String title, double amount,
                                           String category, String date, String paymentMethod,
                                           String description)
            throws InvalidExpenseException {
        Validator.validateExpense(title, amount, category, date, paymentMethod, description);

        Expense newExpense = new Expense(nextId++, username, title.trim(), amount,
                category.trim(), date.trim(), paymentMethod.trim(), description);
        expenses.add(newExpense);
        persistExpenses();
        return newExpense;
    }

    /**
     * Updates an existing expense owned by the user.
     * Enforces ownership rule: "Users can only view, update, or delete their own expenses."
     */
    public synchronized Expense updateExpense(int id, String username, String newTitle,
                                              double newAmount, String newCategory,
                                              String newDate, String newPaymentMethod,
                                              String newDesc)
            throws InvalidExpenseException, ExpenseNotFoundException {

        Validator.validateExpense(newTitle, newAmount, newCategory, newDate, newPaymentMethod, newDesc);

        Expense target = findUserExpenseById(id, username);
        if (target == null) {
            throw new ExpenseNotFoundException("Expense #" + id + " not found or you do not have permission to modify it.");
        }

        target.setTitle(newTitle.trim());
        target.setAmount(newAmount);
        target.setCategory(newCategory.trim());
        target.setDate(newDate.trim());
        target.setPaymentMethod(newPaymentMethod.trim());
        target.setDescription(newDesc != null ? newDesc.trim() : "None");

        persistExpenses();
        return target;
    }

    /**
     * Deletes an expense by ID if it belongs to the specified user.
     */
    public synchronized boolean deleteExpense(int id, String username) throws ExpenseNotFoundException {
        Expense target = findUserExpenseById(id, username);
        if (target == null) {
            throw new ExpenseNotFoundException("Expense #" + id + " not found or you do not have permission to delete it.");
        }
        boolean removed = expenses.remove(target);
        if (removed) {
            persistExpenses();
        }
        return removed;
    }

    /**
     * Finds an expense belonging to a specific user.
     */
    public Expense findUserExpenseById(int id, String username) {
        for (Expense e : expenses) {
            if (e.getId() == id && e.getUsername().equalsIgnoreCase(username)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Returns all expenses owned by a specific user.
     */
    public List<Expense> getUserExpenses(String username) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getUsername().equalsIgnoreCase(username)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Filters a given expense list by category.
     */
    public List<Expense> filterByCategory(List<Expense> list, String category) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : list) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Calculates total sum of expenses for a user.
     */
    public double getTotalExpenses(String username) {
        double sum = 0.0;
        for (Expense e : getUserExpenses(username)) {
            sum += e.getAmount();
        }
        return sum;
    }

    /**
     * Admin Feature: Calculates total expenses across all users.
     */
    public double getTotalExpensesAllUsers() {
        double sum = 0.0;
        for (Expense e : expenses) {
            sum += e.getAmount();
        }
        return sum;
    }

    /**
     * Admin Feature: Returns all expenses across all users.
     */
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * Admin Feature: Searches expenses by username.
     */
    public List<Expense> searchByUsername(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String q = query.trim().toLowerCase();
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getUsername().toLowerCase().contains(q)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Demonstrates Collections & Comparator: Sorts list of expenses by date.
     *
     * @param list      Expenses to sort
     * @param ascending true for oldest first, false for newest first
     * @return Sorted list
     */
    public List<Expense> sortExpensesByDate(List<Expense> list, boolean ascending) {
        List<Expense> copy = new ArrayList<>(list);
        if (ascending) {
            copy.sort(Comparator.comparing(Expense::getDate));
        } else {
            copy.sort(Comparator.comparing(Expense::getDate).reversed());
        }
        return copy;
    }

    /**
     * Demonstrates Collections & Comparator: Sorts list of expenses by amount.
     */
    public List<Expense> sortExpensesByAmount(List<Expense> list, boolean descending) {
        List<Expense> copy = new ArrayList<>(list);
        if (descending) {
            copy.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
        } else {
            copy.sort(Comparator.comparingDouble(Expense::getAmount));
        }
        return copy;
    }

    /**
     * Finds highest expense record among a list of expenses.
     */
    public Expense getHighestExpense(List<Expense> list) {
        if (list == null || list.isEmpty()) return null;
        return Collections.max(list, Comparator.comparingDouble(Expense::getAmount));
    }

    /**
     * Finds lowest expense record among a list of expenses.
     */
    public Expense getLowestExpense(List<Expense> list) {
        if (list == null || list.isEmpty()) return null;
        return Collections.min(list, Comparator.comparingDouble(Expense::getAmount));
    }

    /**
     * Persists expenses to CSV file.
     */
    public synchronized void persistExpenses() {
        try {
            expenseStorage.saveAll(expenses);
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }
}
