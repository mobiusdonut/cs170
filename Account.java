import java.util.Date;

abstract class Account {
  private int id = 0;
  protected double balance;
  protected static double annualInterestRate = 0;
  private Date dateCreated;

  public Account() {
    dateCreated = new Date();
  }
  public Account(int currId, double currBalance) {
    id = currId;
    balance = currBalance;
    dateCreated = new Date();
  }

  public int getId() {
    return this.id;
  }
  public void setId(int newId) {
    id = newId;
  }
  public double getBalance() {
    return balance;
  }
  public void setBalance(double newBalance) {
    balance = newBalance;
  }
  public static double getAnnualInterestRate() {
    return annualInterestRate;
  }
  public static void setAnnualInterestRate(double newAnnualInterestRate) {
    annualInterestRate = newAnnualInterestRate;
  }
  public Date getDateCreated() {
    return dateCreated;
  }
  public double getMonthlyInterest() {
    return balance * (annualInterestRate / 1200);
  }
  public boolean withdraw(double amount) {
    boolean succ = false;
    balance -= amount;
    succ = true;
    return succ;
  }
  public void deposit(double amount) {
    balance += amount;
  }
  public abstract boolean transfer(Account trans, double amount);
  public String toString() {
    return "Account " + String.valueOf(this.getId()) + " has balance " + String.valueOf(this.getBalance());
  }
}
