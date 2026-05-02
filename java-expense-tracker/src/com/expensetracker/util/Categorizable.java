package com.expensetracker.util;

public interface Categorizable {
    String getCategory();
    void setCategory(String category);

    default String getCategoryLabel() {
        return "[" + getCategory().toUpperCase() + "]";
    }
}
