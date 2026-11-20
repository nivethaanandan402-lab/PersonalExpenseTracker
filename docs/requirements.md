# Personal Expense Tracker - Requirements Specification

**Project:** Personal Expense Tracker  
**Course Context:** Software Engineering Mini-Project & Object-Oriented Programming (OOP) Lab Record  
**Platform:** Java Standard Edition (JDK 17+) — Console & Desktop GUI (Swing JFrame)  
**Storage:** Persistent CSV / Plain Text File I/O (`data/`)  

---

## 1. Functional Requirements (FR)

### FR-1: User Management & Authentication
- **FR-1.1:** System shall permit new users to register an account with a unique username, password (minimum 6 characters), and full name.
- **FR-1.2:** System shall prevent duplicate username registrations by throwing a checked `DuplicateUserException`.
- **FR-1.3:** System shall authenticate users and administrators via username and password, throwing `InvalidLoginException` on failure.
- **FR-1.4:** System shall automatically seed a default administrator account (`admin` / `admin123`) on first run if no administrator exists.

### FR-2: Expense Management (Regular User)
- **FR-2.1:** Users shall record new expenses with:
  - Expense name/title (non-empty, max 100 characters)
  - Amount in currency units (strictly $> 0$)
  - Category chosen from predefined list
  - Date in valid format (`YYYY-MM-DD`)
  - Payment method chosen from predefined list
  - Optional description (up to 200 characters)
- **FR-2.2:** Each expense shall automatically be assigned a unique numeric sequential ID.
- **FR-2.3:** Users shall view all their personal expenses in tabular format.
- **FR-2.4:** Users shall update the fields of any existing expense they own.
- **FR-2.5:** Users shall delete any personal expense by its unique ID.
- **FR-2.6 (Ownership Restriction):** Users can only view, update, or delete their own expenses. Cross-user operations are strictly forbidden and throw `ExpenseNotFoundException`.
- **FR-2.7:** Users shall filter their personal expenses by category.
- **FR-2.8:** Users shall view their total cumulative expenditure.
- **FR-2.9:** Users shall view their personal expense history sorted chronologically (ascending or descending) or by amount.

### FR-3: Budgeting & Threshold Monitoring (Regular User)
- **FR-3.1:** Users shall configure a monthly budget limit for a specified month (`YYYY-MM`).
- **FR-3.2:** Monthly budget amount must be strictly $> 0$.
- **FR-3.3:** System shall calculate remaining monthly budget:
  $$\text{Remaining} = \text{Budget Limit} - \sum \text{Monthly Expenses}$$
- **FR-3.4:** System shall display three distinct budget alert states:
  1. **Comfortable Limits:** Spending $< 80\%$ of budget limit.
  2. **Warning Status:** Spending $\ge 80\%$ and $\le 100\%$ of budget limit.
  3. **Alert Status:** Budget exceeded ($\text{Remaining} < 0$).

### FR-4: Analytics & Polymorphic Reporting
- **FR-4.1:** System shall generate monthly spending summaries grouped by year and month.
- **FR-4.2:** System shall generate category-wise spending distribution reports showing transaction count, total amount, and percentage share.
- **FR-4.3:** System shall identify and display the user's highest single expense and lowest single expense records.

### FR-5: Administrator Auditing & Monitoring (Admin Role)
- **FR-5.1:** Administrators shall log in using predefined credentials (`admin` / `admin123`).
- **FR-5.2:** Administrators shall view a complete directory of all registered users and their account creation dates.
- **FR-5.3:** Administrators shall query total number of registered users.
- **FR-5.4:** Administrators shall inspect all expense records across all users.
- **FR-5.5:** Administrators shall search expenses by username substrings.
- **FR-5.6:** Administrators shall filter all system expenses by category.
- **FR-5.7:** Administrators shall view total cumulative spending across all users.
- **FR-5.8:** Administrators shall generate system-wide monthly and category-wise spending reports.
- **FR-5.9:** Administrators shall view the system-wide highest and lowest expense records.
- **FR-5.10:** Administrators shall view all configured monthly budgets across all users.
- **FR-5.11 (Principle of Least Privilege):** Administrators are restricted to read-only monitoring. Administrators **cannot** add, edit, or delete personal user expenses.

