class CheckingAccount extends Account implements Comparable<Account> {
  public CheckingAccount() {
    super();
  }
  public CheckingAccount(int acctNo, double balance) {
    super(acctNo, balance);
  }
  public boolean transfer(Account acct, double amount) {
    boolean succ = false;
    if (acct.getBalance() > amount) {
      acct.withdraw(amount);
      super.deposit(amount);
      succ = true;
    }
    return succ;
  }
  public boolean withdraw(double amount) {
    boolean succ = false;
    if (this.getBalance() - amount > -500) {
      super.withdraw(amount);
      succ = true;
    }
    else {
      System.out.println("Account balance is too low to withdraw!");
    }
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
    return "CheckingAccount " + String.valueOf(this.getId()) + " has balance " + String.valueOf(this.getBalance());
  }
}
