import java.io.*;
import java.util.*;
public class HuffmanTester {
  public static int[] HuffmanArrayGenerator(String word) {
    int[] returnArray = new int[127];
    for (char c : word.toCharArray()) {
        returnArray[((int) c)] += 1;
     }
     return returnArray;
  }
  public static void main(String[] args) {
    int[] testArray = HuffmanArrayGenerator("hello world!");
    HuffmanTree HuffmanTester = new HuffmanTree(testArray);
    try {
      PrintStream printHuffman = new PrintStream(new FileOutputStream("huffmanencoding.txt"));
      HuffmanTester.write(printHuffman);
      File file = new File("huffmanencoding.txt");
      try {
        System.out.println("Reading from /huffhuffmanencoding.txt ...");
        Scanner sc = new Scanner(file);
         while (sc.hasNextLine()) {
             String line = sc.nextLine();
             System.out.println(line);
         }
         sc.close();
      }
      catch (FileNotFoundException e) {
       System.out.println("File not found.");
   }
    }
    catch (FileNotFoundException ex) {
       System.out.println("File not found.");
    }

  }
}
