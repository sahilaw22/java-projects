import java.util.*;
import java.io.*;

public class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();
    private int nextId = 1001;
    private static final String FILE_PATH = "data/expenses.csv";

    public ExpenseManager() {
        loadFromFile();
    }

    public void addExpense(Scanner sc) {
        ConsoleUI.clearScreen();
        System.out.println(ConsoleUI.CYAN + "===== ALL NEW EXPENSE =====" + ConsoleUI.RESET);

        System.out.print("What's this expense for?\n>  ");
        String desc = sc.nextLine();

        System.out.print("Amount (Rs): ");
        double amt = sc.nextDouble();
        sc.nextLine();

        System.out.print("\nSelect Category [ Food | Transport | Rent | Bills | Entertainment | Others ]\n> ");
        String cat = sc.nextLine();
        System.out.print("\nDate (dd-mm-yyyy): ");
        String date = sc.nextLine();

        expenses.add(new Expense(nextId++, date, desc, amt, cat));
        saveToFile();
        System.out.println(ConsoleUI.GREEN + "\nExpense added successfully!" + ConsoleUI.RESET);
    }

    public void viewAll() {
        ConsoleUI.clearScreen();
        System.out.println(ConsoleUI.CYAN + "\n===== ALL EXPENSES =====\n" + ConsoleUI.RESET);
        if (expenses.isEmpty()) {
            System.out.println("\nNo expenses recorded yet.");
            return;
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-28s %-10s %-15s\n",
                "ID", "Date", "Description", "Amount", "Category");
        System.out.println("--------------------------------------------------------------------");
        for (Expense e : expenses) {
            System.out.printf("%-4d %-12s %-28s Rs.%-7.2f %-15s\n",
                    e.getId(), e.getDate(), e.getDescription(),
                    e.getAmount(), e.getCategory());
        }
        System.out.println("--------------------------------------------------------------------");
    }

    public void searchByCategory(Scanner sc) {
        ConsoleUI.clearScreen();
        System.out.print("Which Category?\n>  ");
        String cat = sc.nextLine().trim();

        System.out.println(ConsoleUI.CYAN + "\n===== Expenses in '" + cat + "' =====" + ConsoleUI.RESET);
        boolean found = false;

        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-4s %-12s %-28s %-10s %-15s\n",
                "ID", "Date", "Description", "Amount", "Category");
        System.out.println("--------------------------------------------------------------------");
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(cat)) {
                System.out.printf("%-4d %-12s %-28s Rs.%-7.2f %-15s\n",
                        e.getId(), e.getDate(), e.getDescription(),
                        e.getAmount(), e.getCategory());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No expenses found in this category.");
        }
    }

    public void monthlyReport() {
        ConsoleUI.clearScreen();
        System.out.println(ConsoleUI.CYAN + "\n===== Monthly Expense Report =====\n" + ConsoleUI.RESET);

        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        double total = 0;
        Map<String, Double> catTotal = new HashMap<>();

        for (Expense e : expenses) {
            total += e.getAmount();
            catTotal.put(e.getCategory(), catTotal.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }

        System.out.printf("Total Expenses This Period : Rs.%.2f\n\n", total);
        System.out.println("---------------------------");
        System.out.println("  Category-Wise Breakdown");
        System.out.println("---------------------------");
        for (Map.Entry<String, Double> entry : catTotal.entrySet()) {
            System.out.printf("> %-15s : Rs.%.2f\n", entry.getKey(), entry.getValue());
        }
    }

    public void updateExpense(Scanner sc) {
        viewAll();
        if (expenses.isEmpty()) return;

        System.out.print("\nEnter ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Expense e : expenses) {
            if (e.getId() == id) {
                System.out.print("\nCurrent Description: " + e.getDescription());
                System.out.print("\nEnter New Description : ");
                String desc = sc.nextLine();
                if (!desc.isEmpty()) e.setDescription(desc);

                System.out.print("\nCurrent Amount: " + e.getAmount());
                System.out.print("\nEnter New Amount : ");
                String amtStr = sc.nextLine();
                if (!amtStr.isEmpty()) e.setAmount(Double.parseDouble(amtStr));

                System.out.print("\nCurrent Category: " + e.getCategory());
                System.out.print("\nUpdate Category : ");
                String cat = sc.nextLine();
                if (!cat.isEmpty()) e.setCategory(cat);

                saveToFile();
                System.out.println(ConsoleUI.YELLOW + "\nExpense updated successfully!" + ConsoleUI.RESET);
                return;
            }
        }
        System.out.println(ConsoleUI.RED + "ID not found!" + ConsoleUI.RESET);
    }

    public void deleteExpense(Scanner sc) {
        viewAll();
        if (expenses.isEmpty()) return;

        System.out.print("\nEnter ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean removed = expenses.removeIf(e -> e.getId() == id);
        if (removed) {
            saveToFile();
            System.out.println(ConsoleUI.RED + "\nExpense deleted successfully!" + ConsoleUI.RESET);
        } else {
            System.out.println(ConsoleUI.RED + "ID not found!" + ConsoleUI.RESET);
        }
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("id,date,description,amount,category");
            for (Expense e : expenses) {
                pw.println(e.getId() + "," + e.getDate() + "," + e.getDescription() +
                        "," + e.getAmount() + "," + e.getCategory());
            }
        } catch (Exception e) {
            System.out.println(ConsoleUI.RED + "Error saving to file: " + e.getMessage() + ConsoleUI.RESET);
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
