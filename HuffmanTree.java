// This class HuffmanTree determines the binary representations of characters/symbols in a way
// where the more frequent a symbol, the shorter the binary represention
import java.io.*;
import java.util.*;
public class HuffmanTree{
   private HuffmanNode overallRoot; //top of the Huffman tree
   // post: uses the input array where the index is the integer represetation of the symbol and
   //       the value is the frequency of that symbol; then determines the binary representations
   //       in a way where the more frequent a symbol, the shorter the binary represention
   public HuffmanTree(int[] count){
      Queue<HuffmanNode> treeQ = new PriorityQueue<HuffmanNode>();
      for (int i = 0; i < count.length; i++) {
        if (count[i] > 0) {
          treeQ.add(new HuffmanNode(count[i], i));
        }
      }
      //System.out.println(treeQ);
      while (treeQ.size() > 1) {
        HuffmanNode zeroNode = treeQ.poll();
        HuffmanNode oneNode = treeQ.poll();
        HuffmanNode combinedNode = new HuffmanNode(zeroNode.frequency + oneNode.frequency, 0, zeroNode, oneNode);
        treeQ.add(combinedNode);
        //System.out.println(treeQ);
      }
      overallRoot = treeQ.poll();
   }
   // post: prints to the output file in standard form in pairs where the first line is the
   //       integer representation of the symbol and the second line is the binary
   public void write(PrintStream output){
      write(output, overallRoot,"");
   }
   // post: prints to the output file if leaf node (the symbol and the binary rep), otherwise
   //       keeps going down branches
   private void write(PrintStream output, HuffmanNode root, String binary){
     if (root.zero == null && root.one == null) {
       output.println(root.symbol);
       output.println(binary);
     }
     else {
       write(output, root.zero, binary + "0");
       write(output, root.one, binary + "1");
     }
   }
}
