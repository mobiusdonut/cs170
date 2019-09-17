public class TestAccount {
  public static void main (String[] args) {
    System.out.println("");
    Account account = new CheckingAccount(1122, 20000);
    account.setAnnualInterestRate(4.5);
    account.withdraw(2500);
    account.deposit(3000);
    System.out.println(account);
    System.out.println("Monthly interest is " + account.getMonthlyInterest());
    System.out.println("This account was created at " + account.getDateCreated());
    System.out.println("");
    CheckingAccount checking = new CheckingAccount(2233, 0);
    checking.setAnnualInterestRate(4.0);
    checking.withdraw(2500);
    checking.deposit(100);
    System.out.println(checking);
    System.out.println("Monthly interest is " + checking.getMonthlyInterest());
    System.out.println("This account was created at " + checking.getDateCreated());
    System.out.println("");
    SavingAccount saving = new SavingAccount(3344, 10000);
    saving.setAnnualInterestRate(5.0);
    saving.withdraw(4000);
    saving.deposit(700);
    System.out.println(saving);
    System.out.println("Monthly interest is " + saving.getMonthlyInterest());
    System.out.println("This account was created at " + saving.getDateCreated());
    System.out.println("");
  }
}
