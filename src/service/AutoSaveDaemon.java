package service;

/**
 * Multithreading Demo Class: AutoSaveDaemon
 * 
 * OOP & Java Concepts Demonstrated:
 * 1. Multithreading (Thread / Runnable):
 *    - Extends {@link java.lang.Thread} to perform asynchronous periodic background tasks.
 * 2. Daemon Threads:
 *    - Set as a daemon thread via 'setDaemon(true)' so it automatically halts when all
 *      user/main threads finish execution, avoiding JVM hangs.
 * 3. Exception Handling with Thread.sleep():
 *    - Handles {@link InterruptedException} cleanly when the thread is interrupted.
 */
public class AutoSaveDaemon extends Thread {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final int intervalSeconds;
    private volatile boolean running = true;

    /**
     * Parameterized Constructor.
     *
     * @param expenseService  Expense service reference
     * @param budgetService   Budget service reference
     * @param intervalSeconds Save frequency in seconds
     */
    public AutoSaveDaemon(ExpenseService expenseService, BudgetService budgetService, int intervalSeconds) {
        super("AutoSave-Daemon-Thread");
        this.expenseService = expenseService;
        this.budgetService = budgetService;
        this.intervalSeconds = intervalSeconds;
        // Mark as daemon so JVM can exit smoothly when the main console loop terminates
        setDaemon(true);
    }

    /**
     * Background thread execution logic.
     */
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(intervalSeconds * 1000L);
                if (expenseService != null) {
                    expenseService.persistExpenses();
                }
                if (budgetService != null) {
                    budgetService.persistBudgets();
                }
            } catch (InterruptedException e) {
                // Thread interrupted on shutdown
                running = false;
                break;
            } catch (Exception e) {
                System.err.println("[AutoSaveDaemon] Warning: Periodic auto-save error: " + e.getMessage());
            }
        }
    }

    /**
     * Gracefully stops the daemon thread.
     */
    public void stopDaemon() {
        running = false;
        interrupt();
    }
}
