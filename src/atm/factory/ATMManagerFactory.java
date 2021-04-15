package atm.factory;

import atm.enums.Action;
import atm.manager.ATMDepositManager;
import atm.manager.ATMWithdrawlManager;
import atm.manager.IATMManager;

public class ATMManagerFactory {



  public static IATMManager manager = null; 
  
  public static IATMManager getATMManager( String action){
  
  
     if(action.equalsIgnoreCase(Action.DEPOSIT.name())){
     
       manager = ATMDepositManager.getInstance();
     
     }else if(action.equalsIgnoreCase(Action.WITHDRAWL.name())){
     
        manager = ATMWithdrawlManager.getInstance();
     
     }else{
     
     
     }
  
     return manager;
  }

}
