import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to MySQL database.");


            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n-- Main Menu --");
                System.out.println("1. User");
                System.out.println("2. Admin");
                System.out.println("3. Exit");
                System.out.print("Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> UserHandler.handleUser(conn, scanner);
                    case 2 -> AdminHandler.handleAdmin(conn, scanner);
                    case 3 -> {
                        exit = true;
                        System.out.println("Thank You");
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }

            scanner.close();
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}
