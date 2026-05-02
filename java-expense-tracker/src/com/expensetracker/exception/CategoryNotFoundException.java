package com.expensetracker.exception;

public class CategoryNotFoundException extends TrackerException {

    private final String categoryName;

    public CategoryNotFoundException(String categoryName) {
        super("ERR_NO_CATEGORY", "Unknown category: '" + categoryName + "'");
        this.categoryName = categoryName;
    }

    public String getCategoryName() { return this.categoryName; }

    @Override
    public String toString() {
        return super.toString() + " — check allowed categories";
    }
}
