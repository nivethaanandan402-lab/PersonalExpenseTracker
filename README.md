# Personal Expense Tracker

A simple, robust, console and desktop GUI-based application built in **100% Core Java (JDK 17+)** as a college mini-project for **Software Engineering**.

The project uses only standard Java libraries (`java.util`, `java.io`, `java.time`, `javax.swing`, `java.awt`) with **zero external dependencies, build tools, or frameworks**. It features persistent plain text / CSV file storage in a `data/` folder, clean separation of concerns, and clear demonstration of core OOP concepts suitable for viva defense.

---

## 📌 Project Overview

**Personal Expense Tracker** provides individuals with an intuitive tool to record expenses, classify them by category, monitor monthly budget limits, and inspect detailed financial breakdowns. It supports two distinct roles:
1. **Regular User:** Can sign up, log in, record personal expenses, update/delete personal records, set monthly budgets, track remaining budgets with threshold alerts, and generate category/monthly spending reports.
2. **Administrator:** Can log in using default administrative credentials (`admin` / `admin123`) to audit registered users, search expenses across all users, inspect system-wide monthly and category spending, and view highest/lowest expense extremes. The admin role follows the **Principle of Least Privilege (Read-Only)** and cannot modify or delete users' personal expenses.

---

## 🚀 Key Features

### 1. User Features
- **Sign Up & Secure Login:** User validation with unique username and minimum 6-character passwords.
- **Add Expense:** Captures name, amount ($> 0$), category, date (`YYYY-MM-DD`), payment method, and optional description ($\le 200$ chars).
- **View All Expenses:** Formatted tabular display of personal entries.
- **Update & Delete:** Safe modifications with ownership verification (users can only touch their own data).
- **Category Filtering:** Predefined categories (`Food`, `Transportation`, `Education`, `Shopping`, `Entertainment`, `Healthcare`, `Bills & Utilities`, `Others`).
- **Payment Methods:** Predefined modes (`Cash`, `UPI`, `Debit Card`, `Credit Card`, `Net Banking`).
- **Monthly Budgeting & Alerts:** Set monthly limits with visual alerts:
  - 🟢 **Within Budget:** $< 80\%$ utilized.
  - 🟡 **Warning:** $\ge 80\%$ utilized.
  - 🔴 **Exceeded:** Over budget alert.
- **Sorting & History:** Chronological history sorting (newest/oldest) and monetary sorting using `java.util.Comparator`.
- **Summaries & Analytics:** Category breakdown (with percentage shares) and monthly trends.
- **Highest & Lowest Records:** Instant discovery of maximum and minimum personal expense entries.

### 2. Admin Features (Read-Only Monitoring)
- **Default Admin Account:** Auto-initialized on first run (`admin` / `admin123`).
- **User Auditing:** View complete list of registered users and total account count.
- **Global Expense Directory:** View all transactions across all users.
- **Search & Filter:** Search expenses by username substrings and filter by category.
- **System-Wide Financials:** Total cumulative spending and user monthly budget listings.
- **System Extremes:** Global highest and lowest single expense records.
- **System Reports:** Consolidated monthly and category breakdown across all users.

### 3. Dual Interface Support
- **Console Application (`Main.java`):** Menu-driven CLI using `java.util.Scanner`, input validation, and loop recovery.
- **Desktop Window Application (`GuiMain.java` / `PersonalExpenseTrackerGUI.java`):** Interactive graphical desktop window built with standard Java Swing (`javax.swing.JFrame`), `JTable`, `DefaultTableModel`, `CardLayout`, and forms.

### 4. Background Persistence
- **Periodic Auto-Save Daemon:** A simple Java multithreading demo (`AutoSaveDaemon extends Thread`) that executes asynchronously every 60 seconds to guarantee data persistence without freezing user interactions.

---

## 🛠️ Technologies Used

- **Language:** Java Standard Edition (JDK 17+)
- **Console I/O:** `java.util.Scanner`
- **Graphical Interface (GUI):** `javax.swing.*`, `java.awt.*` (100% Core Java, no external UI toolkit)
- **Data Persistence:** Plain CSV / Text File I/O (`java.io.BufferedReader`, `java.io.BufferedWriter`, `java.io.FileReader`, `java.io.FileWriter`)
- **Data Structures & Collections:** `ArrayList`, `HashMap`, `TreeMap`, `Collections.max/min`, `Comparator`
- **Date & Time:** `java.time.LocalDate`, `java.time.format.DateTimeFormatter`
- **External Dependencies:** **None** (No Maven, Gradle, Spring, Hibernate, or third-party JARs)

---

## 🔑 Default Admin Credentials

On the first application run, the system automatically verifies and creates the default administrator account:
- **Username:** `admin`
- **Password:** `admin123`
- **Role:** `ADMIN`

---

## 📂 Project Folder Structure

