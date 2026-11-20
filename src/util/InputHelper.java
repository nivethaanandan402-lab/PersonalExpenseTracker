package util;

import java.util.List;
import java.util.Scanner;

/**
 * Utility Class: InputHelper
 * 
 * OOP Concepts Demonstrated:
 * 1. Static Methods & Helper Utilities:
 *    - Centralizes Scanner interaction, robust input parsing, and error-recovery loops.
 * 2. Robust Exception Handling:
 *    - Handles NumberFormatException, InputMismatchException, and invalid bounds gracefully
 *      without terminating the console application.
 */
public final class InputHelper {

    private InputHelper() {}

    /**
     * Reads a non-empty string from console.
     *
     * @param scanner Scanner instance
     * @param prompt  Prompt message
     * @return trimmed non-empty string
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
            System.out.println("  [!] Input cannot be empty. Please enter a valid value.");
        }
    }

    /**
     * Reads an optional string (can be empty, defaults to "None").
     *
     * @param scanner Scanner instance
     * @param prompt  Prompt message
     * @return String or default value
     */
    public static String readOptionalString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (input == null || input.trim().isEmpty()) {
            return "None";
        }
        return input.trim();
    }

    /**
     * Reads an integer within a specified range [min, max].
     *
     * @param scanner Scanner instance
     * @param prompt  Prompt message
     * @param min     Minimum allowed integer
     * @param max     Maximum allowed integer
     * @return Validated integer
     */
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                int val = Integer.parseInt(line.trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("  [!] Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid numeric format. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a positive double greater than min.
     *
     * @param scanner Scanner instance
     * @param prompt  Prompt message
     * @param min     Exclusive lower bound
     * @return Validated double
     */
    public static double readPositiveDouble(Scanner scanner, String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                double val = Double.parseDouble(line.trim());
                if (val > min) {
                    return val;
                }
                System.out.printf("  [!] Amount must be strictly greater than %.2f.%n", min);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid monetary value. Please enter a valid decimal number.");
            }
        }
    }

    /**
     * Reads a valid date in YYYY-MM-DD format.
     *
     * @param scanner Scanner instance
     * @param prompt  Prompt message
     * @return Validated date string
     */
    public static String readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (Validator.validateDate(line)) {
                return line;
            }
            System.out.println("  [!] Invalid date format or non-existent calendar date. Use YYYY-MM-DD (e.g. 2026-11-20).");
        }
    }

    /**
     * Reads a valid month-year in YYYY-MM format.
     *
     * @param scanner Scanner instance
     * @param prompt  Prompt message
     * @return Validated month-year string
     */
    public static String readMonthYear(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (Validator.isValidMonthYear(line)) {
                return line;
            }
            System.out.println("  [!] Invalid month format. Use YYYY-MM (e.g. 2026-11).");
        }
    }

    /**
     * Displays a numbered list of choices and prompts user to pick one.
     *
     * @param scanner Scanner instance
     * @param title   Header title
     * @param options List of available options
     * @return Selected option string
     */
    public static String selectFromList(Scanner scanner, String title, List<String> options) {
        System.out.println(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  %d. %s%n", (i + 1), options.get(i));
        }
        int choice = readInt(scanner, "Choose an option (1-" + options.size() + "): ", 1, options.size());
        return options.get(choice - 1);
    }
}
