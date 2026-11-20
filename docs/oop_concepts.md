# OOP Concepts Mapping & Viva Preparation Guide

**Project:** Personal Expense Tracker  
**Context:** College Software Engineering Mini-Project & OOP Lab Record  

---

## 1. Complete OOP Concepts Mapping Table

| No. | OOP / Java Concept | File Path | Class / Interface | Implementation Details & Viva Explanation |
|:---:|:---|:---|:---|:---|
| **1** | **Classes & Objects** | `src/model/Expense.java`<br>`src/model/Budget.java` | `Expense`<br>`Budget` | Objects represent tangible real-world domain entities. State is held in instance attributes and behaviors in methods. |
| **2** | **Encapsulation** | `src/model/User.java`<br>`src/model/Expense.java`<br>`src/model/Budget.java` | `User`<br>`Expense`<br>`Budget` | All attributes are strictly declared `private`. Internal data cannot be corrupted directly from the outside. Read and write access is mediated via public getter and validated setter methods. |
| **3** | **Abstraction & Abstract Classes** | `src/model/User.java`<br>`src/model/Category.java` | `abstract class User`<br>`abstract class Category` | Defines base templates that cannot be instantiated directly (`new User()` is forbidden). Declares abstract methods `getRole()`, `displayUserTypeInfo()`, and `getCategoryType()` enforced on subclasses. |
| **4** | **Inheritance** | `src/model/RegularUser.java`<br>`src/model/Admin.java`<br>`src/ui/PersonalExpenseTrackerGUI.java` | `RegularUser extends User`<br>`Admin extends User`<br>`PersonalExpenseTrackerGUI extends JFrame` | Subclasses inherit attributes (`username`, `password`, `fullName`, `createdAt`) and methods from base classes, promoting code reusability. Swing GUI inherits window lifecycle from `javax.swing.JFrame`. |
| **5** | **Interfaces & Contract Programming** | `src/service/Reportable.java`<br>`src/storage/Storage.java` | `interface Reportable`<br>`interface Storage<T>` | Interfaces define pure behavioral contracts without specifying storage or presentation media. `Storage<T>` leverages Java Generics for type-safe persistence. |
| **6** | **Run-Time Polymorphism (Dynamic Binding)** | `src/service/ReportService.java`<br>`src/service/MonthlyReport.java`<br>`src/service/CategoryWiseReport.java`<br>`src/service/UserSummaryReport.java` | `Reportable report`<br>`MonthlyReport`<br>`CategoryWiseReport`<br>`UserSummaryReport` | The caller invokes `report.generateReport()`. The JVM resolves at runtime which concrete implementation to execute depending on whether `report` holds a `MonthlyReport`, `CategoryWiseReport`, or `UserSummaryReport`. |
| **7** | **Compile-Time Polymorphism (Method Overloading)** | `src/service/ExpenseService.java`<br>`src/util/Validator.java` | `ExpenseService`<br>`Validator` | Multiple methods share the same name with different parameter signatures:<br>• `addExpense(..., paymentMethod)` vs `addExpense(..., paymentMethod, description)`<br>• `validateAmount(double)` vs `validateAmount(String)`<br>• `validateDate(String)` vs `validateDate(String, boolean)`. |
| **8** | **Method Overriding (`@Override`)** | `src/model/RegularUser.java`<br>`src/model/Admin.java`<br>`src/model/Expense.java`<br>`src/storage/FileStorage.java` | `RegularUser`<br>`Admin`<br>`Expense`<br>`UserFileStorage` | Subclasses provide specific implementations of methods defined in parents or interfaces (`@Override public String getRole()`, `@Override public String toString()`, `@Override public void saveAll(...)`). |
| **9** | **Constructors, Overloaded Constructors & Chaining** | `src/model/User.java`<br>`src/model/Expense.java` | `User`<br>`Expense` | Constructors initialize newly allocated objects. Overloaded constructors use `this(...)` to chain parameters and avoid code duplication. |
| **10** | **`this` and `super` Keywords** | `src/model/RegularUser.java`<br>`src/model/Admin.java`<br>`src/model/Expense.java` | `RegularUser`<br>`Admin`<br>`Expense` | • `this` refers to the current object instance and resolves naming shadowing between parameters and fields (`this.title = title`).<br>• `super(...)` delegates constructor execution to the immediate parent class (`super(username, password, fullName)`). |
| **11** | **Static Members & `final` Keyword** | `src/model/Category.java`<br>`src/model/Expense.java`<br>`src/util/Validator.java` | `Category`<br>`Expense`<br>`Validator` | • `static`: Belongs to class level rather than per instance (e.g. `PREDEFINED_CATEGORIES`, utility methods in `Validator`).<br>• `final`: Constants that cannot be reassigned (`public static final List<String> PAYMENT_METHODS`) and immutable helper classes (`public final class Validator`). |
| **12** | **Custom Checked Exceptions** | `src/exception/InvalidLoginException.java`<br>`src/exception/DuplicateUserException.java`<br>`src/exception/InvalidExpenseException.java`<br>`src/exception/ExpenseNotFoundException.java` | `InvalidLoginException`<br>`DuplicateUserException`<br>`InvalidExpenseException`<br>`ExpenseNotFoundException` | All extend `java.lang.Exception`. Enforces compile-time handling: callers must declare `throws` or catch with `try-catch`. |
| **13** | **Exception Handling (`try-catch-finally` & Try-With-Resources)** | `src/main/Main.java`<br>`src/storage/FileStorage.java` | `Main`<br>`FileStorage` | • `try-with-resources`: `try (BufferedReader br = new BufferedReader(...))` guarantees stream closure even during unexpected errors.<br>• `try-catch-finally`: Intercepts and recovers from user input mistakes and file errors without crashing. |
| **14** | **Collections Framework** | `src/service/ExpenseService.java`<br>`src/service/AuthService.java`<br>`src/service/CategoryWiseReport.java` | `ArrayList`<br>`HashMap`<br>`TreeMap`<br>`Comparator` | • `ArrayList<Expense>`: Dynamic sequential list for expenses and users.<br>• `HashMap<String, User>`: $O(1)$ constant time credential verification.<br>• `TreeMap<String, Double>`: Key-sorted map for chronological monthly totals.<br>• `Comparator.comparing()`: Multi-criteria sorting (by date and by amount). |
| **15** | **File I/O & CSV Persistence** | `src/storage/FileStorage.java` | `FileStorage` | Persistent storage in `data/users.csv`, `data/expenses.csv`, and `data/budgets.csv`. Uses `BufferedReader`, `BufferedWriter`, and custom quote/comma escaping. |
| **16** | **Multithreading & Daemon Threads** | `src/service/AutoSaveDaemon.java` | `AutoSaveDaemon extends Thread` | Asynchronous background daemon thread (`setDaemon(true)`). Runs periodically every 60 seconds using `Thread.sleep()` to flush unsaved changes to disk without blocking user interaction. |
| **17** | **Desktop GUI (Java Swing)** | `src/ui/PersonalExpenseTrackerGUI.java`<br>`src/main/GuiMain.java` | `PersonalExpenseTrackerGUI`<br>`GuiMain` | Pure Java Swing desktop interface utilizing `JFrame`, `CardLayout`, `JTable`, `DefaultTableModel`, `JOptionPane`, and event listeners. |

