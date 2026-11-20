# Real Console Run Sample Output

This document contains actual console transcript captures from a complete run of the **Personal Expense Tracker** console application (`java -cp bin main.Main`).

---

## 1. Application Launch & Main Menu

```text
==================================================
       PERSONAL EXPENSE TRACKER (OOP LAB)         
==================================================
Default Admin Account: admin / admin123
--------------------------------------------------
1. Login
2. Sign Up (Register New Account)
3. Launch Desktop GUI (JFrame Window)
4. Exit Application
--------------------------------------------------
Enter your choice (1-4): 2
```

---

## 2. User Sign Up (Registration)

```text
--- Register New User Account ---
Enter Your Full Name: Sarah Connor
Enter Desired Username (3-20 alphanumeric): sarah_c
Enter Password (min 6 characters): secret2026

[+] Registration Successful! Account created for: sarah_c
    You can now log in using your credentials.

==================================================
       PERSONAL EXPENSE TRACKER (OOP LAB)         
==================================================
Default Admin Account: admin / admin123
--------------------------------------------------
1. Login
2. Sign Up (Register New Account)
3. Launch Desktop GUI (JFrame Window)
4. Exit Application
--------------------------------------------------
Enter your choice (1-4): 1
```

---

## 3. User Login

```text
--- User / Admin Login ---
Enter Username: sarah_c
Enter Password: secret2026

[+] Login Successful! Welcome, Sarah Connor.
Role: Standard User | Access: Personal Expenses & Monthly Budgets

==================================================
  USER DASHBOARD: Sarah Connor (sarah_c)
==================================================
1.  Add New Expense
2.  View All My Expenses
3.  Update An Existing Expense
4.  Delete An Expense
5.  View Expenses Filtered by Category
6.  View Total Expenses
7.  View Monthly Expense Summary
8.  View Category-Wise Expense Summary
9.  Set Monthly Budget
10. View Remaining Budget & Budget Status
11. View Personal Expense History (Chronological)
12. View Highest & Lowest Expense Records
13. Launch Desktop GUI (JFrame Window)
14. Logout to Main Menu
--------------------------------------------------
Choose an option (1-14): 1
```

---

## 4. Adding a New Expense

```text
--- Add New Expense ---
Expense Name / Title: Laptop Battery Replacement
Amount (Rs.): 2450.00
Select Category:
  1. Food
  2. Transportation
  3. Education
  4. Shopping
  5. Entertainment
  6. Healthcare
  7. Bills & Utilities
  8. Others
Choose an option (1-8): 7
Date (YYYY-MM-DD): 2026-11-20
Select Payment Method:
  1. Cash
  2. UPI
  3. Debit Card
  4. Credit Card
  5. Net Banking
Choose an option (1-5): 2
Description (Optional, press Enter for None): Original OEM battery replacement

[+] Expense successfully added! Assigned ID: #11

==================================================
  USER DASHBOARD: Sarah Connor (sarah_c)
==================================================
Choose an option (1-14): 1
--- Add New Expense ---
Expense Name / Title: Python Data Science Book
Amount (Rs.): 899.00
Select Category:
  1. Food
  2. Transportation
  3. Education
  4. Shopping
  5. Entertainment
  6. Healthcare
  7. Bills & Utilities
  8. Others
Choose an option (1-8): 3
Date (YYYY-MM-DD): 2026-11-21
Select Payment Method:
  1. Cash
  2. UPI
  3. Debit Card
  4. Credit Card
  5. Net Banking
Choose an option (1-5): 4
Description (Optional, press Enter for None): Technical reference handbook

[+] Expense successfully added! Assigned ID: #12
```

---

## 5. Viewing All Expenses & Total Spent

```text
Choose an option (1-14): 2

--- All Expenses for User: sarah_c ---
  ID   | Title                  | Amount (Rs.) | Category         | Date       | Payment      | Description
  ---------------------------------------------------------------------------------------------------------------
  #11  | Laptop Battery Repl... | Rs.   2450.00 | Bills & Utilities | 2026-11-20 | UPI          | Original OEM battery replacement
  #12  | Python Data Scienc...  | Rs.    899.00 | Education        | 2026-11-21 | Credit Card  | Technical reference handbook
  ---------------------------------------------------------------------------------------------------------------
  Total Records: 2

Choose an option (1-14): 6

==================================================
  Total Expenses Recorded : 2 entries
  Cumulative Total Spent  : Rs. 3349.00
==================================================
```

