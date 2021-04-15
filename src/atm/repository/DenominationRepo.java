package atm.repository;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import atm.enums.Action;
import atm.enums.Denomination;

/*
 * Class serves analogous to balance DB table hosting total number of denoms available at ATM machine. In real world, balance will be saved in DB
 */
public class DenominationRepo {


/*
 * This variable holds the available numbers for each denom in ATM machine
 */
 public static  Map<Integer, Integer> denomTable = null;
 
 private static DenominationRepo denominationRepo = null;

//To initialize denomTable with all denoms as zero. In case of user activity, this static var will be updated
 static
 {
    denomTable = new HashMap<Integer, Integer>();
    
      EnumSet.allOf(Denomination.class)
	            .forEach(denomination -> denomTable.put(denomination.getValue(), 0));
	  

 }
 
 
//Private constructor

    public DenominationRepo(){

	}
	public static DenominationRepo getInstance(){
	
	  if(denominationRepo == null){
	    denominationRepo = new DenominationRepo();
	  }
	  
	  return denominationRepo;
	}

   public void  updateDenomRepo(Map<Integer, Integer> inputMap, String action){

	    if(action.equalsIgnoreCase(Action.DEPOSIT.name())){
	      
	       updateForDeposit(inputMap);
	    
	    }else if( action.equalsIgnoreCase(Action.WITHDRAWL.name())){
	    
	       updateForWithdrawl(inputMap);
	    
	    }


  }
 	 
	 /*
  * Update denomRepo in case of withdrawl
  */
	private void updateForWithdrawl( Map<Integer, Integer> withdrawlMap){
	 
	    for(Integer denom : withdrawlMap.keySet()){
	    
	    	 synchronized(DenominationRepo.class){
	    	    denomTable.put(denom, denomTable.get(denom) - withdrawlMap.get(denom));
	    	 }   
	   
	  
	    }
	 
	 
	 }
	 
	  /*
  * Update denomRepo in case of deposit
  */
	private void updateForDeposit( Map<Integer, Integer> depositMap){
	 
	    for(Integer denom : depositMap.keySet()){
	    
	     synchronized(DenominationRepo.class){
	    	     denomTable.put(denom, denomTable.getOrDefault(denom, 0) + depositMap.get(denom));
	    	 }   
	   
	    
	  
	    }
	 
	 
	 }
	 


}
