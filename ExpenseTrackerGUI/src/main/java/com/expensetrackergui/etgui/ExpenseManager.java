package com.expensetrackergui.etgui;

import java.util.*;
import java.io.*;

public class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();
    private int nextId = 1001;
    private static final String FILE_PATH = "data/expenses.csv";

    public ExpenseManager() {
        loadFromFile();
    }

    public void addExpense(String description, double amount, String category, String date) {
        expenses.add(new Expense(nextId++, date, description, amount, category));
        saveToFile();
    }

    public void deleteExpenseById(int id) {
        expenses.removeIf(e -> e.getId() == id);
        saveToFile();
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("id,date,description,amount,category");
            for (Expense e : expenses) {
                pw.println(e.getId() + "," + e.getDate() + "," + e.getDescription() +
                        "," + e.getAmount() + "," + e.getCategory());
            }
        } catch (Exception e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (Scanner fileSc = new Scanner(new File(FILE_PATH))) {
            if (fileSc.hasNextLine()) {
                fileSc.nextLine(); // skip CSV header
            }
            while (fileSc.hasNextLine()) {
                String line = fileSc.nextLine();
                String[] parts = line.split(",", -1);
                if (parts.length == 5) {
                    Expense e = new Expense(Integer.parseInt(parts[0]), parts[1],
                            parts[2], Double.parseDouble(parts[3]), parts[4]);
                    expenses.add(e);
                    if (e.getId() >= nextId) nextId = e.getId() + 1;
                }
            }
        } catch (Exception e) {
            // file doesn't exist yet
        }
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
