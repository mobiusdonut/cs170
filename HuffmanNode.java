// This class HuffmanNode has two int fields for the frequency and the symbol/character, it also
// has two fields linking to other HuffManNodes, comparing HuffmanNodes and based on the
// frequencies

public class HuffmanNode implements Comparable<HuffmanNode>{
   public int frequency; //number of times symbol appears
   public int symbol; //byte value representing a specific character
   public HuffmanNode zero; //left branch on binary tree
   public HuffmanNode one; //right branch on binary tree

   // post: stores the inputted arguments as the intial state
   public HuffmanNode(int freq, int symbol){ //leaf nodes
      this(freq, symbol, null, null);
   }

   // post: stores the inputted arguments as the intial state, input int symbol is -1 when
   //       not a leaf node aka the node doesn't represent a specific symbol
   public HuffmanNode(int freq, int symbol, HuffmanNode left, HuffmanNode right){
      frequency = freq;
      this.symbol = symbol;
      zero = left;
      one = right;
   }

   // post: compares the frequencies of the two HuffmanNodes, returns pos when frequency of this
   //       HuffmanNode's frequency is greater, returns neg when frequency of inputted HuffmanNode
   //       is greater, return 0 when frequencies are equal
   public int compareTo(HuffmanNode other){
      if(this.frequency != other.frequency){
         return this.frequency - other.frequency;
      } else{
         return 0;
      }
  }
  public String toString() {
      return "symbol " + symbol + " with value of " + ((char) symbol) + " with frequency " + frequency + "\n";
  }
}
