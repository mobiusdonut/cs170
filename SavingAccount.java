class SavingAccount extends Account implements Comparable<Account> {
  public SavingAccount() {
    super();
  }
  public SavingAccount(int acctNo, double balance) {
    super(acctNo, balance);
  }
  public boolean transfer(Account acct, double amount) {
    boolean succ = false;
    acct.withdraw(amount);
    super.deposit(amount);
    succ = true;
    return succ;
  }
  public boolean withdraw(double amount) {
    boolean succ = false;
    super.withdraw(amount);
    succ = true;
    return succ;
  }
  public int compareTo(Account other) {
    if (super.getBalance() > other.getBalance()) {
      return 1;
    }
    else {
      return 0;
    }
  }
  public String toString() {
    return "SavingAccount " + String.valueOf(this.getId()) + " has balance " + String.valueOf(this.getBalance());
  }
}