---

## 2. Frequently Asked Viva Questions & Answers

### Q1: What is the difference between an Abstract Class and an Interface? Where did you use them?
> **Answer:**  
> - An **Abstract Class** can have instance state (private instance variables), constructors, and both abstract and concrete methods. A class can extend only one abstract class (single inheritance). In our project, `User` is an abstract class with private fields (`username`, `password`, `createdAt`) and common logic, extended by `RegularUser` and `Admin`.
> - An **Interface** defines purely a behavioral contract. A class can implement multiple interfaces. In our project, `Reportable` defines `generateReport()`, implemented polymorphically by `MonthlyReport`, `CategoryWiseReport`, and `UserSummaryReport`. Similarly, `Storage<T>` is a generic interface implemented by `UserFileStorage`, `ExpenseFileStorage`, and `BudgetFileStorage`.

### Q2: Why did you create custom checked exceptions instead of using `RuntimeException`?
> **Answer:**  
> Checked exceptions (subclasses of `java.lang.Exception`) enforce compile-time verification. When an invalid operation occurs—such as a duplicate username (`DuplicateUserException`), incorrect credentials (`InvalidLoginException`), bad expense constraints (`InvalidExpenseException`), or accessing unauthorized records (`ExpenseNotFoundException`)—the compiler forces the caller to explicitly handle the error or declare it. This guarantees that user input mistakes in the console or GUI are caught and handled gracefully without terminating the application.

### Q3: How is Polymorphism demonstrated in your reports?
> **Answer:**  
> In `ReportService`, the method `printReport(Reportable report)` accepts any object implementing the `Reportable` interface. When `report.generateReport()` is called, the JVM performs dynamic method dispatch at runtime. If passed a `MonthlyReport`, it calculates monthly totals using a `TreeMap`; if passed a `CategoryWiseReport`, it computes category percentages using a `HashMap`. The caller does not need separate printing methods for each report type.

### Q4: How is Encapsulation enforced across the entities?
> **Answer:**  
> In `Expense`, `User`, and `Budget`, all member fields are marked `private`. Outside classes cannot modify `expense.amount = -500;` directly. Modifications must pass through validated setters or constructors (e.g., `Validator.validateAmount(amount)`), ensuring that amounts are strictly $> 0$ and data integrity is permanently maintained.

### Q5: What is the purpose of the `AutoSaveDaemon` thread and why is it marked as a daemon?
> **Answer:**  
> `AutoSaveDaemon` extends `java.lang.Thread` and runs periodically every 60 seconds in the background to flush memory collections to disk. It is marked with `setDaemon(true)` so that the Java Virtual Machine does not wait for it to finish when the user exits the main console application or closes the GUI window. Daemon threads automatically terminate when all user threads stop.

### Q6: How does Method Overloading differ from Method Overriding in your project?
> **Answer:**  
> - **Method Overloading** occurs within the same class with the same method name but different parameter lists (compile-time polymorphism). For example, `ExpenseService.addExpense(...)` has two overloaded versions: one without description and one with description.
> - **Method Overriding** occurs when a subclass redefines a method inherited from a superclass with the exact same name, return type, and parameters (run-time polymorphism). For example, `RegularUser` and `Admin` override `getRole()` from `User`, returning `"USER"` and `"ADMIN"` respectively.
