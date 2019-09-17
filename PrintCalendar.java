import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Calendar;
import java.io.FileWriter;

public class PrintCalendar {
  public static int[][] yearly = {{1,31},{2,28},{3,31},{4,30},{5,31},{6,30},{7,31},{8,31},{9,30},{10,31},{11,30},{12,31}};
  public static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
  public static String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
  private static boolean isLeap(int year) {
    boolean leap = false;
    if(year % 400 == 0) {
      leap = true;
    }
    else if (year % 100 == 0) {
      leap = false;
    }
    else if(year % 4 == 0) {
      leap = true;
    }
    else {
      leap = false;
    }
    return leap;
  }
  public static String printCalendar(int currentYear) {
    if (isLeap(currentYear)) {
      yearly[1][1] = 29;
    }
    int daysPassed = LocalDate.of(currentYear, 1, 1).getDayOfWeek().getValue();
    String calstring = "";
    for(int i = 0; i < 12; i++) {
      calstring = calstring + monthNames[i] + "\n";
      for (int j = 0; j < 7; j++) {
        calstring = calstring + dayNames[j] + "\t";
      }
      calstring = calstring + "\n";
      for (int k = 0; k < daysPassed % 7; k++) {
        calstring = calstring + "\t";
      }
      for (int l = 1; l <= yearly[i][1]; l++) {
        calstring = calstring + l;
        if ((daysPassed + l) % 7 == 0) {
          calstring = calstring + "\n";
        }
        else{
          calstring = calstring + "\t";
        }
      }
      daysPassed += yearly[i][1];
      calstring = calstring + "\n\n";
    }
    return calstring;
  }
  public static String csvCalendar(int currentYear) {
    if (isLeap(currentYear)) {
      yearly[1][1] = 29;
    }
    int daysPassed = LocalDate.of(currentYear, 1, 1).getDayOfWeek().getValue();
    String calstring = "";
    for(int i = 0; i < 12; i++) {
      calstring = calstring + monthNames[i] + "\n";
      for (int j = 0; j < 7; j++) {
        calstring = calstring + dayNames[j] + ",";
      }
      calstring = calstring + "\n";
      for (int k = 0; k < daysPassed % 7; k++) {
        calstring = calstring + ",";
      }
      for (int l = 1; l <= yearly[i][1]; l++) {
        calstring = calstring + l;
        if ((daysPassed + l) % 7 == 0) {
          calstring = calstring + "\n";
        }
        else{
          calstring = calstring + ",";
        }
      }
      daysPassed += yearly[i][1];
      calstring = calstring + "\n\n";
    }
    return calstring;
  }
  public static void main(String[] args) {
    System.out.println(printCalendar(Calendar.getInstance().get(Calendar.YEAR))); //current year test
    //System.out.println(printCalendar(2016)); // leap year test
    try{
     FileWriter fw=new FileWriter("calendar.csv");
     fw.write(csvCalendar(Calendar.getInstance().get(Calendar.YEAR)));
     fw.close();
    }
    catch(Exception e){
      System.out.println(e);
    }
  }
}
