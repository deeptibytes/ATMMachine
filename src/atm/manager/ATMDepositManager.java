package atm.manager;

import java.util.Map;

import atm.repository.BalanceRepository;
import atm.util.ATMUtils;

public class ATMDepositManager implements IATMManager{


 
  private static BalanceRepository balanceRepo = BalanceRepository.getInstance();;  
  private static IATMManager atmManager = null;
 
 //private constructor
    private ATMDepositManager(){
       
    }

	public static IATMManager getInstance(){
	
	  if(atmManager == null){
	    atmManager = new ATMDepositManager();
	  }
	  
	  return atmManager;
	}
	
 @Override	
 public String updateBalance(String userId, Object input){
 
	  //Declare variables
	     int currBalance;
	     int depositAmount;
	  
	  //If user accessing ATM machine for the first time, create entry in DB
	     if( ! balanceRepo.lookUpUser(userId)){
		      balanceRepo.createUserEntry(userId);
		   }
		   
	   
	//Get current balance from DB
	    Map<Integer, Integer> currBalanceMap = balanceRepo.getBalance(userId);
	    currBalance = ATMUtils.getBalance(currBalanceMap);;
	  
	    
	 //Get deposit amount from user in ATM machine
		Map<Integer, Integer> inputMap = (Map<Integer, Integer>)input;
	    depositAmount = ATMUtils.getBalance(inputMap);
	    
	 //Update database to complete the deposit process  
	   balanceRepo.updateForDeposit(currBalanceMap, inputMap);
	   
	 //Calculate new balance
	   currBalance = currBalance + depositAmount;
	   
	 //print success message 
	   return  "Deposit succes for "+inputMap + "\n"
        + ATMUtils.prepareBalanceMessage(currBalanceMap, currBalance);
  
  
  }
  

}