---

## 6. Setting Monthly Budget & Checking Budget Status

```text
Choose an option (1-14): 9

--- Set Monthly Budget ---
Enter Month-Year (YYYY-MM, e.g. 2026-11): 2026-11
Enter Monthly Budget Limit (Rs.): 10000.00

[+] Budget successfully configured: Budget[User: sarah_c | Month: 2026-11 | Limit: Rs. 10000.00]

Choose an option (1-14): 10

--- View Budget & Remaining Status ---
Enter Month-Year (YYYY-MM, default 2026-11): 2026-11

Budget for sarah_c (2026-11):
  - Allocated Budget : Rs. 10000.00
  - Total Spent      : Rs. 3349.00
  - Remaining Budget : Rs. 6651.00 (33.5% used)
  - STATUS           : [OK] Spending is within comfortable limits.
```

---

## 7. Viewing Personal Expense History (Chronological)

```text
Choose an option (1-14): 11

--- Personal Expense History (Chronological) ---
1. Newest First (Descending)
2. Oldest First (Ascending)
Choose sort order (1-2): 1

  ID   | Title                  | Amount (Rs.) | Category         | Date       | Payment      | Description
  ---------------------------------------------------------------------------------------------------------------
  #12  | Python Data Scienc...  | Rs.    899.00 | Education        | 2026-11-21 | Credit Card  | Technical reference handbook
  #11  | Laptop Battery Repl... | Rs.   2450.00 | Bills & Utilities | 2026-11-20 | UPI          | Original OEM battery replacement
  ---------------------------------------------------------------------------------------------------------------
  Total Records: 2
```

---

## 8. Category-Wise Spending Report (User View)

```text
Choose an option (1-14): 8

========================================================
  Category-Wise Spending Breakdown Report
  Scope: User: sarah_c
========================================================
  Category             | Transactions | Total (Rs.)     | Share (%)
  ------------------------------------------------------------------
  Food                 | 0            | Rs.         0.00 |    0.0%
  Transportation       | 0            | Rs.         0.00 |    0.0%
  Education            | 1            | Rs.       899.00 |   26.8%
  Shopping             | 0            | Rs.         0.00 |    0.0%
  Entertainment        | 0            | Rs.         0.00 |    0.0%
  Healthcare           | 0            | Rs.         0.00 |    0.0%
  Bills & Utilities    | 1            | Rs.      2450.00 |   73.2%
  Others               | 0            | Rs.         0.00 |    0.0%
  ------------------------------------------------------------------
  TOTAL SPENT ACROSS ALL CATEGORIES: Rs. 3349.00 (2 transactions)
==================================================================

Choose an option (1-14): 14

[+] Logged out successfully. Returning to main menu.
```

---

## 9. Administrator Login & Dashboard

```text
==================================================
       PERSONAL EXPENSE TRACKER (OOP LAB)         
==================================================
Default Admin Account: admin / admin123
--------------------------------------------------
1. Login
2. Sign Up (Register New Account)
3. Launch Desktop GUI (JFrame Window)
4. Exit Application
--------------------------------------------------
Enter your choice (1-4): 1

--- User / Admin Login ---
Enter Username: admin
Enter Password: admin123

[+] Login Successful! Welcome, System Administrator.
Role: Administrator | Access: Global Reports, User Auditing & Spending Analytics (Read-Only)

==================================================
   ADMIN MONITORING DASHBOARD (READ-ONLY)         
==================================================
1.  View All Registered Users
2.  View Total Number of Registered Users
3.  View All Expenses Across All Users
4.  Search Expenses by Username
5.  Filter Expenses by Category
6.  View Total Expenses Across All Users
7.  View Monthly Expense Reports
8.  View Category-Wise Spending Reports
9.  View Highest & Lowest Expense Records (System-Wide)
10. View Users' Monthly Budget Details
11. Launch Desktop GUI (JFrame Window)
12. Logout to Main Menu
--------------------------------------------------
Choose an option (1-12): 1
```

