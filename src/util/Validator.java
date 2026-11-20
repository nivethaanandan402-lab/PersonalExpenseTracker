package util;

import exception.InvalidExpenseException;
import model.Category;
import model.Expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility Class: Validator
 * 
 * OOP Concepts Demonstrated:
 * 1. Static Members & 'final' Keyword:
 *    - Utility class containing pure static validation routines without needing object instantiation.
 *    - Private constructor prevents instantiation.
 * 2. Method Overloading:
 *    - validateAmount(double amount) vs validateAmount(String amountStr)
 *    - validateDate(String dateStr) vs validateDate(String dateStr, boolean allowFuture)
 * 3. Exception Handling:
 *    - Throws checked {@link InvalidExpenseException} when validation criteria are violated.
 */
public final class Validator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    // Private constructor prevents instantiation (Design Best Practice)
    private Validator() {}

    /**
     * Validates that username is non-empty and contains alphanumeric characters.
     *
     * @param username Given username
     * @return true if valid
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * Validates that password has at least 6 characters.
     * Rule: "Password must contain at least 6 characters."
     *
     * @param password Account password
     * @return true if valid
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validates an expense title.
     * Rule: "Expense name must not be empty."
     *
     * @param title Name of expense
     * @return true if valid
     */
    public static boolean isValidExpenseName(String title) {
        return title != null && !title.trim().isEmpty() && title.trim().length() <= 100;
    }

    /**
     * Method Overloading (Variant 1: double parameter).
     * Validates that amount is strictly greater than 0.
     * Rule: "Expense amount must be greater than 0."
     *
     * @param amount Numeric value
     * @return true if amount > 0
     */
    public static boolean validateAmount(double amount) {
        return amount > 0.0;
    }

    /**
     * Method Overloading (Variant 2: String parameter).
     * Parses and validates monetary string.
     *
     * @param amountStr String representation of amount
     * @return parsed double amount
     * @throws InvalidExpenseException if format is invalid or amount <= 0
     */
    public static double validateAmount(String amountStr) throws InvalidExpenseException {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            throw new InvalidExpenseException("Expense amount cannot be empty.");
        }
        try {
            double val = Double.parseDouble(amountStr.trim());
            if (!validateAmount(val)) {
                throw new InvalidExpenseException("Expense amount must be greater than 0.");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new InvalidExpenseException("Expense amount must be a valid positive number.");
        }
    }

    /**
     * Validates that category is one of the predefined categories.
     * Rule: "Expense category must be selected from the predefined list."
     *
     * @param category Category name
     * @return true if matched
     */
    public static boolean isValidCategory(String category) {
        if (category == null) return false;
        for (String c : Category.PREDEFINED_CATEGORIES) {
            if (c.equalsIgnoreCase(category.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates that payment method is in the predefined list.
     *
     * @param method Payment method name
     * @return true if matched
     */
    public static boolean isValidPaymentMethod(String method) {
        if (method == null) return false;
        for (String m : Expense.PAYMENT_METHODS) {
            if (m.equalsIgnoreCase(method.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method Overloading (Variant 1: single parameter).
     * Validates date format (YYYY-MM-DD) and calendar correctness.
     * Rule: "Date must be in a valid format (YYYY-MM-DD)."
     *
     * @param dateStr Date string
     * @return true if valid
     */
    public static boolean validateDate(String dateStr) {
        return validateDate(dateStr, true);
    }

    /**
     * Method Overloading (Variant 2: with boolean allowFuture flag).
     * Demonstrates Method Overloading.
     *
     * @param dateStr     Date string
     * @param allowFuture whether future dates are permitted
     * @return true if valid
     */
    public static boolean validateDate(String dateStr, boolean allowFuture) {
        if (dateStr == null || dateStr.trim().length() != 10) {
            return false;
        }
        try {
            LocalDate parsed = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            if (!allowFuture && parsed.isAfter(LocalDate.now())) {
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates month-year format (YYYY-MM).
     *
     * @param monthYear String such as "2026-11"
     * @return true if valid
     */
    public static boolean isValidMonthYear(String monthYear) {
        if (monthYear == null || monthYear.trim().length() != 7) {
            return false;
        }
        try {
            LocalDate.parse(monthYear.trim() + "-01", DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates description length (optional, max 200 chars).
     * Rule: "Description is optional, with a maximum of 200 characters."
     *
     * @param description Text description
     * @return true if valid
     */
    public static boolean isValidDescription(String description) {
        return description == null || description.length() <= 200;
    }

    /**
     * Comprehensive validation of all expense fields.
     * Throws {@link InvalidExpenseException} detailing the specific violation.
     */
    public static void validateExpense(String title, double amount, String category,
                                        String date, String paymentMethod, String description)
            throws InvalidExpenseException {
        if (!isValidExpenseName(title)) {
            throw new InvalidExpenseException("Expense name cannot be empty (maximum 100 characters).");
        }
        if (!validateAmount(amount)) {
            throw new InvalidExpenseException("Expense amount must be strictly greater than 0.");
        }
        if (!isValidCategory(category)) {
            throw new InvalidExpenseException("Category must be one of: " + Category.PREDEFINED_CATEGORIES);
        }
        if (!validateDate(date)) {
            throw new InvalidExpenseException("Date must be in valid YYYY-MM-DD format (e.g., 2026-11-20).");
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new InvalidExpenseException("Payment method must be one of: " + Expense.PAYMENT_METHODS);
        }
        if (!isValidDescription(description)) {
            throw new InvalidExpenseException("Description cannot exceed 200 characters.");
        }
    }
}
