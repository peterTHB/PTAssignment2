package app;

import java.util.Scanner;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * Author:		Peter Bui(Originally Rodney Cocker) - s3786794
 */
public class Menu {
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	private boolean testingWithValidation = true;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run() {
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		do {
			printMenu();

			input = console.nextLine().toUpperCase();

			if (input.length() != MENU_ITEM_LENGTH) {
				System.out.println("Error - selection must be two characters!");
			} else {
				System.out.println();

				switch (input) {
				case "CC":
					createCar();
					break;
				case "BC":
					book();
					break;
				case "CB":
					completeBooking();
					break;
				case "DA":
					System.out.println(application.displayAllBookings());
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SD":
					application.seedData();
					break;
				case "EX":
					choice = "EX";
					System.out.println("Exiting Program ... Goodbye!");
					break;
				case "TE":
					application.printToString();
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
					System.out.println("Need some answers?");
				}
			}

		} while (choice != "EX");
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar() {
		String id = "", make, model, driverName, carType = "", refreshments;
		int numPassengers = 0;
		double bookingFee = 0;

		System.out.print("Enter registration number: ");
		id = promptUserForRegNo();
		if (id.length() != 0) {
			// Get details required for creating a car.
			System.out.print("Enter Make: ");
			make = console.nextLine();

			System.out.print("Enter Model: ");
			model = console.nextLine();

			System.out.print("Enter Driver Name: ");
			driverName = console.nextLine();

			System.out.print("Enter number of passengers: ");
			numPassengers = promptForPassengerNumbers();
			
			System.out.print("Enter service type(SD/SS): ");
			carType = console.nextLine().toUpperCase();
			
			if (carType == "SD") {
				System.out.println(carType);
			} else if (carType == "SS") {
				System.out.println(carType);
			}
			
//			if (carType == "SD") {
//				makeStandard(id, make, model, driverName, numPassengers);
//			}
//			if (carType == "SS") {
//				System.out.println("Enter Standard Fee: ");
//				bookingFee = promptForBookingFee();
//				
//				System.out.println("Enter list of Refreshments: ");
//				refreshments = console.nextLine();
//				
//				makeSilver(id, make, model, driverName, numPassengers, bookingFee, refreshments);
//			}
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book() {
		System.out.println("Booking date(dd/mm/yyyy): ");
		String dateEntered = console.nextLine();
		String[] dateCheckAvailable = dateEntered.split("/");
		int day = Integer.parseInt(dateCheckAvailable[0]);
		int month = Integer.parseInt(dateCheckAvailable[1]);
		int year = Integer.parseInt(dateCheckAvailable[2]);
		
		DateTime dateRequired = new DateTime(day, month, year);
		
		if(!DateUtilities.dateIsNotInPast(dateRequired) || !DateUtilities.dateIsNotMoreThan7Days(dateRequired)) {
			System.out.println("Date is invalid, must be within the coming week.");
			return false;
		}
		
		String[] availableCars = application.book(dateRequired);
		for (int i = 0; i < availableCars.length; i++) {
			System.out.println(availableCars[i]);
		}
		if (availableCars.length != 0) {
			System.out.println("Please enter a number from the list:");
			int itemSelected = Integer.parseInt(console.nextLine());
			
			String regNo = availableCars[itemSelected - 1];
			regNo = regNo.substring(regNo.length() - 6);
			System.out.println("Please enter your first name:");
			String firstName = console.nextLine();
			System.out.println("Please enter your last name:");
			String lastName = console.nextLine();
			System.out.println("Please enter the number of passengers:");
			int numPassengers = Integer.parseInt(console.nextLine());
			String result = application.book(firstName, lastName, dateRequired, numPassengers, regNo);

			System.out.println(result);
		} else {
			System.out.println("There are no available cars on this date.");
		}
		return true;
	}
	
	/*
	 * Complete bookings found by either registration number or booking date.
	 */
	private void completeBooking() {
		System.out.print("Enter Registration or Booking Date:");
		String response = console.nextLine();
		
		String result;
		// User entered a booking date
		if (response.contains("/")) {
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			System.out.print("Enter kilometers:");
			double kilometers = Double.parseDouble(console.nextLine());
			
			String[] dateCheckAvailable = response.split("/");
			int day = Integer.parseInt(dateCheckAvailable[0]);
			int month = Integer.parseInt(dateCheckAvailable[1]);
			int year = Integer.parseInt(dateCheckAvailable[2]);
			
			DateTime dateOfBooking = new DateTime(day, month, year);
			result = application.completeBooking(firstName, lastName, dateOfBooking, kilometers);
			System.out.println(result);
		} else {
			System.out.print("Enter First Name:");
			String firstName = console.nextLine();
			System.out.print("Enter Last Name:");
			String lastName = console.nextLine();
			if (application.getBookingByName(firstName, lastName, response)) {
				System.out.print("Enter kilometers:");
				double kilometers = Double.parseDouble(console.nextLine());
				result = application.completeBooking(firstName, lastName, response, kilometers);
				System.out.println(result);
			} else {
				System.out.println("Error: Booking not found.");
			}
		}
		
	}
	
	private int promptForPassengerNumbers() {
		int numPassengers = 0;
		boolean validPassengerNumbers = false;
		// By pass user input validation.
		if (!testingWithValidation) {
			return Integer.parseInt(console.nextLine());
		} else {
			while (!validPassengerNumbers) {
				numPassengers = Integer.parseInt(console.nextLine());

				String validId = application.isValidPassengerCapacity(numPassengers);
				if (validId.contains("Error:")) {
					System.out.println(validId);
					System.out.println("Enter passenger capacity: ");
					System.out.println("(or hit ENTER to exit)");
				} else {
					validPassengerNumbers = true;
				}
			}
			return numPassengers;
		}
	}

	/*
	 * Prompt user for registration number and validate it is in the correct form.
	 * Boolean value for indicating test mode allows by passing validation to test
	 * program without user input validation.
	 */
	private String promptUserForRegNo() {
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation) {
			return console.nextLine();
		} else {
			while (!validRegistrationNumber) {
				regNo = console.nextLine().toUpperCase();
				boolean exists = application.checkIfCarExists(regNo);
				if(exists) {
					// Empty string means the menu will not try to process
					// the registration number
					System.out.println("Error: Reg Number already exists");
					return "";
				}
				if (regNo.length() == 0) {
					break;
				}

				String validId = application.isValidId(regNo);
				if (validId.contains("Error:")) {
					System.out.println(validId);
					System.out.println("Enter registration number: ");
					System.out.println("(or hit ENTER to exit)");
				} else {
					validRegistrationNumber = true;
				}
			}
			return regNo;
		}
	}
	
	/*
	 * Checks if the booking fee for a silver car is correct
	 */
	
	private double promptForBookingFee() {
		double bookingFee = 0;
		boolean validBookingFee = false;
		// By pass user input validation.
		if (!testingWithValidation) {
			return Double.parseDouble(console.nextLine());
		} else {
			while (!validBookingFee) {
				bookingFee = Double.parseDouble(console.nextLine());

				String validId = application.isValidBookingFee(bookingFee);
				if (validId.contains("Error:")) {
					System.out.println(validId);
					System.out.println("Enter passenger capacity: ");
					System.out.println("(or hit ENTER to exit)");
				} else {
					validBookingFee = true;
				}
			}
			return bookingFee;
		}
	}

	/*
	 * Prints the menu.
	 */
	private void printMenu()
	{
		System.out.printf("\n********** MiRide System Menu **********\n\n");

		System.out.printf("%-30s %s\n", "Create Car", "CC");
		System.out.printf("%-30s %s\n", "Book Car", "BC");
		System.out.printf("%-30s %s\n", "Complete Booking", "CB");
		System.out.printf("%-30s %s\n", "Display ALL Cars", "DA");
		System.out.printf("%-30s %s\n", "Search Specific Car", "SS");
		System.out.printf("%-30s %s\n", "Search Available Cars", "SA");
		System.out.printf("%-30s %s\n", "Seed Data", "SD");
		System.out.printf("%-30s %s\n", "Exit Program", "EX");
		// Testing purposes
		System.out.printf("%-30s %s\n", "Print out to strings of cars", "TE");
		System.out.println("\nEnter your selection: ");
		System.out.println("(Hit enter to cancel any operation)");
	}
	
	private void makeStandard(String id, String make, String model, String driverName, int numPassengers) {
		boolean result = application.checkIfCarExists(id);

		if (!result) {
			String carRegistrationNumber = application.createCar(id, make, model, driverName, numPassengers);
			System.out.println(carRegistrationNumber);
		} else {
			System.out.println("Error - Already exists in the system");
		}
	}
	
	private void makeSilver(String id, String make, String model, String driverName, int numPassengers, double bookingFee, String refreshments) {
		boolean result = application.checkIfCarExists(id);

		if (!result) {
			String carRegistrationNumber = application.createCarSilver(id, make, model, driverName, numPassengers, bookingFee, refreshments);
			System.out.println(carRegistrationNumber);
		} else {
			System.out.println("Error - Already exists in the system");
		}
	}
}
