import java.util.Scanner;

public class Atm {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank theBank = new Bank("State Bank of India");
        User aUser = theBank.addUser("Raghuram", "Aluru", "724142");

        // Automatically create the first account for the user
        aUser.createNewAccount();

        User curUser;

        while (true) {
            curUser = Atm.mainMenuPrompt(theBank, sc);
            Atm.printUserMenu(curUser, sc);
        }
    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userID;
        String pin;
        User authUser;

        do {
            System.out.printf("\n\nWelcome to %s \n\n", theBank.getName());
            System.out.printf("Enter User ID: ");
            userID = sc.nextLine();
            System.out.printf("Enter the pin: ");
            pin = sc.nextLine();
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.printf("Incorrect User ID or pin. Please try again. ");
            }
        } while (authUser == null);

        return authUser;
    }

    public static void printUserMenu(User curUser, Scanner sc) {
        curUser.printAccountSummary();

        int choice;

        do {
            System.out.printf("Welcome %s, what would you like to do?\n", curUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Make Withdrawal");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Create a new account");
            System.out.println("  6) Exit");
            System.out.println("\n");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice < 1 || choice > 6) {
                System.out.println("Invalid choice, please choose from the above options");
            }

        } while (choice < 1 || choice > 6);

        switch (choice) {
            case 1:
                Atm.showTransactionHistory(curUser, sc);
                break;
            case 2:
                Atm.withdrawalFunds(curUser, sc);
                break;
            case 3:
                Atm.depositFunds(curUser, sc);
                break;
            case 4:
                Atm.transferFunds(curUser, sc);
                break;
            case 5:
                curUser.createNewAccount();
                break;
        }

        if (choice != 6) {
            Atm.printUserMenu(curUser, sc);
        }
    }

    public static void showTransactionHistory(User curUser, Scanner sc) {
        int theAcc;

        do {
            System.out.printf("Enter the account number (1 to %d) of the account\n whose transactions you want to see: ", curUser.numAccounts());
            theAcc = sc.nextInt() - 1;
            sc.nextLine();
            if (theAcc < 0 || theAcc >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcc < 0 || theAcc >= curUser.numAccounts());

        curUser.printAcctTransHistory(theAcc);
    }

    public static void transferFunds(User curUser, Scanner sc) {
        int fromAcc;
        int toAcc;
        double amount;
        double accBal;

        do {
            System.out.printf("Enter the account number (1 to %d) to transfer from: ", curUser.numAccounts());
            fromAcc = sc.nextInt() - 1;
            sc.nextLine();
            if (fromAcc < 0 || fromAcc >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcc < 0 || fromAcc >= curUser.numAccounts());

        accBal = curUser.getAcctBalance(fromAcc);

        do {
            System.out.printf("Enter the account number (1 to %d) to transfer to: ", curUser.numAccounts());
            toAcc = sc.nextInt() - 1;
            sc.nextLine();
            if (toAcc < 0 || toAcc >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcc < 0 || toAcc >= curUser.numAccounts());

        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", accBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > accBal) {
                System.out.printf("Amount must not be greater than balance of $%.02f.\n", accBal);
            }
        } while (amount < 0 || amount > accBal);

        curUser.addAcctTransaction(fromAcc, -1 * amount, String.format("Transfer to account %s", curUser.getAcctUUID(toAcc)));
        curUser.addAcctTransaction(toAcc, amount, String.format("Transfer from account %s", curUser.getAcctUUID(fromAcc)));
    }

    public static void withdrawalFunds(User curUser, Scanner sc) {
        int fromAcc;
        double amount;
        double accBal;
        String memo;

        do {
            System.out.printf("Enter the account number (1 to %d) to withdraw from: ", curUser.numAccounts());
            fromAcc = sc.nextInt() - 1;
            sc.nextLine();
            if (fromAcc < 0 || fromAcc >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcc < 0 || fromAcc >= curUser.numAccounts());

        accBal = curUser.getAcctBalance(fromAcc);

        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $", accBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > accBal) {
                System.out.printf("Amount must not be greater than balance of $%.02f.\n", accBal);
            }
        } while (amount < 0 || amount > accBal);

        System.out.printf("Enter a memo: ");
        memo = sc.nextLine();

        curUser.addAcctTransaction(fromAcc, -1 * amount, memo);
    }

    public static void depositFunds(User curUser, Scanner sc) {
        int toAcc;
        double amount;
        String memo;

        do {
            System.out.printf("Enter the account number (1 to %d) to deposit in: ", curUser.numAccounts());
            toAcc = sc.nextInt() - 1;
            sc.nextLine();
            if (toAcc < 0 || toAcc >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcc < 0 || toAcc >= curUser.numAccounts());

        do {
            System.out.printf("Enter the amount to deposit: $");
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        System.out.printf("Enter a memo: ");
        memo = sc.nextLine();

        curUser.addAcctTransaction(toAcc, amount, memo);
    }
}

