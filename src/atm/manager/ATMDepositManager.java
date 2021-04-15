package atm.manager;

import java.util.Map;

import atm.enums.Action;

import atm.repository.DenominationRepo;

import atm.repository.UserBalanceRepository;
import atm.util.ATMUtils;

public class ATMDepositManager implements IATMManager{


 
  private static UserBalanceRepository userRepo = UserBalanceRepository.getInstance() ;
  private static DenominationRepo denomRepo = DenominationRepo.getInstance();
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
	     int currUserBalance;
	     int depositAmount;
	     int newUserBalance;
	  
	  //If user accessing ATM machine for the first time, create entry in DB
	     if( ! userRepo.lookUpUser(userId)){
		      userRepo.createUserEntry(userId);
		   }
		   
	   
	//Get current balance from DB
	   currUserBalance = userRepo.getBalance(userId);
	  
	    
	 //Get deposit amount from user in ATM machine. It will be calculated from diff denoms user deposited
		Map<Integer, Integer> inputMap = (Map<Integer, Integer>)input;
	    depositAmount = ATMUtils.getBalance(inputMap);
	   
	   
	 //Calculate new balance
	   newUserBalance = currUserBalance + depositAmount;
	   
	    //Update new User balance in DB 
	   userRepo.updateUserBalance(userId, newUserBalance);
	   
	    //Update new denom balance in denom DB 
	   denomRepo.updateDenomRepo(inputMap, Action.DEPOSIT.name());
	   
	 //print success message 
	   return  "Deposit succes for "+inputMap + "\n"
        + ATMUtils.prepareBalanceMessage(DenominationRepo.denomTable, newUserBalance);
  
  
  }
  

}
