public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(String name, User holder, Bank theBank, double interestRate) {
        super(name, holder, theBank);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void applyInterest() {
        double balance = this.getBalance();
        double interest = balance * (this.interestRate / 100);
        this.addTransaction(interest, "Interest applied");
    }
}