---

## 10. Admin Views All Registered Users

```text
--- All Registered Users ---
Username           | Full Name                | Role       | Registered  
-------------------------------------------------------------------------
admin              | System Administrator     | ADMIN      | 2026-01-01  
john_doe           | John Doe                 | USER       | 2026-01-10  
alice_smith        | Alice Smith              | USER       | 2026-02-01  
sarah_c            | Sarah Connor             | USER       | 2026-11-20  
-------------------------------------------------------------------------
Total Users: 4
```

---

## 11. Admin Searches Expenses by Username

```text
Choose an option (1-12): 4

--- Search Expenses by Username ---
Enter username search term: alice

Found 3 records matching username 'alice':
  ID   | User         | Title                | Amount (Rs.) | Category         | Date       | Payment      | Description
  ------------------------------------------------------------------------------------------------------------------------
  #8   | alice_smith  | College Tuition Fee  | Rs. 18500.00 | Education        | 2026-11-01 | Net Banking  | Semester fee installment
  #9   | alice_smith  | Hostel Electricity.. | Rs.  1450.00 | Bills & Utilities | 2026-11-04 | UPI          | Monthly electricity bill
  #10  | alice_smith  | Organic Groceries    | Rs.  2200.00 | Food             | 2026-11-10 | Debit Card   | Weekly fruits and vegetables
  ------------------------------------------------------------------------------------------------------------------------
  Total Records: 3
```

---

## 12. Admin System-Wide Monthly & Category Reports

```text
Choose an option (1-12): 7

--- System-Wide Monthly Expense Reports ---
1. Consolidated View (All Months)
2. Specific Month (YYYY-MM)
Choose option (1-2): 1

========================================================
  Monthly Expense Report (All Months)
  User: All Users (Admin View)
========================================================
  Month (YYYY-MM) | Transactions | Total Spending 
  ------------------------------------------------------
  2026-11         | 12           | Rs.     36166.00
  ------------------------------------------------------
  TOTAL: 12 record(s) | Grand Total: Rs. 36166.00
========================================================

Choose an option (1-12): 8

========================================================
  Category-Wise Spending Breakdown Report
  Scope: All Users (Admin View)
========================================================
  Category             | Transactions | Total (Rs.)     | Share (%)
  ------------------------------------------------------------------
  Food                 | 2            | Rs.      5650.00 |   15.6%
  Transportation       | 1            | Rs.      1200.00 |    3.3%
  Education            | 3            | Rs.     20249.00 |   56.0%
  Shopping             | 1            | Rs.      2999.00 |    8.3%
  Entertainment        | 1            | Rs.       650.00 |    1.8%
  Healthcare           | 1            | Rs.      1500.00 |    4.1%
  Bills & Utilities    | 3            | Rs.      3918.00 |   10.8%
  Others               | 0            | Rs.         0.00 |    0.0%
  ------------------------------------------------------------------
  TOTAL SPENT ACROSS ALL CATEGORIES: Rs. 36166.00 (12 transactions)
==================================================================
```

---

## 13. Admin System Extremes (Highest & Lowest Expenses)

```text
Choose an option (1-12): 9

==================================================
  SYSTEM-WIDE HIGHEST & LOWEST EXPENSE RECORDS    
==================================================
Highest Expense: College Tuition Fee by user 'alice_smith'
  Amount: Rs. 18500.00 | Category: Education | Date: 2026-11-01 | Payment: Net Banking

Lowest Expense: Weekend Cinema by user 'john_doe'
  Amount: Rs. 650.00 | Category: Entertainment | Date: 2026-11-12 | Payment: UPI
==================================================

Choose an option (1-12): 12

[+] Admin logged out. Returning to main menu.

Enter your choice (1-4): 4

Saving all records and exiting... Thank you for using Personal Expense Tracker!
```
