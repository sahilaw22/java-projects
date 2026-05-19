package com.expensetrackergui.etgui;

public class Expense {
    private int id;
    private String date;        // dd-mm-yyyy
    private String description;
    private double amount;
    private String category;

    // create a new expense
    public Expense(int id, String date, String description, double amount, String category) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    // used to read the data
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }

    // to update data (used in update feature)
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return String.format("%-4d %-12s %-28s Rs.%-7.2f %-15s",
                id, date, description, amount, category);
    }
}