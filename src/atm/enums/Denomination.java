package atm.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public enum Denomination {

	FIFTYs(50),
	TWENTIEs(20),
	TENSs(10),
	FIVEs(5),
	ONEs(1);
	/*
	 *Add more denominations in future
	 */
	 
  public static final DenomBST DENOM_TREE;
  
 /*
  * Static block. 
  * It will create Binary Search Tree from sorted enum values upon app startup
  * Each node will contain value of a denomination
  * Each node will also contain a sorted set of denominations which are lower than the denomination value of node.
  * For example:
  * SmallerDenoms than 1 = []
Denom 5
SmallerDenoms than 5 = [1]
Denom 10
SmallerDenoms than 10 = [5, 1]
Denom 20
SmallerDenoms than 20 = [10, 5, 1]
Denom 50
SmallerDenoms than 50 = [20, 10, 5, 1]
  *  
  *  When user will do withdrawl, will check which node the withdrawl value falls in BST. 
  *  Retrieve the list of denoms lower than node value and search if the denoms are available for withdrawl. 
  *  If more denominations are added in future, this design will work.
  *  
  * 
 */
  static {
  
    int i = 0;
    int[] enumArray = new int[Denomination.values().length ];
    
    for (Denomination denom : Denomination.values()) { 
      enumArray[i++] = denom.getValue();
    }
     Arrays.sort(enumArray);
     DENOM_TREE = createDenomTree(enumArray); 
   
     //Test Code
     //System.out.println("GetAllDenomSet "+DENOM_TREE.getAllDenomSet());
    // printTree(DENOM_TREE.getRoot());
     
      
  }
  
//Constructor
 private Denomination(int value) {
      this.value = value;
  }



  private int value;
  public int getValue() { 
     return value;	
}

 private static DenomBST createDenomTree(int[] enumArray){
 
  DenomBST bst = new DenomBST();
  bst.createDenomTree(enumArray, 0, enumArray.length -1);
  bst.setMin(enumArray[0]);
  bst.setMax(enumArray[enumArray.length - 1]);
  return bst;
 }
 



private static void printTree(DenomNode root){

 if(root == null) return;

printTree(root.left);
System.out.println("Denom "+root.denomValue);
System.out.println("SmallerDenoms than "+root.denomValue +" = "+root.smallDenomSet);
printTree(root.right);

}


}
