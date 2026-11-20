package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Abstract Base Class: Category
 * 
 * OOP Concepts Demonstrated:
 * 1. Abstraction: Base class for all expense categories. Cannot be instantiated directly.
 * 2. Inheritance: Specialized concrete subclasses (FoodCategory, TransportationCategory, etc.)
 *    inherit common state and provide category-specific behaviors.
 * 3. Polymorphism: Subclasses override the abstract method 'getCategoryType()' with their classification.
 * 4. Static and Final Members: 'PREDEFINED_CATEGORIES' is public, static, and final (immutable constant).
 * 5. Encapsulation: Fields are protected/private with getters and setters.
 */
public abstract class Category {

    // Static & Final: Predefined category names mandated by requirements
    public static final List<String> PREDEFINED_CATEGORIES = Collections.unmodifiableList(
            Arrays.asList(
                    "Food",
                    "Transportation",
                    "Education",
                    "Shopping",
                    "Entertainment",
                    "Healthcare",
                    "Bills & Utilities",
                    "Others"
            )
    );

    // Encapsulated fields
    protected String name;
    protected String description;

    /**
     * Parameterized Constructor using 'this' keyword.
     *
     * @param name        Category name
     * @param description Brief description of what falls under this category
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Abstract Method (Polymorphism):
     * Returns whether the category is Essential, Discretionary, Utility, or Emergency.
     *
     * @return Classification type string
     */
    public abstract String getCategoryType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Demonstrates Method Overriding.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", getCategoryType(), name, description);
    }

    /**
     * Static Factory Method to instantiate the appropriate concrete Category subclass.
     * Demonstrates Polymorphism where the return type is the abstract base class 'Category'.
     *
     * @param categoryName Name of category
     * @return Concrete Category subclass instance
     */
    public static Category fromName(String categoryName) {
        if (categoryName == null) {
            return new OthersCategory();
        }
        switch (categoryName.trim()) {
            case "Food":
                return new FoodCategory();
            case "Transportation":
                return new TransportationCategory();
            case "Education":
                return new EducationCategory();
            case "Shopping":
                return new ShoppingCategory();
            case "Entertainment":
                return new EntertainmentCategory();
            case "Healthcare":
                return new HealthcareCategory();
            case "Bills & Utilities":
                return new BillsUtilitiesCategory();
            default:
                return new OthersCategory();
        }
    }

    // =========================================================================
    // Concrete Subclasses Demonstrating Inheritance & Polymorphic Method Overriding
    // =========================================================================

    public static class FoodCategory extends Category {
        public FoodCategory() {
            super("Food", "Groceries, dining out, snacks, beverages");
        }

        @Override
        public String getCategoryType() {
            return "Essential Needs";
        }
    }

    public static class TransportationCategory extends Category {
        public TransportationCategory() {
            super("Transportation", "Public transit, fuel, taxi, vehicle maintenance");
        }

        @Override
        public String getCategoryType() {
            return "Essential Commute";
        }
    }

    public static class EducationCategory extends Category {
        public EducationCategory() {
            super("Education", "Tuition, books, courses, exam fees, stationery");
        }

        @Override
        public String getCategoryType() {
            return "Self-Development";
        }
    }

    public static class ShoppingCategory extends Category {
        public ShoppingCategory() {
            super("Shopping", "Clothing, electronics, personal accessories");
        }

        @Override
        public String getCategoryType() {
            return "Discretionary Lifestyle";
        }
    }

    public static class EntertainmentCategory extends Category {
        public EntertainmentCategory() {
            super("Entertainment", "Movies, games, events, streaming subscriptions");
        }

        @Override
        public String getCategoryType() {
            return "Leisure & Fun";
        }
    }

    public static class HealthcareCategory extends Category {
        public HealthcareCategory() {
            super("Healthcare", "Doctor visits, medicines, lab tests, medical insurance");
        }

        @Override
        public String getCategoryType() {
            return "Emergency & Health";
        }
    }

    public static class BillsUtilitiesCategory extends Category {
        public BillsUtilitiesCategory() {
            super("Bills & Utilities", "Electricity, water, gas, Wi-Fi, mobile recharges");
        }

        @Override
        public String getCategoryType() {
            return "Fixed Recurring";
        }
    }

    public static class OthersCategory extends Category {
        public OthersCategory() {
            super("Others", "Miscellaneous and miscellaneous uncategorized expenses");
        }

        @Override
        public String getCategoryType() {
            return "General Expenses";
        }
    }
}
