import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        String[] menuOptions = {
                "Add New Expense",
                "View All Expenses",
                "Search by Category",
                "Monthly Report",
                "Update Expense",
                "Delete Expense",
                "Exit"
        };

        int selected = 0;

        while (true) {
            ConsoleUI.printBanner();
            System.out.println(ConsoleUI.YELLOW + "Track every expense and manage it effortlessly." + ConsoleUI.RESET);
            System.out.println();
            ConsoleUI.printMenu(menuOptions, selected);

            System.out.print("\nEnter choice (1-" + menuOptions.length + "): ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    manager.addExpense(sc);
                    break;
                case 2:
                    manager.viewAll();
                    break;
                case 3:
                    manager.searchByCategory(sc);
                    break;
                case 4:
                    manager.monthlyReport();
                    break;
                case 5:
                    manager.updateExpense(sc);
                    break;
                case 6:
                    manager.deleteExpense(sc);
                    break;
                case 7:
                    System.out.println(ConsoleUI.GREEN + ConsoleUI.BOLD + "\nThank you for using Personal Expense Tracker!\n" + ConsoleUI.RESET);
                    sc.close();
                    System.exit(0);
                    return;
                default:
                    System.out.println(ConsoleUI.RED + "Invalid choice! Please try again." + ConsoleUI.RESET);
            }
            ConsoleUI.pause(sc);
        }
    }
}
