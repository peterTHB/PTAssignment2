package app;

import java.util.*;
import exceptions.*;
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
	private ManageExceptions manageExcep = new ManageExceptions();
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
					try {
						createCar();
					} catch (NumberFormatException nf) {
						System.out.println("\nNo numbers inputted.");
					} catch (InvalidRefreshments ir) {
						System.out.println("\nInvalid refreshments list.");
					} catch (InvalidId id) {
						// TODO Auto-generated catch block
						System.out.println("\nInvalid registration number input.");
					}
					break;
				case "BC":
					try {
						book();
					} catch (NoSuchElementException ns) {
						System.out.println("No elements detected from input.");
					} catch (InvalidDate iv) {
						System.out.println("Invalid date input.");
					}
					break;
				case "CB":
					completeBooking();
					break;
				case "DA":
					displayAllCars();
					break;
				case "SS":
					System.out.print("Enter Registration Number: ");
					System.out.println(application.displaySpecificCar(console.nextLine()));
					break;
				case "SA":
					displayAvailable();
					break;
				case "SD":
					application.seedData();
					break;
				case "EX":
					choice = "EX";
					System.out.println("Exiting Program ... Goodbye!");
					break;
				default:
					System.out.println("Error, invalid option selected!");
					System.out.println("Please try Again...");
				}
			}

		} while (choice != "EX");
	}

	/*
	 * Creates cars for use in the system available or booking.
	 */
	private void createCar() throws NumberFormatException, InvalidRefreshments, InvalidId {
		String id = "", make, model, driverName, refreshments;
		int carType;
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
			
			// String input was not registering, so int is used instead
			System.out.println("Enter service type:");
			System.out.print("(1 for SD/2 for SS): " );
			carType = Integer.parseInt(console.nextLine());
			
			if (carType == 1) {
				makeStandard(id, make, model, driverName, numPassengers);
			}
			if (carType == 2) {
				System.out.print("Enter Standard Fee: ");
				bookingFee = promptForBookingFee();
				
				System.out.print("Enter list of Refreshments: ");
				refreshments = console.nextLine();
				
				manageExcep.refreshDupCheck(refreshments);
				manageExcep.refreshLimitCheck(refreshments);
				
				makeSilver(id, make, model, driverName, numPassengers, bookingFee, refreshments);
			}
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book() throws NoSuchElementException, InvalidDate {
		System.out.println("Booking date(dd/mm/yyyy): ");
		String dateEntered = console.nextLine();
		
		manageExcep.dateCheck(dateEntered);
		
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
		String response = console.nextLine().toUpperCase();
		
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
	private String promptUserForRegNo() throws InvalidId {
		String regNo = "";
		boolean validRegistrationNumber = false;
		// By pass user input validation.
		if (!testingWithValidation) {
			return console.nextLine();
		} else {
			while (!validRegistrationNumber) {
				regNo = console.nextLine().toUpperCase();
				manageExcep.regNoCheck(regNo);
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
	
	private void displayAvailable() {
		int type = 0;
		String dateInput = "";
		
		// String input is not registering, so ints used as a substitute
		System.out.println("Enter Type:");
		System.out.print("(1 for SD/2 for SS): ");
		type = Integer.parseInt(console.nextLine());
		
		System.out.print("Enter Date: ");
		dateInput = console.nextLine();
		
		String[] dateCheckAvailable = dateInput.split("/");
		int day = Integer.parseInt(dateCheckAvailable[0]);
		int month = Integer.parseInt(dateCheckAvailable[1]);
		int year = Integer.parseInt(dateCheckAvailable[2]);
		
		DateTime dateRequired = new DateTime(day, month, year);
		
		System.out.println(application.displayAvailable(type, dateRequired));
	}
	
	private void displayAllCars() {
		int type = 0; 
		int order = 0;
		
		// String input is not registering, so ints used as a substitute
		System.out.println("Enter Type:");
		System.out.print("(1 for SD, 2 for SS): ");
		type = Integer.parseInt(console.nextLine());
		
		System.out.println("\nEnter sort order: ");
		System.out.print("(1 for A, 2 for D): ");
		order = Integer.parseInt(console.nextLine());
		
		System.out.println(application.displayAllBookings(type, order));
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
		System.out.println("\nEnter your selection: ");
		System.out.println("(Hit enter to cancel any operation)");
	}
}
