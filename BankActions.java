public interface BankActions {
    void withdraw(double amount);
    void deposit(double amount);
    void transfer(double amount, Account toAccount);
}
