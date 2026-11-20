package ui;

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
import service.BudgetService;
import service.ExpenseService;
import service.ReportService;
import util.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Desktop Graphical User Interface: PersonalExpenseTrackerGUI
 * 
 * Built with pure Java Swing (javax.swing.JFrame).
 * 
 * OOP Concepts Demonstrated:
 * 1. Inheritance: Extends {@link javax.swing.JFrame} to inherit full desktop window capabilities.
 * 2. Event-Driven Programming & Polymorphism:
 *    - Implements action listeners via lambdas and inner classes.
 * 3. Layered Architecture:
 *    - GUI layer interacts solely with the backend Service layer (AuthService, ExpenseService,
 *      BudgetService, ReportService), ensuring complete separation of concerns.
 * 4. Role-Based View Switching:
 *    - Dynamic UI rendering based on polymorphic user type (Admin vs RegularUser).
 * 5. Exception Handling:
 *    - Catches custom checked exceptions and displays user-friendly dialog alerts via JOptionPane.
 */
public class PersonalExpenseTrackerGUI extends JFrame {

    private final AuthService authService;
    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final ReportService reportService;

    // Currently logged-in user
    private User currentUser;

    // Main layout container (CardLayout for swapping Login vs Dashboard)
    private final JPanel rootPanel;
    private final CardLayout cardLayout;

    // Panels
    private JPanel loginCard;
    private JPanel userDashboardCard;
    private JPanel adminDashboardCard;

    // User Dashboard UI controls
    private DefaultTableModel userExpenseTableModel;
    private JTable userExpenseTable;
    private JComboBox<String> userCategoryFilterCombo;
    private JLabel userTotalSpentLabel;
    private JLabel userBudgetStatusLabel;
    private DefaultTableModel userCategorySummaryModel;
    private DefaultTableModel userMonthlySummaryModel;

    // User Add Expense fields
    private JTextField txtTitle;
    private JTextField txtAmount;
    private JComboBox<String> cmbCategory;
    private JTextField txtDate;
    private JComboBox<String> cmbPaymentMethod;
    private JTextField txtDescription;

    // User Budget fields
    private JTextField txtBudgetMonth;
    private JTextField txtBudgetAmount;

    // Admin Dashboard UI controls
    private DefaultTableModel adminExpenseTableModel;
    private JTable adminExpenseTable;
    private JTextField adminSearchUserField;
    private JComboBox<String> adminCategoryFilterCombo;
    private JLabel adminTotalSystemSpentLabel;
    private JLabel adminTotalUsersLabel;
    private JLabel adminHighestExpenseLabel;
    private JLabel adminLowestExpenseLabel;
    private DefaultTableModel adminUsersTableModel;
    private DefaultTableModel adminBudgetsTableModel;

    public PersonalExpenseTrackerGUI(AuthService authService, ExpenseService expenseService,
                                     BudgetService budgetService, ReportService reportService) {
        super("Personal Expense Tracker - Desktop Application");
        this.authService = authService;
        this.expenseService = expenseService;
        this.budgetService = budgetService;
        this.reportService = reportService;

        // Configure Frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null); // Center on screen

        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        // Build Login Card
        buildLoginCard();
        rootPanel.add(loginCard, "LOGIN");

        add(rootPanel);
        cardLayout.show(rootPanel, "LOGIN");
    }

