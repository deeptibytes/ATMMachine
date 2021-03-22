# ATMMachine

To test ATM depost and withdrawl, please go to atm.main package and open ATMMain.java. In main method, invoke deposit withdrawls as below:

 ATMBusiness atm = new ATMBusiness();
 String message = null;
  
    /*
     * Test Deposit Feature
     */
  
    int[][] inputDepo = {{1,3}, {5,2}, {10, 11}, {20,1} }; //input is 2D array
    String message = atm.processDeposit("Deepti", inputDepo);
    System.out.println(message);
     
      /*
     * Test Withdrawl Feature
     */
  
    int inputWithdrawl = 20 ;
    String messageW = atm.processWithdrawl("USERID", inputWithdrawl); // input is integer
    System.out.println(messageW);
  
