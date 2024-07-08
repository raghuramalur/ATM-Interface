import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String uuid;
    private byte[] pinHash;
    private ArrayList<Account> accounts;
    private Bank bank;

    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bank = theBank;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: No such algorithm");
            e.printStackTrace();
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID();
        this.accounts = new ArrayList<Account>();

        System.out.printf("New User %s, %s with ID %s created.\n", firstName, lastName, this.uuid);
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);
    }

    public String getUUID() {
        return uuid;
    }

    public boolean validatePin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: No such algorithm");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void printAccountSummary() {
        System.out.printf("\n\n%s %s's accounts summary\n", this.firstName, this.lastName);
        for (int a = 0; a < accounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts() {
        return accounts.size();
    }

    public void printAcctTransHistory(int accIndex) {
        this.accounts.get(accIndex).printTransHistory();
    }

    public double getAcctBalance(int accIndex) {
        return this.accounts.get(accIndex).getBalance();
    }

    public void addAcctTransaction(int accIndex, double amt, String memo) {
        this.accounts.get(accIndex).addTransaction(amt, memo);
    }

    public String getAcctUUID(int accIndex) {
        return this.accounts.get(accIndex).getUUID();
    }

    public void createNewAccount() {
        int accountNumber = this.numAccounts() + 1;
        String accountName = "Account " + accountNumber;
        double interestRate = 2.5; // Define the interest rate
    
        Account newAccount = new SavingsAccount(accountName, this, this.bank, interestRate);
        this.addAccount(newAccount);
        this.bank.addAccount(newAccount);
    }

    public Bank getBank() {
        return this.bank;
    }
}
