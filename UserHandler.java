import java.io.FileWriter;
import java.sql.*;
import java.util.Scanner;

public class UserHandler {
    public static void handleUser(Connection conn, Scanner scanner) {
        System.out.println("\n-- User Menu --");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> register(conn, scanner);
            case 2 -> login(conn, scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void register(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if(username!="" || password!=""){
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            System.out.println("Registration successful!");}
            else{
              System.out.println("Registration failed.Invalid Details");  
            }
        } catch (SQLException e) {
            System.out.println("Registration failed. Username might already exist.");
        }
    }

    private static void login(Connection conn, Scanner scanner) {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                System.out.println("Login successful.");
                userDashboard(conn, scanner, userId);
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void userDashboard(Connection conn, Scanner scanner, int userId) {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- User Dashboard --");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Export to CSV");
            System.out.println("4. Logout");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addExpense(conn, scanner, userId);
                case 2 -> viewExpenses(conn, userId);
                case 3 -> exportToCSV(conn, userId);
                case 4 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addExpense(Connection conn, Scanner scanner, int userId) {
        try {
            System.out.print("Amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Category: ");
            String category = scanner.nextLine();
            System.out.print("Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Description: ");
            String desc = scanner.nextLine();

            String sql = "INSERT INTO expenses (user_id, amount, category, date_spent, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setDate(4, java.sql.Date.valueOf(date));
            ps.setString(5, desc);
            ps.executeUpdate();

            System.out.println("Expense added.");
        } catch (Exception e) {
            System.out.println("Failed to add expense.");
        }
    }

    private static void viewExpenses(Connection conn, int userId) {
        try {
            String sql = "SELECT * FROM expenses WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n-- Your Expenses --");
            while (rs.next()) {
                System.out.printf("ID: %d | Amount: %.2f | Category: %s | Date: %s | Description: %s\n",
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getDate("date_spent"),
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve expenses.");
        }
    }

    private static void exportToCSV(Connection conn, int userId) {
        try {
            String sql = "SELECT * FROM expenses WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            FileWriter csvWriter = new FileWriter("user_" + userId + "_expenses.csv");
            csvWriter.append("ID,Amount,Category,Date,Description\n");

            while (rs.next()) {
                csvWriter.append(String.format("%d,%.2f,%s,%s,%s\n",
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getDate("date_spent"),
                        rs.getString("description")));
            }

            csvWriter.close();
            System.out.println("Exported to CSV.");
        } catch (Exception e) {
            System.out.println("Failed to export.");
        }
    }
}
