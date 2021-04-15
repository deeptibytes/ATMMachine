package atm.enums;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class DenomBST {

  private DenomNode root;
  private int max;
  private int min;
  private SortedSet<Integer> allDenomSet = new TreeSet<Integer>(Comparator.reverseOrder());
  
  
  
 public SortedSet<Integer> getAllDenomSet() {
	return allDenomSet;
}
public void setAllDenomSet(SortedSet<Integer> allDenomSet) {
	this.allDenomSet = allDenomSet;
}
public int getMin() {
	return min;
}
public void setMin(int min) {
	this.min = min;
}
public DenomNode getRoot() {
	return root;
}
  public void setRoot(DenomNode root) {
		this.root = root;
}

public int getMax() {
	return max;
}
public void setMax(int max) {
	this.max = max;
}

  


public DenomNode addDenom( int value){

   root =  addDenom(root,value);
   allDenomSet.add(value);
   return root;
  
}
private DenomNode addDenom(DenomNode root, int value){

 if(root == null){ 
  root = new DenomNode(value);
  return root;  
 }
 
 if(value < root.denomValue ){
   root.left = addDenom(root.left, value);   
   root.smallDenomSet.add(value);
   
 }else{
   root.right = addDenom(root.right, value);
   root.right.smallDenomSet.addAll(root.smallDenomSet);
   root.right.smallDenomSet.add(root.denomValue);
 }

 return root;

}


public void createDenomTree(int[] enumArray, int start, int end){
     //Base condition
     if(start > end) return;
       
     int mid = (start + end)/2;
     
    addDenom(enumArray[mid]);
    createDenomTree( enumArray, start, mid - 1);
    createDenomTree( enumArray, mid + 1, end);

  
 }

public static void main(String[] args){

  
}





}