package atm.repository;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import atm.enums.Action;
import atm.enums.Denomination;

/*
 * Class serves analogous to balance DB table hsoting balance of each user. In real world, balance will be saved in DB
 */
public class UserBalanceRepository  {

 /*
 * This variable holds the balance for all the users. USERID  is key, balance is the value
 */
 private static Map<String, Integer> userBalanceTable = new HashMap<String, Integer>();
 private static UserBalanceRepository userBalanceRepo = null;
 
 
//Private constructor

    public UserBalanceRepository(){

	}
	public static UserBalanceRepository getInstance(){
	
	  if(userBalanceRepo == null){
	    userBalanceRepo = new UserBalanceRepository();
	  }
	  
	  return userBalanceRepo;
	}


 
 /*
  * Get current balance for the user
  */
	 public  int getBalance(String userId){
	   
	       return userBalanceTable.get(userId);
	     
	 
	 }

/*
  * update user balance after deposit/withdrawl
  */	 
 public void updateUserBalance (String userId, int newBalance){
 	  
	     userBalanceTable.put(userId, newBalance );
	 
 }

	 

 /*
  * Check if user has ever accessed the ATM. If not, balanceMap wont have this user.
  */
	  public boolean lookUpUser(String userId){
	
	     if(  userBalanceTable.containsKey(userId)){ 
	        return true;
	     }
	     return false;
	  }

 /*
  * If user accessing ATM first time, insert the record in balanceMap
  */
	public void createUserEntry(String userId){	  
	  
	  userBalanceTable.put(userId, 0);
	 
	 }
}