```text
PersonalExpenseTracker/
│
├── src/
│   ├── exception/
│   │   ├── DuplicateUserException.java      # Checked exception: duplicate registration
│   │   ├── ExpenseNotFoundException.java    # Checked exception: missing/unauthorized expense
│   │   ├── InvalidExpenseException.java     # Checked exception: constraint validation error
│   │   └── InvalidLoginException.java       # Checked exception: invalid credentials
│   │
│   ├── model/
│   │   ├── User.java                        # Abstract base class: User (Encapsulation)
│   │   ├── RegularUser.java                 # Subclass of User: Regular user role
│   │   ├── Admin.java                       # Subclass of User: Administrator role
│   │   ├── Category.java                    # Abstract Category & concrete subclasses
│   │   ├── Expense.java                     # Entity model: Encapsulated expense record
│   │   └── Budget.java                      # Entity model: Monthly budget configuration
│   │
│   ├── storage/
│   │   ├── Storage.java                     # Generic persistence interface: Storage<T>
│   │   └── FileStorage.java                 # CSV file implementations (User, Expense, Budget)
│   │
│   ├── util/
│   │   ├── Validator.java                   # Static validation routines & overloaded methods
│   │   └── InputHelper.java                 # Robust Scanner console input helper
│   │
│   ├── service/
│   │   ├── Reportable.java                  # Interface: Polymorphic reporting contract
│   │   ├── MonthlyReport.java               # Concrete report: Monthly spending trends
│   │   ├── CategoryWiseReport.java          # Concrete report: Category percentage breakdown
│   │   ├── UserSummaryReport.java           # Concrete report: Comprehensive financial overview
│   │   ├── AuthService.java                 # User registration, login, and user directory
│   │   ├── ExpenseService.java              # CRUD operations, filtering, sorting, extremes
│   │   ├── BudgetService.java               # Monthly budget limits, status, threshold alerts
│   │   ├── ReportService.java               # Polymorphic report execution coordinator
│   │   └── AutoSaveDaemon.java              # Multithreading demo: Background auto-save thread
│   │
│   ├── ui/
│   │   └── PersonalExpenseTrackerGUI.java   # Swing desktop GUI (extends javax.swing.JFrame)
│   │
│   ├── test/
│   │   └── SystemIntegrationTest.java       # Automated verification test suite
│   │
│   └── main/
│       ├── Main.java                        # Console menu-driven application (Scanner)
│       └── GuiMain.java                     # Direct launcher for Desktop Swing GUI
│
├── data/
│   ├── users.csv                            # Persistent user account records
│   ├── expenses.csv                         # Persistent expense transactions
│   └── budgets.csv                          # Persistent monthly budget limits
│
├── docs/
│   ├── requirements.md                      # Functional & non-functional requirements & use cases
│   ├── class_diagram.md                     # Mermaid UML and text-based architectural diagrams
│   ├── sample_output.md                     # Actual console run transcripts
│   └── oop_concepts.md                      # OOP concept mapping table & viva preparation Q&A
│
└── README.md                                # Project documentation & instructions
```

---

## 💻 How to Compile and Run

Open any terminal (PowerShell, Command Prompt, or Bash) and navigate to the project root directory:

```bash
cd PersonalExpenseTracker
```

### 1. Compile the Source Code
Create the output directory `bin` and compile all source files using standard `javac`:

**On Windows (PowerShell):**
```powershell
if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin }
javac -d bin -sourcepath src (Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
```

**On Windows (Command Prompt):**
```cmd
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin -sourcepath src @sources.txt
del sources.txt
```

**On Linux / macOS:**
```bash
mkdir -p bin
javac -d bin -sourcepath src $(find src -name "*.java")
```

---

### 2. Run the Console Menu Application (`Main.java`)
```bash
java -cp bin main.Main
```
*Note: From the console main menu or dashboard, you can also launch the Swing desktop window anytime by choosing option `3` (Main Menu) or option `13` (User Dashboard).*

---

### 3. Run the Desktop Swing JFrame GUI Directly (`GuiMain.java`)
If you want to start the desktop graphical user interface directly:
```bash
java -cp bin main.GuiMain
```

---

### 4. Run the Automated Test Suite (`SystemIntegrationTest.java`)
To verify all 10 core integration tests and assertions:
```bash
java -cp bin test.SystemIntegrationTest
```

---

## 🎓 OOP Concepts Demonstrated (Viva Reference)

1. **Encapsulation:** Private attributes with public getters/setters in `User`, `Expense`, `Budget`.
2. **Abstraction & Abstract Classes:** `abstract class User` (extended by `RegularUser` and `Admin`) and `abstract class Category`.
3. **Inheritance:** `RegularUser extends User`, `Admin extends User`, and `PersonalExpenseTrackerGUI extends JFrame`.
4. **Interfaces & Polymorphism:** `Reportable` interface implemented by `MonthlyReport`, `CategoryWiseReport`, and `UserSummaryReport`; `Storage<T>` implemented by `UserFileStorage`, `ExpenseFileStorage`, and `BudgetFileStorage`.
5. **Method Overloading:** `ExpenseService.addExpense(...)` (with/without description), `Validator.validateAmount(...)`, `Validator.validateDate(...)`.
6. **Method Overriding:** `@Override` annotations in `getRole()`, `generateReport()`, `toString()`, and `saveAll()`.
7. **`this` and `super` Keywords:** Constructor chaining using `this(...)` and superclass delegation using `super(...)`.
8. **Static & `final` Members:** Constants (`PREDEFINED_CATEGORIES`, `PAYMENT_METHODS`) and immutable helper classes (`Validator`).
9. **Custom Checked Exceptions:** `InvalidLoginException`, `DuplicateUserException`, `InvalidExpenseException`, `ExpenseNotFoundException`.
10. **Exception Handling:** `try-catch-finally` blocks and `try-with-resources` ensuring file stream closure.
11. **Collections Framework:** `ArrayList`, `HashMap`, `TreeMap`, and `Comparator` sorting.
12. **File I/O:** Reading and writing CSV files using `BufferedReader` and `BufferedWriter`.
13. **Multithreading:** Periodic background daemon thread (`AutoSaveDaemon extends Thread`).
14. **Java Swing GUI:** Desktop GUI implementing window management, tables, and dialogs using `javax.swing.JFrame`.

*For detailed explanations, refer to [docs/oop_concepts.md](docs/oop_concepts.md).*
