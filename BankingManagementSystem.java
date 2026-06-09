import java.sql.*;
import java.util.Scanner;

public class BankingManagementSystem {
    static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String USER = "root";
    static final String PASSWORD = "password";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Banking Management System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createAccount(sc);
                case 2 -> deposit(sc);
                case 3 -> withdraw(sc);
                case 4 -> checkBalance(sc);
                case 5 -> System.exit(0);
                default -> System.out.println("Invalid choice");
            }
        }
    }

    static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void createAccount(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Account No: ");
            int accNo = sc.nextInt();
            sc.nextLine();
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Initial Balance: ");
            double balance = sc.nextDouble();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO accounts(acc_no,name,balance) VALUES(?,?,?)");
            ps.setInt(1, accNo);
            ps.setString(2, name);
            ps.setDouble(3, balance);
            ps.executeUpdate();

            System.out.println("Account Created!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deposit(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Account No: ");
            int accNo = sc.nextInt();
            System.out.print("Amount: ");
            double amount = sc.nextDouble();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?");
            ps.setDouble(1, amount);
            ps.setInt(2, accNo);
            ps.executeUpdate();

            System.out.println("Deposit Successful!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void withdraw(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Account No: ");
            int accNo = sc.nextInt();
            System.out.print("Amount: ");
            double amount = sc.nextDouble();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE acc_no = ? AND balance >= ?");
            ps.setDouble(1, amount);
            ps.setInt(2, accNo);
            ps.setDouble(3, amount);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Withdrawal Successful!" : "Insufficient Balance!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void checkBalance(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Account No: ");
            int accNo = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM accounts WHERE acc_no = ?");
            ps.setInt(1, accNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Balance: " + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
