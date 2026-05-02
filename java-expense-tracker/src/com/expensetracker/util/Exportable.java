package com.expensetracker.util;

public interface Exportable {

    String toCSVRow();

    default String toCSVHeader() {
        return "id,type,amount,description,date";
    }

    static String csvDelimiter() {
        return ",";
    }
}
