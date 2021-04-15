package atm.util;

import java.util.HashMap;
import java.util.Map;

import atm.model.MessageWrapper;

public class ATMUtils {

public static final String WITHDRAWL_FAILED = "Withdrawl Failed >> Requested withdraw amount is not dispensable";
/*
 * Prepares message for successful withdrawl
 */
public static String prepareWithdrawlMessage(Map<Integer, Integer> denominationMap, Map<Integer, 
                                             Integer> dispensedMap, 
                                             int currBalance,
                                             int withdrawlAmount){
  
     String message = prepareDispenseMessage(dispensedMap, withdrawlAmount) 
     + prepareBalanceMessage(denominationMap, currBalance);
     
     
     return message;
    
  }
  
 /*
 * Prepares message for successful deposit
 */ 
  public static String prepareBalanceMessage(Map<Integer, Integer> denominationMap, int currBalance){
  
     StringBuilder sb = new StringBuilder();
    
     sb.append("Balance: ");
    
     denominationMap.entrySet().stream()
     .forEach(e -> {
       sb.append(   e.getKey()+"s="+e.getValue()+", "   );
     
     });
     
     sb.append("Total= ");
     sb.append(currBalance);
     sb.append("\n");
     
     return sb.toString();
    
  }
 /*
 * Prepares message for successful withdrawl
 */ 
  private static String prepareDispenseMessage(Map<Integer, Integer> dispensedMap, int withdrawlAmount){
  
     StringBuilder sb = new StringBuilder();
     sb.append("Withdrawl Success for " +withdrawlAmount  +" >> Dispensed: ");
    
     dispensedMap.entrySet().stream()
     .forEach(e -> {
       sb.append(   e.getKey()+"s="+e.getValue()+", "   );
     
     });
     
     sb.append("\n");
     return sb.toString();
    
  }
  
  
  
  
 /*
  * This method calculates total balance from any denomination map
  *  
  */
  public static final int getBalance(Map<Integer, Integer> denominationMap){
  
     int balance = 0;
     
     for(int denom : denominationMap.keySet()){    
       balance +=  denom * denominationMap.get(denom);
     }
   
	 return balance;
 }
 
  
  /*
 * This method validates the deposit input
 */
public static boolean validateDepositInput(Map<Integer, Integer> inputMap, MessageWrapper msg){
 
    if(   isAnyNegative(inputMap) ){
           msg.setMessage("Incorrect deposit amount"+"\n");
           return false;
    }
    
    if(   ! isAnyNonZero(inputMap) ){//
           msg.setMessage("Deposit amount cannot be zero"+"\n");
          return false;
    }
    
    return true;
 }
 // returns true if any of the denom has value less than zero
 private static boolean isAnyNegative(Map<Integer, Integer> inputMap){
  
   return inputMap.values().stream()     
      .anyMatch(value ->      
        value  <  0 
    );
     
 }
 // returns true if any of the denom has non zero value
 private static boolean isAnyNonZero(Map<Integer, Integer> inputMap){
      
     return inputMap.values().stream()     
      .anyMatch(value ->      
        value  !=  0 
    );
     
 }
 /*
  * This method validates if the input amount > user current balance in DB
  */
 public static boolean isInputOverCurrent(int inputAmt, int curreBalance){
 
   return inputAmt > curreBalance;
 
 }
 
 /*
  * This method validates if the input amount for withdrawl is either 0 or less than zero.
  */
 public static boolean validateWithdrawlInput(int inputAmt, MessageWrapper msg){
 

 if( inputAmt == 0 ||
     inputAmt < 0  ){
       msg.setMessage("Incorrect or insufficient funds for "+inputAmt + "\n");
     return false;
 }
   return true;  
 
 }
 
 
 public static Map<Integer, Integer>  createDispenseMap(int amount){
   
   Map<Integer, Integer> inputMap = new HashMap<Integer, Integer>();
   
   return inputMap;
 
 }
 
 public static boolean isDenoAvailable(Map<Integer, Integer> currBalanceMap, Map<Integer, Integer> inputMap){
 
 
 return true;
 
 }
 
 /*
  *It creates map from user input (2D array)
  */
 public static Map<Integer, Integer> createDepositMap( int[][] input){
 
   Map<Integer, Integer> inputMap = new HashMap<Integer, Integer>();
     for(int[] denomPair : input){     
      inputMap.put(denomPair[0], denomPair[1]);   
   } 
  return inputMap;
 }
 
 

    
  

}