    // =========================================================================
    // 1. LOGIN / REGISTRATION CARD
    // =========================================================================
    private void buildLoginCard() {
        loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(new Color(245, 247, 250));

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel titleLabel = new JLabel("Personal Expense Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(40, 50, 75));

        JLabel subLabel = new JLabel("College OOP Mini-Project | Java Swing GUI");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subLabel.setForeground(Color.GRAY);

        box.add(titleLabel);
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        box.add(subLabel);
        box.add(Box.createRigidArea(new Dimension(0, 25)));

        // Input Fields
        JTextField txtUsername = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);

        JPanel userPanel = new JPanel(new BorderLayout(5, 5));
        userPanel.setBackground(Color.WHITE);
        userPanel.add(new JLabel("Username:"), BorderLayout.NORTH);
        userPanel.add(txtUsername, BorderLayout.CENTER);

        JPanel passPanel = new JPanel(new BorderLayout(5, 5));
        passPanel.setBackground(Color.WHITE);
        passPanel.add(new JLabel("Password (min 6 chars):"), BorderLayout.NORTH);
        passPanel.add(txtPassword, BorderLayout.CENTER);

        box.add(userPanel);
        box.add(Box.createRigidArea(new Dimension(0, 12)));
        box.add(passPanel);
        box.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JButton btnRegister = new JButton("Register New Account");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setFocusPainted(false);
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel hintLabel = new JLabel("<html><center><font color='#888888'>Default Admin: <b>admin</b> / <b>admin123</b></font></center></html>");
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(btnLogin);
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        box.add(btnRegister);
        box.add(Box.createRigidArea(new Dimension(0, 15)));
        box.add(hintLabel);

        // Login Action
        btnLogin.addActionListener(e -> {
            String u = txtUsername.getText().trim();
            String p = new String(txtPassword.getPassword());
            try {
                currentUser = authService.login(u, p);
                txtPassword.setText("");
                txtUsername.setText("");
                openDashboardForUser(currentUser);
            } catch (InvalidLoginException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register Action
        btnRegister.addActionListener(e -> {
            showRegistrationDialog();
        });

        loginCard.add(box);
    }

    private void showRegistrationDialog() {
        JTextField regUser = new JTextField();
        JPasswordField regPass = new JPasswordField();
        JTextField regName = new JTextField();

        Object[] message = {
                "Full Name:", regName,
                "Username (Alphanumeric, 3-20 chars):", regUser,
                "Password (At least 6 chars):", regPass
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New Account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String u = regUser.getText().trim();
            String p = new String(regPass.getPassword());
            String n = regName.getText().trim();

            try {
                authService.register(u, p, n);
                JOptionPane.showMessageDialog(this,
                        "Registration successful! You can now log in as: " + u,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (DuplicateUserException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openDashboardForUser(User user) {
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            buildAdminDashboard();
            rootPanel.add(adminDashboardCard, "ADMIN_DASHBOARD");
            cardLayout.show(rootPanel, "ADMIN_DASHBOARD");
        } else {
            buildUserDashboard();
            rootPanel.add(userDashboardCard, "USER_DASHBOARD");
            cardLayout.show(rootPanel, "USER_DASHBOARD");
        }
    }

    // =========================================================================
    // 2. USER DASHBOARD
    // =========================================================================
    private void buildUserDashboard() {
        userDashboardCard = new JPanel(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel lblWelcome = new JLabel("Welcome, " + currentUser.getFullName() + " (" + currentUser.getUsername() + ")");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWelcome.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(rootPanel, "LOGIN");
        });

        headerPanel.add(lblWelcome, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        userDashboardCard.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane for User Features
        JTabbedPane userTabs = new JTabbedPane();
        userTabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Tab 1: Manage Expenses
        userTabs.addTab("Manage Expenses", buildUserExpenseManagementPanel());

        // Tab 2: Monthly Budget & Status
        userTabs.addTab("Monthly Budget & Status", buildUserBudgetPanel());

        // Tab 3: Analytics & Summaries
        userTabs.addTab("Summaries & Reports", buildUserReportsPanel());

        userDashboardCard.add(userTabs, BorderLayout.CENTER);

        // Load initial data
        refreshUserExpenseTable();
        refreshUserSummaries();
    }

    private JPanel buildUserExpenseManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left: Add Expense Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));
        formPanel.setPreferredSize(new Dimension(300, 500));

        txtTitle = new JTextField();
        txtAmount = new JTextField();
        cmbCategory = new JComboBox<>(Category.PREDEFINED_CATEGORIES.toArray(new String[0]));
        txtDate = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        cmbPaymentMethod = new JComboBox<>(Expense.PAYMENT_METHODS.toArray(new String[0]));
        txtDescription = new JTextField();

        formPanel.add(new JLabel("Expense Name / Title:"));
        formPanel.add(txtTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        formPanel.add(new JLabel("Amount (Rs.):"));
        formPanel.add(txtAmount);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        formPanel.add(new JLabel("Category:"));
        formPanel.add(cmbCategory);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(txtDate);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        formPanel.add(new JLabel("Payment Method:"));
        formPanel.add(cmbPaymentMethod);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        formPanel.add(new JLabel("Description (Optional, max 200 chars):"));
        formPanel.add(txtDescription);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JButton btnAddExpense = new JButton("+ Add Expense");
        btnAddExpense.setBackground(new Color(39, 174, 96));
        btnAddExpense.setForeground(Color.WHITE);
        btnAddExpense.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddExpense.setFocusPainted(false);
        btnAddExpense.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnAddExpense.addActionListener(e -> onAddExpenseClicked());
        formPanel.add(btnAddExpense);

        panel.add(formPanel, BorderLayout.WEST);

        // Center: Expense Table & Action Toolbar
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        // Top Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filterBar.add(new JLabel("Filter by Category:"));
        String[] filterOptions = new String[Category.PREDEFINED_CATEGORIES.size() + 1];
        filterOptions[0] = "All Categories";
        for (int i = 0; i < Category.PREDEFINED_CATEGORIES.size(); i++) {
            filterOptions[i + 1] = Category.PREDEFINED_CATEGORIES.get(i);
        }
        userCategoryFilterCombo = new JComboBox<>(filterOptions);
        userCategoryFilterCombo.addActionListener(e -> refreshUserExpenseTable());
        filterBar.add(userCategoryFilterCombo);

        JButton btnSortDate = new JButton("Sort by Date (Newest)");
        btnSortDate.addActionListener(e -> sortUserExpensesByDate());
        filterBar.add(btnSortDate);

        JButton btnSortAmount = new JButton("Sort by Amount (Highest)");
        btnSortAmount.addActionListener(e -> sortUserExpensesByAmount());
        filterBar.add(btnSortAmount);

        centerPanel.add(filterBar, BorderLayout.NORTH);

        // Expense Table
        String[] columns = {"ID", "Title", "Amount (Rs.)", "Category", "Date", "Payment", "Description"};
        userExpenseTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only cells
            }
        };
        userExpenseTable = new JTable(userExpenseTableModel);
        userExpenseTable.setRowHeight(22);
        userExpenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        centerPanel.add(new JScrollPane(userExpenseTable), BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomBar = new JPanel(new BorderLayout());
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        JButton btnEdit = new JButton("Update Selected Expense");
        JButton btnDelete = new JButton("Delete Selected Expense");
        JButton btnRefresh = new JButton("Refresh");

        btnEdit.addActionListener(e -> onEditSelectedExpense());
        btnDelete.addActionListener(e -> onDeleteSelectedExpense());
        btnRefresh.addActionListener(e -> {
            refreshUserExpenseTable();
            refreshUserSummaries();
        });

        buttonGroup.add(btnEdit);
        buttonGroup.add(btnDelete);
        buttonGroup.add(btnRefresh);

        userTotalSpentLabel = new JLabel("Total Spent: Rs. 0.00  ");
        userTotalSpentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        bottomBar.add(buttonGroup, BorderLayout.WEST);
        bottomBar.add(userTotalSpentLabel, BorderLayout.EAST);

        centerPanel.add(bottomBar, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildUserBudgetPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Form to set budget
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Set Monthly Budget"));

        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        txtBudgetMonth = new JTextField(currentMonth, 8);
        txtBudgetAmount = new JTextField(10);
        JButton btnSetBudget = new JButton("Save Budget");
        btnSetBudget.setBackground(new Color(41, 128, 185));
        btnSetBudget.setForeground(Color.WHITE);

        form.add(new JLabel("Month (YYYY-MM):"));
        form.add(txtBudgetMonth);
        form.add(new JLabel("Budget Limit (Rs.):"));
        form.add(txtBudgetAmount);
        form.add(btnSetBudget);

        panel.add(form, BorderLayout.NORTH);

        // Status Card
        JPanel statusCard = new JPanel(new BorderLayout());
        statusCard.setBorder(BorderFactory.createTitledBorder("Current Budget & Remaining Status"));
        statusCard.setBackground(Color.WHITE);

        userBudgetStatusLabel = new JLabel("Select or set a budget to view details.", SwingConstants.CENTER);
        userBudgetStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        statusCard.add(userBudgetStatusLabel, BorderLayout.CENTER);

        panel.add(statusCard, BorderLayout.CENTER);

        btnSetBudget.addActionListener(e -> {
            String my = txtBudgetMonth.getText().trim();
            String amtStr = txtBudgetAmount.getText().trim();
            try {
                double amt = Double.parseDouble(amtStr);
                budgetService.setBudget(currentUser.getUsername(), my, amt);
                JOptionPane.showMessageDialog(this, "Budget saved successfully for " + my, "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshBudgetStatusView();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel buildUserReportsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left Table: Category-Wise Breakdown
        JPanel leftBox = new JPanel(new BorderLayout());
        leftBox.setBorder(BorderFactory.createTitledBorder("Category-Wise Spending Breakdown"));
        String[] catCols = {"Category", "Transactions", "Total (Rs.)", "Share (%)"};
        userCategorySummaryModel = new DefaultTableModel(catCols, 0);
        JTable catTable = new JTable(userCategorySummaryModel);
        leftBox.add(new JScrollPane(catTable), BorderLayout.CENTER);

        // Right Table: Monthly Summary
        JPanel rightBox = new JPanel(new BorderLayout());
        rightBox.setBorder(BorderFactory.createTitledBorder("Monthly Spending Breakdown"));
        String[] monthCols = {"Month (YYYY-MM)", "Transactions", "Total (Rs.)"};
        userMonthlySummaryModel = new DefaultTableModel(monthCols, 0);
        JTable monthTable = new JTable(userMonthlySummaryModel);
        rightBox.add(new JScrollPane(monthTable), BorderLayout.CENTER);

        panel.add(leftBox);
        panel.add(rightBox);
        return panel;
    }

    // Helper: Add Expense Action
    private void onAddExpenseClicked() {
        String title = txtTitle.getText().trim();
        String amtStr = txtAmount.getText().trim();
        String cat = (String) cmbCategory.getSelectedItem();
        String date = txtDate.getText().trim();
        String pay = (String) cmbPaymentMethod.getSelectedItem();
        String desc = txtDescription.getText().trim();

        try {
            double amt = Validator.validateAmount(amtStr);
            expenseService.addExpense(currentUser.getUsername(), title, amt, cat, date, pay, desc);
            JOptionPane.showMessageDialog(this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear inputs
            txtTitle.setText("");
            txtAmount.setText("");
            txtDescription.setText("");

            refreshUserExpenseTable();
            refreshUserSummaries();
            refreshBudgetStatusView();
        } catch (InvalidExpenseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Expense", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper: Edit Expense Action
    private void onEditSelectedExpense() {
        int row = userExpenseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an expense row to update.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) userExpenseTableModel.getValueAt(row, 0);
        Expense target = expenseService.findUserExpenseById(id, currentUser.getUsername());
        if (target == null) {
            JOptionPane.showMessageDialog(this, "Expense record not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField editTitle = new JTextField(target.getTitle());
        JTextField editAmount = new JTextField(String.valueOf(target.getAmount()));
        JComboBox<String> editCat = new JComboBox<>(Category.PREDEFINED_CATEGORIES.toArray(new String[0]));
        editCat.setSelectedItem(target.getCategory());
        JTextField editDate = new JTextField(target.getDate());
        JComboBox<String> editPay = new JComboBox<>(Expense.PAYMENT_METHODS.toArray(new String[0]));
        editPay.setSelectedItem(target.getPaymentMethod());
        JTextField editDesc = new JTextField(target.getDescription());

        Object[] message = {
                "Title:", editTitle,
                "Amount (Rs.):", editAmount,
                "Category:", editCat,
                "Date (YYYY-MM-DD):", editDate,
                "Payment Method:", editPay,
                "Description:", editDesc
        };

        int opt = JOptionPane.showConfirmDialog(this, message, "Update Expense #" + id, JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                double newAmt = Validator.validateAmount(editAmount.getText());
                expenseService.updateExpense(id, currentUser.getUsername(),
                        editTitle.getText().trim(), newAmt,
                        (String) editCat.getSelectedItem(),
                        editDate.getText().trim(),
                        (String) editPay.getSelectedItem(),
                        editDesc.getText().trim());

                JOptionPane.showMessageDialog(this, "Expense updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshUserExpenseTable();
                refreshUserSummaries();
                refreshBudgetStatusView();
            } catch (InvalidExpenseException | ExpenseNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper: Delete Expense Action
    private void onDeleteSelectedExpense() {
        int row = userExpenseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an expense row to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) userExpenseTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete Expense #" + id + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                expenseService.deleteExpense(id, currentUser.getUsername());
                JOptionPane.showMessageDialog(this, "Expense deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                refreshUserExpenseTable();
                refreshUserSummaries();
                refreshBudgetStatusView();
            } catch (ExpenseNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshUserExpenseTable() {
        if (currentUser == null) return;
        userExpenseTableModel.setRowCount(0);

        List<Expense> list = expenseService.getUserExpenses(currentUser.getUsername());
        String selectedCat = (String) userCategoryFilterCombo.getSelectedItem();
        if (selectedCat != null && !"All Categories".equalsIgnoreCase(selectedCat)) {
            list = expenseService.filterByCategory(list, selectedCat);
        }

        double total = 0.0;
        for (Expense e : list) {
            userExpenseTableModel.addRow(new Object[]{
                    e.getId(), e.getTitle(), e.getAmount(), e.getCategory(),
                    e.getDate(), e.getPaymentMethod(), e.getDescription()
            });
            total += e.getAmount();
        }

        userTotalSpentLabel.setText(String.format("Total Shown: Rs. %.2f (%d entries)  ", total, list.size()));
    }

    private void sortUserExpensesByDate() {
        if (currentUser == null) return;
        List<Expense> list = expenseService.getUserExpenses(currentUser.getUsername());
        list = expenseService.sortExpensesByDate(list, false); // newest first
        populateUserExpenseTable(list);
    }

    private void sortUserExpensesByAmount() {
        if (currentUser == null) return;
        List<Expense> list = expenseService.getUserExpenses(currentUser.getUsername());
        list = expenseService.sortExpensesByAmount(list, true); // highest first
        populateUserExpenseTable(list);
    }

    private void populateUserExpenseTable(List<Expense> list) {
        userExpenseTableModel.setRowCount(0);
        double total = 0.0;
        for (Expense e : list) {
            userExpenseTableModel.addRow(new Object[]{
                    e.getId(), e.getTitle(), e.getAmount(), e.getCategory(),
                    e.getDate(), e.getPaymentMethod(), e.getDescription()
            });
            total += e.getAmount();
        }
        userTotalSpentLabel.setText(String.format("Total Shown: Rs. %.2f (%d entries)  ", total, list.size()));
    }

    private void refreshUserSummaries() {
        if (currentUser == null) return;
        List<Expense> userExpenses = expenseService.getUserExpenses(currentUser.getUsername());

        // Category Summary
        userCategorySummaryModel.setRowCount(0);
        Map<String, Double> catTotals = new HashMap<>();
        Map<String, Integer> catCounts = new HashMap<>();
        double grand = 0.0;
        for (Expense e : userExpenses) {
            catTotals.put(e.getCategory(), catTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            catCounts.put(e.getCategory(), catCounts.getOrDefault(e.getCategory(), 0) + 1);
            grand += e.getAmount();
        }
        for (String cat : Category.PREDEFINED_CATEGORIES) {
            double amt = catTotals.getOrDefault(cat, 0.0);
            int count = catCounts.getOrDefault(cat, 0);
            double pct = grand > 0 ? (amt / grand) * 100.0 : 0.0;
            userCategorySummaryModel.addRow(new Object[]{cat, count, String.format("%.2f", amt), String.format("%.1f%%", pct)});
        }

        // Monthly Summary
        userMonthlySummaryModel.setRowCount(0);
        Map<String, Double> monthTotals = new TreeMap<>();
        Map<String, Integer> monthCounts = new TreeMap<>();
        for (Expense e : userExpenses) {
            String my = e.getMonthYear();
            monthTotals.put(my, monthTotals.getOrDefault(my, 0.0) + e.getAmount());
            monthCounts.put(my, monthCounts.getOrDefault(my, 0) + 1);
        }
        for (Map.Entry<String, Double> entry : monthTotals.entrySet()) {
            userMonthlySummaryModel.addRow(new Object[]{
                    entry.getKey(), monthCounts.get(entry.getKey()), String.format("%.2f", entry.getValue())
            });
        }

        refreshBudgetStatusView();
    }

    private void refreshBudgetStatusView() {
        if (currentUser == null || userBudgetStatusLabel == null) return;
        String currentMonth = txtBudgetMonth != null ? txtBudgetMonth.getText().trim() : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        Budget b = budgetService.getBudget(currentUser.getUsername(), currentMonth);
        List<Expense> userExpenses = expenseService.getUserExpenses(currentUser.getUsername());
        double monthSpent = 0.0;
        for (Expense e : userExpenses) {
            if (currentMonth.equals(e.getMonthYear())) {
                monthSpent += e.getAmount();
            }
        }

        if (b == null) {
            userBudgetStatusLabel.setText(String.format("<html><center>No budget set for month <b>%s</b>.<br>Total spent this month: <b>Rs. %.2f</b></center></html>",
                    currentMonth, monthSpent));
        } else {
            double remaining = b.getAmount() - monthSpent;
            double pct = (monthSpent / b.getAmount()) * 100.0;
            String statusColor = remaining < 0 ? "red" : (pct >= 80 ? "orange" : "green");
            String statusText = remaining < 0 ? "[!] BUDGET EXCEEDED" : (pct >= 80 ? "[!] WARNING: >= 80% USED" : "[OK] WITHIN BUDGET");

            userBudgetStatusLabel.setText(String.format("<html><div style='text-align:center; padding:15px;'>"
                            + "<h3>Budget Overview for %s</h3>"
                            + "<p>Budget Allocated: <b>Rs. %.2f</b> | Total Spent: <b>Rs. %.2f</b></p>"
                            + "<p>Remaining Budget: <b>Rs. %.2f</b> (%.1f%% utilized)</p>"
                            + "<h3 style='color:%s;'>%s</h3>"
                            + "</div></html>",
                    currentMonth, b.getAmount(), monthSpent, remaining, pct, statusColor, statusText));
        }
    }

    // =========================================================================
    // 3. ADMIN DASHBOARD (READ-ONLY MONITORING)
    // =========================================================================
    private void buildAdminDashboard() {
        adminDashboardCard = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel lblTitle = new JLabel("Administrator Monitoring Dashboard (Read-Only)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(rootPanel, "LOGIN");
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        adminDashboardCard.add(headerPanel, BorderLayout.NORTH);

        // Sub-banner: Explicit enforcement notice
        JPanel noticePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        noticePanel.setBackground(new Color(254, 249, 231));
        noticePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 196, 15)));
        JLabel lblNotice = new JLabel("Admin can only monitor and view records. User personal expenses cannot be modified or deleted here.");
        lblNotice.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        noticePanel.add(lblNotice);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(noticePanel, BorderLayout.SOUTH);
        adminDashboardCard.add(topContainer, BorderLayout.NORTH);

        // Stats Banner
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(new Color(240, 243, 246));

        adminTotalUsersLabel = createStatCard("Total Registered Users", String.valueOf(authService.getTotalUsersCount()), new Color(52, 152, 219));
        adminTotalSystemSpentLabel = createStatCard("System Total Spent", String.format("Rs. %.2f", expenseService.getTotalExpensesAllUsers()), new Color(46, 204, 113));

        Expense high = expenseService.getHighestExpense(expenseService.getAllExpenses());
        Expense low = expenseService.getLowestExpense(expenseService.getAllExpenses());
        adminHighestExpenseLabel = createStatCard("Highest Single Expense", high != null ? String.format("Rs. %.2f (%s)", high.getAmount(), high.getUsername()) : "N/A", new Color(155, 89, 182));
        adminLowestExpenseLabel = createStatCard("Lowest Single Expense", low != null ? String.format("Rs. %.2f (%s)", low.getAmount(), low.getUsername()) : "N/A", new Color(230, 126, 34));

        statsPanel.add(adminTotalUsersLabel.getParent());
        statsPanel.add(adminTotalSystemSpentLabel.getParent());
        statsPanel.add(adminHighestExpenseLabel.getParent());
        statsPanel.add(adminLowestExpenseLabel.getParent());

        // Admin Tabbed Pane
        JTabbedPane adminTabs = new JTabbedPane();
        adminTabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Tab 1: All User Expenses (Search & Filter)
        adminTabs.addTab("All Expenses Monitoring", buildAdminExpensesPanel());

        // Tab 2: Registered Users
        adminTabs.addTab("Registered Users List", buildAdminUsersPanel());

        // Tab 3: Monthly & Category Reports
        adminTabs.addTab("Monthly & Category Analytics", buildAdminReportsPanel());

        // Tab 4: User Monthly Budgets
        adminTabs.addTab("User Monthly Budgets", buildAdminBudgetsPanel());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(adminTabs, BorderLayout.CENTER);

        adminDashboardCard.add(centerPanel, BorderLayout.CENTER);

        refreshAdminAllTables();
    }

    private JLabel createStatCard(String title, String val, Color barColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblT.setForeground(Color.GRAY);

        JLabel lblV = new JLabel(val);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblV.setForeground(barColor);

        card.add(lblT, BorderLayout.NORTH);
        card.add(lblV, BorderLayout.CENTER);

        return lblV;
    }

    private JPanel buildAdminExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Top Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterBar.add(new JLabel("Search by Username:"));
        adminSearchUserField = new JTextField(10);
        filterBar.add(adminSearchUserField);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> filterAdminExpenses());
        filterBar.add(btnSearch);

        filterBar.add(new JLabel("Filter by Category:"));
        String[] filterOptions = new String[Category.PREDEFINED_CATEGORIES.size() + 1];
        filterOptions[0] = "All Categories";
        for (int i = 0; i < Category.PREDEFINED_CATEGORIES.size(); i++) {
            filterOptions[i + 1] = Category.PREDEFINED_CATEGORIES.get(i);
        }
        adminCategoryFilterCombo = new JComboBox<>(filterOptions);
        adminCategoryFilterCombo.addActionListener(e -> filterAdminExpenses());
        filterBar.add(adminCategoryFilterCombo);

        JButton btnReset = new JButton("Reset All");
        btnReset.addActionListener(e -> {
            adminSearchUserField.setText("");
            adminCategoryFilterCombo.setSelectedIndex(0);
            refreshAdminExpenseTable(expenseService.getAllExpenses());
        });
        filterBar.add(btnReset);

        panel.add(filterBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Username", "Title", "Amount (Rs.)", "Category", "Date", "Payment Method", "Description"};
        adminExpenseTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        adminExpenseTable = new JTable(adminExpenseTableModel);
        adminExpenseTable.setRowHeight(22);
        panel.add(new JScrollPane(adminExpenseTable), BorderLayout.CENTER);

        return panel;
    }

    private void filterAdminExpenses() {
        String userQuery = adminSearchUserField.getText().trim().toLowerCase();
        String catQuery = (String) adminCategoryFilterCombo.getSelectedItem();

        List<Expense> list = expenseService.getAllExpenses();
        if (!userQuery.isEmpty()) {
            list = expenseService.searchByUsername(userQuery);
        }
        if (catQuery != null && !"All Categories".equalsIgnoreCase(catQuery)) {
            list = expenseService.filterByCategory(list, catQuery);
        }
        refreshAdminExpenseTable(list);
    }

    private void refreshAdminExpenseTable(List<Expense> list) {
        adminExpenseTableModel.setRowCount(0);
        for (Expense e : list) {
            adminExpenseTableModel.addRow(new Object[]{
                    e.getId(), e.getUsername(), e.getTitle(), e.getAmount(),
                    e.getCategory(), e.getDate(), e.getPaymentMethod(), e.getDescription()
            });
        }
    }

    private JPanel buildAdminUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        String[] cols = {"Username", "Full Name", "Role", "Account Created"};
        adminUsersTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(adminUsersTableModel);
        table.setRowHeight(22);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAdminBudgetsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        String[] cols = {"Username", "Month (YYYY-MM)", "Monthly Budget Limit (Rs.)"};
        adminBudgetsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(adminBudgetsTableModel);
        table.setRowHeight(22);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAdminReportsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // System Category Breakdown
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createTitledBorder("System Category Breakdown"));
        String[] cols = {"Category", "Transactions", "Total Spending (Rs.)", "System Share"};
        DefaultTableModel catModel = new DefaultTableModel(cols, 0);
        JTable catTable = new JTable(catModel);
        left.add(new JScrollPane(catTable), BorderLayout.CENTER);

        // System Monthly Breakdown
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("System Monthly Trends"));
        String[] mCols = {"Month", "Transactions", "Total Spent (Rs.)"};
        DefaultTableModel mModel = new DefaultTableModel(mCols, 0);
        JTable mTable = new JTable(mModel);
        right.add(new JScrollPane(mTable), BorderLayout.CENTER);

        // Populate System Summaries
        List<Expense> all = expenseService.getAllExpenses();
        double grand = expenseService.getTotalExpensesAllUsers();

        Map<String, Double> catTotals = new HashMap<>();
        Map<String, Integer> catCounts = new HashMap<>();
        for (Expense e : all) {
            catTotals.put(e.getCategory(), catTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            catCounts.put(e.getCategory(), catCounts.getOrDefault(e.getCategory(), 0) + 1);
        }
        for (String cat : Category.PREDEFINED_CATEGORIES) {
            double amt = catTotals.getOrDefault(cat, 0.0);
            int count = catCounts.getOrDefault(cat, 0);
            double pct = grand > 0 ? (amt / grand) * 100.0 : 0.0;
            catModel.addRow(new Object[]{cat, count, String.format("%.2f", amt), String.format("%.1f%%", pct)});
        }

        Map<String, Double> monthTotals = new TreeMap<>();
        Map<String, Integer> monthCounts = new TreeMap<>();
        for (Expense e : all) {
            String my = e.getMonthYear();
            monthTotals.put(my, monthTotals.getOrDefault(my, 0.0) + e.getAmount());
            monthCounts.put(my, monthCounts.getOrDefault(my, 0) + 1);
        }
        for (Map.Entry<String, Double> entry : monthTotals.entrySet()) {
            mModel.addRow(new Object[]{entry.getKey(), monthCounts.get(entry.getKey()), String.format("%.2f", entry.getValue())});
        }

        panel.add(left);
        panel.add(right);
        return panel;
    }

    private void refreshAdminAllTables() {
        // Refresh expenses
        refreshAdminExpenseTable(expenseService.getAllExpenses());

        // Refresh users
        adminUsersTableModel.setRowCount(0);
        for (User u : authService.getAllUsers()) {
            adminUsersTableModel.addRow(new Object[]{
                    u.getUsername(), u.getFullName(), u.getRole(), u.getCreatedAt()
            });
        }

        // Refresh budgets
        adminBudgetsTableModel.setRowCount(0);
        for (Budget b : budgetService.getAllBudgets()) {
            adminBudgetsTableModel.addRow(new Object[]{
                    b.getUsername(), b.getMonthYear(), String.format("%.2f", b.getAmount())
            });
        }

        // Refresh stat cards
        adminTotalUsersLabel.setText(String.valueOf(authService.getTotalUsersCount()));
        adminTotalSystemSpentLabel.setText(String.format("Rs. %.2f", expenseService.getTotalExpensesAllUsers()));
        Expense high = expenseService.getHighestExpense(expenseService.getAllExpenses());
        Expense low = expenseService.getLowestExpense(expenseService.getAllExpenses());
        adminHighestExpenseLabel.setText(high != null ? String.format("Rs. %.2f (%s)", high.getAmount(), high.getUsername()) : "N/A");
        adminLowestExpenseLabel.setText(low != null ? String.format("Rs. %.2f (%s)", low.getAmount(), low.getUsername()) : "N/A");
    }

    /**
     * Entry point for running GUI directly.
     */
    public static void launch(AuthService auth, ExpenseService exp, BudgetService bud, ReportService rep) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for native OS appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            PersonalExpenseTrackerGUI gui = new PersonalExpenseTrackerGUI(auth, exp, bud, rep);
            gui.setVisible(true);
        });
    }
}
