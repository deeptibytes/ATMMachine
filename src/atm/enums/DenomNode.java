package atm.enums;


import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;

public class DenomNode{

	 public int denomValue;
	 public SortedSet<Integer> smallDenomSet = new TreeSet<Integer>(Comparator.reverseOrder());
	 public  DenomNode left;
	 public DenomNode right;
 
	 public DenomNode(int denomValue){
	   this.denomValue = denomValue;
	 
	 }

}