### FR-6: Persistence & Storage
- **FR-6.1:** User accounts shall be stored in `data/users.csv`.
- **FR-6.2:** Expense records shall be stored in `data/expenses.csv`.
- **FR-6.3:** Monthly budget limits shall be stored in `data/budgets.csv`.
- **FR-6.4:** File I/O operations shall employ try-with-resources and handle CSV comma escaping safely.
- **FR-6.5:** A background daemon thread (`AutoSaveDaemon`) shall periodically trigger automatic persistence flushes every 60 seconds.

---

## 2. Non-Functional Requirements (NFR)

1. **Portability & Standards:** 100% Core Java SE (JDK 17+). Runs on Windows, macOS, and Linux without modification.
2. **Zero External Dependencies:** No external JARs, Maven, Gradle, or network dependencies.
3. **Data Integrity:** Graceful handling of corrupted CSV lines without application crashes.
4. **Usability:** Dual interface design:
   - Console menu-driven CLI using `java.util.Scanner` for academic evaluation and viva testing.
   - Interactive desktop Swing window using `javax.swing.JFrame` for enhanced user interaction.
5. **Robustness & Error Recovery:** Input helper routines intercept invalid numeric entries, negative values, and menu choices with looping retries.

---

## 3. Predefined Domain Constraints

### Predefined Expense Categories
1. `Food`
2. `Transportation`
3. `Education`
4. `Shopping`
5. `Entertainment`
6. `Healthcare`
7. `Bills & Utilities`
8. `Others`

### Predefined Payment Methods
1. `Cash`
2. `UPI`
3. `Debit Card`
4. `Credit Card`
5. `Net Banking`

---

## 4. Use Case Specifications

### Use Case UC-01: User Sign Up
- **Actor:** Unauthenticated Visitor
- **Pre-condition:** System is running.
- **Flow of Events:**
  1. Visitor chooses Sign Up option.
  2. Visitor inputs full name, desired username, and password ($\ge 6$ chars).
  3. System validates format and verifies username uniqueness.
  4. System registers new `RegularUser` entity and appends to `data/users.csv`.
  5. System informs user of success.
- **Alternative Flow:** If username already exists, system displays `DuplicateUserException` error message and prompts again.

### Use Case UC-02: Add Expense
- **Actor:** Logged-in Regular User
- **Pre-condition:** User is authenticated.
- **Flow of Events:**
  1. User selects "Add New Expense".
  2. User provides title, positive amount, predefined category, valid `YYYY-MM-DD` date, predefined payment method, and optional description.
  3. System validates constraints and instantiates `Expense`.
  4. Expense is assigned auto-incremented ID and saved to `data/expenses.csv`.
- **Alternative Flow:** If amount is $\le 0$ or date format is invalid, system displays `InvalidExpenseException` error.

### Use Case UC-03: Set & Monitor Monthly Budget
- **Actor:** Logged-in Regular User
- **Pre-condition:** User is authenticated.
- **Flow of Events:**
  1. User inputs month in `YYYY-MM` format and budget amount $> 0$.
  2. System records budget limit in `data/budgets.csv`.
  3. User requests budget status. System computes total spending for that month and displays remaining amount and health warning if $\ge 80\%$.

### Use Case UC-04: Admin Audit & System Reports
- **Actor:** Administrator (`admin`)
- **Pre-condition:** Admin logged in with `admin123`.
- **Flow of Events:**
  1. Admin inspects list of all registered users and total count.
  2. Admin reviews system-wide expenses, filters by category or searches by username.
  3. Admin triggers polymorphic reports (`MonthlyReport` and `CategoryWiseReport`).
  4. Admin views system extremes (highest single expense and lowest single expense).
