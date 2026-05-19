import java.util.Scanner;

public class ConsoleUI {

    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String CYAN = "\033[36m";
    public static final String BOLD = "\033[1m";
    public static final String ORANGE = "\033[38;5;208m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printBanner() {
        clearScreen();
        System.out.println(CYAN + """

███████╗██╗  ██╗██████╗ ███████╗███╗   ██╗███████╗███████╗    ████████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗ 
██╔════╝╚██╗██╔╝██╔══██╗██╔════╝████╗  ██║██╔════╝██╔════╝    ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗
█████╗   ╚███╔╝ ██████╔╝█████╗  ██╔██╗ ██║███████╗█████╗         ██║   ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝
██╔══╝   ██╔██╗ ██╔═══╝ ██╔══╝  ██║╚██╗██║╚════██║██╔══╝         ██║   ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗
███████╗██╔╝ ██╗██║     ███████╗██║ ╚████║███████║███████╗       ██║   ██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║
╚══════╝╚═╝  ╚═╝╚═╝     ╚══════╝╚═╝  ╚═══╝╚══════╝╚══════╝       ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
""" + RESET);
        System.out.println();
    }

    public static void printMenu(String[] options, int selected) {
        for (int i = 0; i < options.length; i++) {
                System.out.println("    " + (i + 1) + ". " + options[i]);
            }
        }
        public static void pause(Scanner sc) {
            System.out.print("\nPress Enter to continue...");
            sc.nextLine();
        }
    }