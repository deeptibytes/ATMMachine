package atm.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import atm.enums.DenomNode;
import atm.enums.Denomination;
import atm.model.MessageWrapper;
import atm.repository.BalanceRepository;
import atm.util.ATMUtils;

public class ATMWithdrawlManager implements IATMManager{



 /*
  * Declare class variables
  */  
  private static BalanceRepository balanceRepo = BalanceRepository.getInstance();;
  private static IATMManager atmManager = null;
 
 //private constructor
   private ATMWithdrawlManager(){
       
    }

   public static IATMManager getInstance(){
	
	  if(atmManager == null){
	    atmManager = new ATMWithdrawlManager();
	  }
	  
	  return atmManager;
	}
	
  @Override
  public String updateBalance(String userId, Object input){
       
     //Declare variables
       boolean isFirst = false;
	   String message = null;
	   Map<Integer, Integer> dispenseMap;
	   int currBalance;
	   int withdrawlAmount;
	   String dispenseErrMsg = null;
	 
	 //If user accessing ATM machine for the first time, create entry in DB 
	  if( ! balanceRepo.lookUpUser(userId)){
	        balanceRepo.createUserEntry(userId);
	        isFirst = true;
	   }
	   
	//Get current balance from DB
	    Map<Integer, Integer> currBalanceMap = balanceRepo.getBalance(userId);
	    currBalance = ATMUtils.getBalance(currBalanceMap);
	    
	//Get withdrawl amount from user in ATM machine
	    withdrawlAmount = (int)input;

    
      //Validations - start  
		 if(  isFirst || ATMUtils.isInputOverCurrent(withdrawlAmount, currBalance)){
			 
		       message =  "Incorrect or insufficient funds";
		       return message;
		 }
		 
		
		  dispenseMap = new HashMap<Integer,Integer>();
		  dispenseErrMsg = createDispenseMap(currBalanceMap,  dispenseMap, withdrawlAmount);
		  
		  if(dispenseErrMsg != null){
		    return  dispenseErrMsg + " "+withdrawlAmount+ "\n" +"DB Balance after failed withdrawl" +"\n" 
		    +ATMUtils.prepareBalanceMessage(currBalanceMap,currBalance);
		     
		  }
		    
	  //Validations - end    
	   
	 //Validation successful, now update database to complete the withdrawl process  
	   balanceRepo.updateForWithdrawl(currBalanceMap, dispenseMap);
	   
	 //Calculate new balance
	   currBalance = currBalance - withdrawlAmount;
	   
	 //print success message
	   return ATMUtils.prepareWithdrawlMessage(currBalanceMap, dispenseMap, currBalance, withdrawlAmount);
	         
	 
	 
}
/*This method with check where the withdrawl amount falls on the DENOM_TREE. 
 * Then get the closest node in the DENOM_TREE
 * Retrieve the sorted list (reverse sorted) of smaller denoms from the closest denom node
 * Loop through the small denoms and if the required number of denominations is available in the repo
 *
 */

  public  String createDispenseMap(Map<Integer, Integer> currBalanceMap, 
								  Map<Integer, Integer> dispenseMap, 
								  int amount){
   
  
   //If amount is higher than higheset denom, no need of binary search, just retrieve the allDenomSet from DENOM_TREE object
   //If amount is less than highest, then get closeset node and retrieve list of smaller denoms than node.
   
   SortedSet<Integer> smallDenomSet = (amount >= Denomination.DENOM_TREE.getMax()) ?
                                      Denomination.DENOM_TREE.getAllDenomSet() : 
                                      getClosestDenom(amount, Denomination.DENOM_TREE.getRoot()).smallDenomSet;
                                      System.out.println("smallDenomSet "+smallDenomSet);
  //Loop through the all the smaller denoms
  for(int denom : smallDenomSet){
    
    //Base Condition: if amount to be withdrawn is 10 but current denom is 50. Invalid denom, pick lower denom
      if(amount < denom){
        continue;
      }
      
      //Repo does not contain this denom, check next lower denom and so on
      if( currBalanceMap.get(denom) == 0) {
        continue;
      }
      //Base condition :: this is for amount less than 5
      if(denom == Denomination.DENOM_TREE.getMin() && amount > denom * currBalanceMap.get(denom)){
         return ATMUtils.WITHDRAWL_FAILED;
      }
      
       int reqBills = amount/denom;//calculate how many bills are required for the amount
       int valueDB = currBalanceMap.get(denom);//check how many available in DB
       int dispenseValue = (valueDB >= reqBills) ? reqBills : valueDB;
       
        dispenseMap.put(denom, dispenseValue ); 
        amount = amount - denom * dispenseValue; //check how much amount is left
        
        if(amount == 0)//amount = 0 means transaction complete
		    return null; 
        else
           continue;        
   }
    
   return (amount != 0) ? ATMUtils.WITHDRAWL_FAILED : null;
 }
 
 /*
  * This method with check where the withdrawl amount falls on the DENOM_TREE.. Modification of binary search
  */
 private DenomNode getClosestDenom(int amount, DenomNode root){
   
   //Base Cases - start
   if(root == null) return null;

//if amount is equal to one of the denoms. For example, of amount is 20, we take closest node as 50.Node 50 has small denom list {20, 20, 5, 1)
//so for amount 20, the list of thse denoms {20, 20, 5, 1) will be considered   
   if(amount == root.denomValue){  
       return root.right;

//If amount is less than root and between 10 and 5 (eg..7), closest node will be 10. So for amount 7, the list of these denoms {5, 1) will be considered   
   }else if(root.left != null && 
            amount < root.denomValue && amount > root.left.denomValue){
     return root.left;
//If amount is more than root and between 10 and 20 (eg..15), closest node will be 20. So for amount 15, the list of these denoms {10, 5, 1) will be considered      
   } else if(root.right != null && 
             amount > root.denomValue && amount < root.right.denomValue){
  
     return root.right;
   }
   //Base Cases - end
   
   //Recursive call
   if(amount < root.denomValue){
     return getClosestDenom(amount, root.left);
   }  
     
    return getClosestDenom(amount, root.right);
   
 
}
  
  
  
 
   
 
}
