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


  public  String createDispenseMap(Map<Integer, Integer> currBalanceMap, 
								  Map<Integer, Integer> dispenseMap, 
								  int amount){
   
   DenomNode closestDenom = getClosestDenom(amount, Denomination.DENOM_TREE.getRoot());
   SortedSet<Integer> smallDenomSet = (amount >= Denomination.DENOM_TREE.getMax()) ?
                                      Denomination.DENOM_TREE.getAllDenomSet() : 
                                      closestDenom.smallDenomSet;
                                      System.out.println("smallDenomSet "+smallDenomSet);
  
  for(int denom : smallDenomSet){
    
    //Base Condition: if amount to be withdrawn is 10 but denom is 50. Invalid denom, pick lower denom
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
      
       int reqBills = amount/denom;
       int valueDB = currBalanceMap.get(denom);
       int dispenseValue = (valueDB >= reqBills) ? reqBills : valueDB;
       
        dispenseMap.put(denom, dispenseValue ); 
        amount = amount - denom * dispenseValue;
        
        if(amount == 0)//amount = 0 means transaction complete
		    return null; 
        else
           continue;        
   }
    
   return (amount != 0) ? ATMUtils.WITHDRAWL_FAILED : null;
 }
 
 
 private DenomNode getClosestDenom(int amount, DenomNode root){
   
   //Base Cases - start
   if(root == null) return null;
   
   if(amount == root.denomValue){
       return root.right;
   
   }else if(root.left != null && 
            amount < root.denomValue && amount > root.left.denomValue){
     return root.left;
   
   } else if(root.right != null && 
             amount > root.denomValue && amount < root.right.denomValue){
  
     return root.right;
   }
   //Base Cases - end
   
   
   if(amount < root.denomValue){
     return getClosestDenom(amount, root.left);
   }  
     
    return getClosestDenom(amount, root.right);
   
 
}
  
  
  
 
   
 
}
