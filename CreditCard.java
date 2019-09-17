import java.util.Scanner;

public class CreditCard {
  public static void validate(long creditnum) {
    String iternum = Long.toString(creditnum);
    int validator = 0;
    int[] credarray = new int[iternum.length()];
    for (int i = 0; i < iternum.length(); i++) {
      credarray[i] = iternum.charAt(i) - '0';
    }
    for (int i = iternum.length() - 2; i >= 0; i -= 2) {
      validator += ((credarray[i] * 2) / 10 + (credarray[i] * 2) % 10);
    }
    for (int i = iternum.length() - 1; i >= 0; i -= 2) {
      validator += credarray[i];
    }
    System.out.print(creditnum + " is ");
    if (validator % 10 != 0) {
      System.out.print("in");
    }
    System.out.println("valid");
  }
  public static String base16Encoder (long creditnum) {
    char[] chars = (Long.toString(creditnum)).toCharArray();
    StringBuilder hex = new StringBuilder();
    for (char ch : chars) {
        hex.append(Integer.toHexString((int) ch));
    }

    return hex.toString();
  }
  public static String base16Decoder (String hexnum) {
    StringBuilder output = new StringBuilder("");

    for (int i = 0; i < hexnum.length(); i += 2) {
        String str = hexnum.substring(i, i + 2);
        output.append((char) Integer.parseInt(str, 16));
    }

    return output.toString();
  }
  public static void main(String[] args) {
    Scanner cred = new Scanner(System.in);
    System.out.print("Enter a credit card number as a long integer: ");
    long creditnum = Long.parseLong(cred.nextLine());
    validate(creditnum);
    String encoded = base16Encoder(creditnum);
    System.out.println("After encoding: " + encoded);
    String decoded = base16Decoder(encoded);
    System.out.println("After decoding: " + decoded);
  }
}
