package service;

/**
 * Interface: Reportable
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface: Defines a standard contract for any class that generates visual or text reports.
 * 2. Polymorphism: Different report generators (MonthlyReport, CategoryWiseReport, UserSummaryReport)
 *    implement this interface. The caller can invoke 'generateReport()' without knowing the specific
 *    underlying report type.
 */
public interface Reportable {

    /**
     * Executes the report generation and outputs the formatted summary.
     */
    void generateReport();

    /**
     * Returns a human-readable title for the report.
     *
     * @return Report title string
     */
    String getReportTitle();
}
