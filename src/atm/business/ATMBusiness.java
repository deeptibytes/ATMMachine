package atm.business;

import java.util.Map;

import atm.enums.Action;
import atm.factory.ATMManagerFactory;
import atm.model.MessageWrapper;
import atm.util.ATMUtils;

public class ATMBusiness {


public ATMBusiness(){

}


/*
 * Process deposit activity from user
 */
public String processDeposit(String userId, int[][] input){

    MessageWrapper msg = new MessageWrapper(); 
    //Create input map from 2D array user input for deposit   
    Map<Integer, Integer> inputMap = ATMUtils.createDepositMap(input); 
    
   if(ATMUtils.validateDepositInput(inputMap, msg)){
    
      return ATMManagerFactory.getATMManager(Action.DEPOSIT.name()).updateBalance(userId, inputMap);
   }else{
      return msg.getMessage();
   }

}

/*
 * Process withdrawl activity from user
 */
public String processWithdrawl(String userId, Integer amount){

    MessageWrapper msg = new MessageWrapper();
    
     if(ATMUtils.validateWithdrawlInput(amount, msg)){   
     
      return ATMManagerFactory.getATMManager(Action.WITHDRAWL.name()).updateBalance(userId, amount);
   }else{
       return msg.getMessage();
   }
   
}

}

 
 
 
