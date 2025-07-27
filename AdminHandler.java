import java.io.FileWriter;
import java.sql.*;
import java.util.Scanner;

public class AdminHandler {

    public static void handleAdmin(Connection conn, Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (!isAdminValid(conn, username, password)) {
            System.out.println("Invalid admin credentials.");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n-- Admin Dashboard --");
            System.out.println("1. View All Users");
            System.out.println("2. Change User Password");
            System.out.println("3. Export User Expenses");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewUsers(conn);
                    break;
                case 2:
                    changePassword(conn, scanner);
                    break;
                case 3:
                    exportUserExpenses(conn, scanner);
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean isAdminValid(Connection conn, String username, String password) {
        try {
            String sql = "SELECT * FROM admin WHERE name = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking admin credentials.");
            return false;
        }
    }

    private static void viewUsers(Connection conn) {
        try {
            String sql = "SELECT id, username FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n-- Registered Users --");
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                System.out.println("ID: " + id + " | Username: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch users.");
        }
    }

    private static void changePassword(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            String sql = "UPDATE users SET password = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to change password.");
        }
    }

    private static void exportUserExpenses(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter user ID to export: ");
            int userId = scanner.nextInt();
            scanner.nextLine();

            String sql = "SELECT * FROM expenses WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            FileWriter csvWriter = new FileWriter("admin_export_user_" + userId + ".csv");
            csvWriter.append("ID,Amount,Category,DateSpent,Description\n");

            while (rs.next()) {
                int id = rs.getInt("id");
                double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                Date dateSpent = rs.getDate("date_spent");
                String description = rs.getString("description");

                csvWriter.append(id + "," + amount + "," + category + "," + dateSpent + "," + description + "\n");
            }

            csvWriter.close();
            System.out.println("User expenses exported to CSV file.");
        } catch (Exception e) {
            System.out.println("Failed to export user expenses.");
        }
    }
}
