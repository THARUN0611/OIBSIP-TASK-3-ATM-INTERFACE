import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ATM {
    private List<User> users;
    private User currentUser;
    private boolean loggedIn;
    private Scanner scanner;

    public ATM() {
        users = new ArrayList<>();
        // Add sample users
        users.add(new User("tharun", "1234"));
        users.add(new User("muthu", "5678"));
        loggedIn = false;
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (!loggedIn) {
                System.out.print("Enter user ID: ");
                String userId = scanner.nextLine();
                System.out.print("Enter user PIN: ");
                String userPin = scanner.nextLine();
                if (authenticateUser(userId, userPin)) {
                    System.out.println("Authentication successful!");
                    loggedIn = true;
                } else {
                    System.out.println("Authentication failed! Please try again.");
                }
            } else {
                showMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayTransactionHistory();
                        break;
                    case 2:
                        performWithdrawal();
                        break;
                    case 3:
                        performDeposit();
                        break;
                    case 4:
                        performTransfer();
                        break;
                    case 5:
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        }
    }

    private boolean authenticateUser(String userId, String userPin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getUserPin().equals(userPin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private void showMenu() {
        System.out.println("===== ATM Menu =====");
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
        System.out.print("Enter your choice: ");
    }

    private void displayTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction transaction : currentUser.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    private void performWithdrawal() {
        System.out.print("Enter withdrawal amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        Withdrawal withdrawal = new Withdrawal(amount);
        currentUser.addTransaction(withdrawal);
        System.out.println("Withdrawal successful! Current balance: " + currentUser.getBalance());
    }

    private void performDeposit() {
        System.out.print("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        Deposit deposit = new Deposit(amount);
        currentUser.addTransaction(deposit);
        System.out.println("Deposit successful! Current balance: " + currentUser.getBalance());
    }

    private void performTransfer() {
        System.out.print("Enter recipient's user ID: ");
        String recipientId = scanner.nextLine();
        User recipient = null;
        for (User user : users) {
            if (user.getUserId().equals(recipientId)) {
                recipient = user;
                break;
            }
        }
        if (recipient == null) {
            System.out.println("Recipient user not found! Please try again.");
            return;
        }
        System.out.print("Enter transfer amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (currentUser.getBalance() < amount) {
            System.out.println("Insufficient balance! Please try again.");
            return;
        }
        Withdrawal withdrawal = new Withdrawal(amount);
        currentUser.addTransaction(withdrawal);
        Deposit deposit = new Deposit(amount);
        recipient.addTransaction(deposit);
        System.out.println("Transfer successful! Current balance: " + currentUser.getBalance());
    }

    private void logout() {
        loggedIn = false;
        currentUser = null;
        System.out.println("Logged out successfully!");
    }
}

class User {
    private String userId;
    private String userPin;
    private double balance;
    private List<Transaction> transactionHistory;

    public User(String userId, String userPin) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        if (transaction instanceof Withdrawal) {
            balance -= transaction.getAmount();
        } else if (transaction instanceof Deposit) {
            balance += transaction.getAmount();
        }
    }
}

abstract class Transaction {
    protected double amount;

    public Transaction(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

class Withdrawal extends Transaction {
    public Withdrawal(double amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Withdrawal: " + amount;
    }
}

class Deposit extends Transaction {
    public Deposit(double amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Deposit: " + amount;
    }
}

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
