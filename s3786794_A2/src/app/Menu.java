package app;

import java.io.*;
import java.util.*;
import exceptions.*;
import utilities.DateTime;
import messages.ErrorMessages;

/*
 * Class:		Menu
 * Description:	The class a menu and is used to interact with the user. 
 * Author:		Peter Bui(Originally Rodney Cocker) - s3786794
 */
public class Menu {
	private Scanner console = new Scanner(System.in);
	private MiRideApplication application = new MiRideApplication();
	private ErrorMessages errorMessages = new ErrorMessages();
	// Allows me to turn validation on/off for testing business logic in the
	// classes.
	// private boolean testingWithValidation = true;

	/*
	 * Runs the menu in a loop until the user decides to exit the system.
	 */
	public void run() throws InvalidId, InvalidRefreshments, InvalidBooking, InvalidDate, 
								NumberFormatException, InputMismatchException, FileNotFoundException, 
								IOException, CorruptedFiles, NullFile {
		final int MENU_ITEM_LENGTH = 2;
		String input;
		String choice = "";
		
		application.loadData();
		
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
					} catch (InvalidId id) {
						System.out.println(errorMessages.errorId());
						createCar();
					} catch (InvalidRefreshments ir) {
						System.out.println(errorMessages.errorRefreshments());
						createCar();
					} catch (InputMismatchException im) {
						System.out.println(errorMessages.errorMismatch());
						createCar();
					} catch (NumberFormatException nf) {
						System.out.println(errorMessages.errorNumeric());
						createCar();
					} catch (ArrayIndexOutOfBoundsException ai) {
						System.out.println("\nNot enough memory to create car.");
						System.out.println("Returning to menu.");
					}
					break;
				case "BC":
					try {
						book();
					} catch (InvalidBooking in) {
						System.out.println(errorMessages.errorBooking());
						book();
					} catch (InvalidDate id) {
						System.out.println(errorMessages.errorDate());
						book();
					} catch (ArrayIndexOutOfBoundsException ai) {
						System.out.println("\nCar is currently fully booked.");
						System.out.println("Returning to menu.");
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
					try {
						application.seedData();
					} catch (InvalidId id) {
						System.out.println("Data could not be seeded.\n");
					} catch (InvalidBooking ib) {
						System.out.println("Data could not be seeded.\n");
					} catch (InvalidRefreshments ir) {
						System.out.println("Data could not be seeded.\n");
					} catch (InvalidDate id) {
						System.out.println("Data could not be seeded.\n");
					} catch (InputMismatchException im) {
						System.out.println("Data could not be seeded.\n");
					}
					System.out.println("Data seeded.");
					break;
				case "EX":
					choice = "EX";
					try {
						application.saveCars();
					} catch (IOException io) {
						System.out.println("Cars could not be saved.");
					}
					System.out.println("Exiting Program ... Goodbye!");
					break;
				case "TE":
//					application.printToString("SD");
//					application.printCarList();
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
	private void createCar() throws InvalidId, InvalidRefreshments, InputMismatchException {
		String id = "", make, model, driverName, refreshments, carType = "";
		int numPassengers = 0;
		double bookingFee = 0;
		
		System.out.print("Enter registration number: ");
		id = console.nextLine().toUpperCase();
		
		if (id.length() != 0) {
			// Get details required for creating a car.
			System.out.print("Enter Make: ");
			make = console.nextLine();

			System.out.print("Enter Model: ");
			model = console.nextLine();

			System.out.print("Enter Driver Name: ");
			driverName = console.nextLine();

			System.out.print("Enter number of passengers: ");
			numPassengers = Integer.parseInt(console.nextLine());

			System.out.print("Enter service type(SD/SS): ");
			carType = console.nextLine().toUpperCase();
			
			if (carType.equals("SD")) {
				makeCar(carType, id, make, model, driverName, numPassengers, 0, null);
			}
			if (carType.equals("SS")) {
				System.out.print("Enter Standard Fee: ");
				bookingFee = Integer.parseInt(console.nextLine());
				
				System.out.print("Enter list of Refreshments(3 or More): ");
				refreshments = console.nextLine();
				
				makeCar(carType, id, make, model, driverName, numPassengers, bookingFee, refreshments);
			}
		}
	}

	/*
	 * Book a car by finding available cars for a specified date.
	 */
	private boolean book() throws InvalidBooking, InvalidDate {
		System.out.println("Booking date(dd/mm/yyyy): ");
		String dateEntered = console.nextLine();
		
		String[] dateCheckAvailable = dateEntered.split("/");
		int day = Integer.parseInt(dateCheckAvailable[0]);
		int month = Integer.parseInt(dateCheckAvailable[1]);
		int year = Integer.parseInt(dateCheckAvailable[2]);
		
		DateTime dateRequired = new DateTime(day, month, year);
		
		
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
	
	/*
	 * 
	 */
	private void makeCar(String carType, String id, String make, String model, String driverName, int numPassengers, double bookingFee, String refreshments) throws InvalidId, InputMismatchException, InvalidRefreshments {
		boolean result = application.checkIfCarExists(id);

		if (!result) {
			String carRegistrationNumber = application.createCar(carType, id, make, model, driverName, numPassengers, bookingFee, refreshments);
			System.out.println(carRegistrationNumber);
		} else {
			System.out.println("Error - Already exists in the system");
		}
	}
	
	/*
	 * 
	 */
	private void displayAvailable() {
		String type = "", dateInput = "";
		
		System.out.println("Enter Type(SD/SS): ");
		type = console.nextLine().toUpperCase();
		
		System.out.print("Enter Date: ");
		dateInput = console.nextLine();
		
		String[] dateCheckAvailable = dateInput.split("/");
		int day = Integer.parseInt(dateCheckAvailable[0]);
		int month = Integer.parseInt(dateCheckAvailable[1]);
		int year = Integer.parseInt(dateCheckAvailable[2]);
		
		DateTime dateRequired = new DateTime(day, month, year);
		
		System.out.println(application.displayAvailable(type, dateRequired));
	}
	
	/*
	 * 
	 */
	private void displayAllCars() {
		String type, order;
		
		System.out.print("Enter Type(SD/SS): ");
		type = console.nextLine().toUpperCase();
		
		System.out.print("\nEnter sort order(A/D): ");
		order = console.nextLine().toUpperCase();
		
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
