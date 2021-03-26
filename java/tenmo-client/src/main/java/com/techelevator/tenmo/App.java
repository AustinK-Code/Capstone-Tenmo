package com.techelevator.tenmo;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.TransferDTO;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferMoneyService;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService = new AccountService(API_BASE_URL);
    private TransferMoneyService transferServices = new TransferMoneyService(API_BASE_URL);
	private TransferDTO[] transferArray;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
				searchByTransaction();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			}
			else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		BigDecimal balance = accountService.getBalance(currentUser.getToken());
		System.out.println("Your current account balance is: " + balance);
		
	}

	private void viewTransferHistory() {

		transferArray = transferServices.getTransferHistory(currentUser.getToken());
		System.out.println("Transfers\n------------------------\n");
		for (TransferDTO o : transferArray) {
			System.out.println("Transfer Id:"+ o.getTransferId() +
					"    Account From:" + convertIdToUserName( o.getAccountFrom()) +
					"    Account To:" + convertIdToUserName(o.getAccountTo()) +
					"    Amount Sent:$" + o.getAmount().toString());
		}
	}
	private void searchByTransaction(){
    	System.out.println("\nPlease enter the transfer ID to view details(0 to cancel): ");
    	int choice = console.getUserInputTransactionInt();
    	System.out.println("Transfer Details \n-----------------------------\n");
    	for (TransferDTO o : transferArray){
    		if(choice == o.getTransferId()){
				System.out.println("Transfer Id: "+ o.getTransferId() +
						"\nTransfer Type: " +convertTransferTypeIdToWords(o.getTransferTypeId()) +
						"\nTransfer Status: " + convertStatusIdToWords(o.getTransferStatusId()) +
						"\nAccount From: " + convertIdToUserName( o.getAccountFrom()) +
						"\nAccount To: " + convertIdToUserName(o.getAccountTo()) +
						"\nAmount Sent: $" + o.getAmount().toString());
				searchByTransaction();
			}
		}
    	if (choice == 0){
    		mainMenu();
		}

	}
	public String convertIdToUserName(int num){
    	User[] users  = transferServices.listUsers(currentUser.getToken());
    	for(User user : users){
    		user.getAccountId();
    		if(user.getAccountId() == num){
    			return user.getUsername();
			}
		}
    	return null;
    }
    public String convertStatusIdToWords(int num){
    	if (num == 1){
    		return "Pending"; }
		if (num == 2){
			return "Approve"; }
		if (num == 3){
			return "Rejected"; }
		return null;
	}
	public String convertTransferTypeIdToWords(int num){
		if (num == 1){
			return "Request";}
		if (num == 2){
			return "Send";}
		return null;
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		User[] users = transferServices.listUsers(currentUser.getToken());
		List userIDs = new ArrayList();
		for (User user: users){
			userIDs.add(user.getId());
			System.out.println("Username: "+user.getUsername() +
					" User ID: "+user.getId()+"\n-----------------");
		}
		System.out.println("Enter User ID of the user you are sending to (0 to cancel):");
		int idChoice = console.getUserInputTransactionInt();
		System.out.println("Enter amount: ");
		int amount = console.getUserInputTransactionInt();
		BigDecimal amount1 = BigDecimal.valueOf(amount);
		TransferDTO amountDTO = new TransferDTO();
		amountDTO.setAmount(amount1);
		if(userIDs.contains(idChoice) && idChoice > 0){
			transferServices.sendBucks(currentUser.getToken(),idChoice,amountDTO);
		}else mainMenu();

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
