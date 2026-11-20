package storage;

import model.Admin;
import model.Budget;
import model.Expense;
import model.RegularUser;
import model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorage and Entity-Specific File Storage Implementations
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface Implementation & Polymorphism:
 *    - UserFileStorage implements Storage<User>
 *    - ExpenseFileStorage implements Storage<Expense>
 *    - BudgetFileStorage implements Storage<Budget>
 * 2. Exception Handling:
 *    - Demonstrates 'try-with-resources' to guarantee automatic closure of file streams.
 *    - Catches and propagates standard 'java.io.IOException'.
 * 3. Encapsulation:
 *    - File paths and CSV parsing rules are kept internal and private to each storage handler.
 * 4. Static Helpers:
 *    - Centralized directory verification and CSV escaping.
 */
public class FileStorage {

    public static final String DATA_DIR = "data";

    /**
     * Ensures the data directory exists.
     */
    public static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Escapes CSV values to handle commas and quotation marks safely.
     *
     * @param value Raw text string
     * @return Escaped CSV column value
     */
    public static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String clean = value.replace("\r", " ").replace("\n", " ");
        if (clean.contains(",") || clean.contains("\"")) {
            clean = "\"" + clean.replace("\"", "\"\"") + "\"";
        }
        return clean;
    }

    /**
     * Splits a CSV line while respecting quoted commas.
     *
     * @param line Raw CSV line
     * @return Array of parsed tokens
     */
    public static String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++; // skip escaped quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }

    // =========================================================================
    // 1. User Storage Implementation (implements Storage<User>)
    // =========================================================================
    public static class UserFileStorage implements Storage<User> {
        private final File file;

        public UserFileStorage() {
            ensureDataDirectory();
            this.file = new File(DATA_DIR, "users.csv");
            initializeDefaultAdminIfEmpty();
        }

        private void initializeDefaultAdminIfEmpty() {
            if (!file.exists() || file.length() == 0) {
                try {
                    List<User> defaults = new ArrayList<>();
                    // Default admin requested: "Admin can log in using a default admin account created on the first run."
                    defaults.add(new Admin("admin", "admin123", "System Administrator", "2026-01-01"));
                    saveAll(defaults);
                } catch (IOException e) {
                    System.err.println("Warning: Failed to create default admin account in " + file.getName());
                }
            }
        }

        @Override
        public void saveAll(List<User> items) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write("username,password,role,fullName,createdAt");
                writer.newLine();
                for (User u : items) {
                    writer.write(String.format("%s,%s,%s,%s,%s",
                            escapeCsv(u.getUsername()),
                            escapeCsv(u.getPassword()),
                            escapeCsv(u.getRole()),
                            escapeCsv(u.getFullName()),
                            escapeCsv(u.getCreatedAt())
                    ));
                    writer.newLine();
                }
            }
        }

        @Override
        public List<User> loadAll() throws IOException {
            List<User> list = new ArrayList<>();
            if (!file.exists()) {
                return list;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine(); // skip header
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = parseCsvLine(line);
                    if (parts.length >= 5) {
                        String username = parts[0];
                        String password = parts[1];
                        String role = parts[2];
                        String fullName = parts[3];
                        String createdAt = parts[4];

                        if ("ADMIN".equalsIgnoreCase(role)) {
                            list.add(new Admin(username, password, fullName, createdAt));
                        } else {
                            list.add(new RegularUser(username, password, fullName, createdAt));
                        }
                    }
                }
            }
            return list;
        }
    }

    // =========================================================================
    // 2. Expense Storage Implementation (implements Storage<Expense>)
    // =========================================================================
    public static class ExpenseFileStorage implements Storage<Expense> {
        private final File file;

        public ExpenseFileStorage() {
            ensureDataDirectory();
            this.file = new File(DATA_DIR, "expenses.csv");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ignored) {}
            }
        }

        @Override
        public void saveAll(List<Expense> items) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write("id,username,title,amount,category,date,paymentMethod,description");
                writer.newLine();
                for (Expense exp : items) {
                    writer.write(String.format("%d,%s,%s,%.2f,%s,%s,%s,%s",
                            exp.getId(),
                            escapeCsv(exp.getUsername()),
                            escapeCsv(exp.getTitle()),
                            exp.getAmount(),
                            escapeCsv(exp.getCategory()),
                            escapeCsv(exp.getDate()),
                            escapeCsv(exp.getPaymentMethod()),
                            escapeCsv(exp.getDescription())
                    ));
                    writer.newLine();
                }
            }
        }

        @Override
        public List<Expense> loadAll() throws IOException {
            List<Expense> list = new ArrayList<>();
            if (!file.exists()) {
                return list;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine(); // skip header
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = parseCsvLine(line);
                    if (parts.length >= 8) {
                        try {
                            int id = Integer.parseInt(parts[0]);
                            String username = parts[1];
                            String title = parts[2];
                            double amount = Double.parseDouble(parts[3]);
                            String category = parts[4];
                            String date = parts[5];
                            String paymentMethod = parts[6];
                            String description = parts[7];

                            list.add(new Expense(id, username, title, amount, category, date, paymentMethod, description));
                        } catch (NumberFormatException e) {
                            // Skip corrupted row gracefully
                        }
                    }
                }
            }
            return list;
        }
    }

    // =========================================================================
    // 3. Budget Storage Implementation (implements Storage<Budget>)
    // =========================================================================
    public static class BudgetFileStorage implements Storage<Budget> {
        private final File file;

        public BudgetFileStorage() {
            ensureDataDirectory();
            this.file = new File(DATA_DIR, "budgets.csv");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ignored) {}
            }
        }

        @Override
        public void saveAll(List<Budget> items) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write("username,monthYear,amount");
                writer.newLine();
                for (Budget b : items) {
                    writer.write(String.format("%s,%s,%.2f",
                            escapeCsv(b.getUsername()),
                            escapeCsv(b.getMonthYear()),
                            b.getAmount()
                    ));
                    writer.newLine();
                }
            }
        }

        @Override
        public List<Budget> loadAll() throws IOException {
            List<Budget> list = new ArrayList<>();
            if (!file.exists()) {
                return list;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine(); // skip header
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = parseCsvLine(line);
                    if (parts.length >= 3) {
                        try {
                            String username = parts[0];
                            String monthYear = parts[1];
                            double amount = Double.parseDouble(parts[2]);
                            list.add(new Budget(username, monthYear, amount));
                        } catch (NumberFormatException e) {
                            // Skip corrupted row gracefully
                        }
                    }
                }
            }
            return list;
        }
    }
}
