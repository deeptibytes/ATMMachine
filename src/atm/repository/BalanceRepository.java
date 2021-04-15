package atm.repository;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import atm.enums.Action;
import atm.enums.Denomination;

/*
 * Class serves analogous to balance DB table. In real world, balance will be saved in DB
 */
public class BalanceRepository {

 /*
 * This variable holds the balance for all the users. USERID  is key, denominationMap is value
 */
 private static Map<String, Map<Integer, Integer>> balanceTable = new HashMap<String, Map<Integer, Integer>>();
 private static BalanceRepository balanceRepo = null;
 
 
//Private constructor

    public BalanceRepository(){

	}
	public static BalanceRepository getInstance(){
	
	  if(balanceRepo == null){
	    balanceRepo = new BalanceRepository();
	  }
	  
	  return balanceRepo;
	}


 
 /*
  * Get current balance for the user
  */
	 public  Map<Integer, Integer> getBalance(String userId){
	   
	       return balanceTable.get(userId);
	     
	 
	 }
	 
 

	 
	 
	 /*
  * Update BalanceMap in case of withdrawl
  */
	public void updateForWithdrawl(Map<Integer, Integer> currBalance, Map<Integer, Integer> withdrawlMap){
	 
	    for(Integer denom : withdrawlMap.keySet()){
	    
	     currBalance.put(denom, currBalance.get(denom) - withdrawlMap.get(denom));
	  
	    }
	 
	 
	 }
	 
	  /*
  * Update BalanceMap in case of deposit
  */
	public void updateForDeposit(Map<Integer, Integer> currBalance, Map<Integer, Integer> depositMap){
	 
	    for(Integer denom : depositMap.keySet()){
	    
	     currBalance.put(denom, currBalance.getOrDefault(denom, 0) + depositMap.get(denom));
	  
	    }
	 
	 
	 }
	 


 /*
  * Check if user has ever accessed the ATM. If not, balanceMap wont have this user.
  */
	  public boolean lookUpUser(String userId){
	
	     if(  balanceTable.containsKey(userId)){ 
	        return true;
	     }
	     return false;
	  }

 /*
  * If user accessing ATM first time, insert the record in balanceMap
  */
	public void createUserEntry(String userId){
	  
	  Map<Integer, Integer> balanceMap = new HashMap<Integer, Integer>();
	  
	  
	  EnumSet.allOf(Denomination.class)
	            .forEach(denomination -> balanceMap.put(denomination.getValue(), 0));
	  
	  
	  balanceTable.put(userId, balanceMap);
	 
	 }
}
