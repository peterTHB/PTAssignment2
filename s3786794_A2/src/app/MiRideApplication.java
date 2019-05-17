package app;

import java.util.InputMismatchException;

import cars.Car;
import cars.SilverServiceCar;
import exceptions.*;
import utilities.DateTime;
import utilities.MiRidesUtilities;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Peter Bui(Originally Rodney Cocker) - s3786794
 */

public class MiRideApplication {
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;
	Car[] sortCars;
	
	public String createCar(String carType, String id, String make, String model, String driverName, 
							int numPassengers, double bookingFee, String refreshments) 
							throws InvalidId, InvalidRefreshments, InputMismatchException {
		if(!checkIfCarExists(id)) {
			if (carType.equals("SD")) {
				cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			} else if (carType.equals("SS")) {
				String[] refreshList = refreshments.split(",");
				cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, bookingFee, refreshList);
			}
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount-1].getRegNo();
		}
		return "Error: Already exists in the system.";
	}

	public String[] book(DateTime dateRequired) {
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for(int i = 0; i < cars.length; i++) {
			if(cars[i] != null) {
				if(!cars[i].isCarBookedOnDate(dateRequired)) {
					numberOfAvailableCars++;
				}
			}
		}
		if(numberOfAvailableCars == 0) {
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for(int i=0; i<cars.length;i++) {
			if(cars[i] != null) {
				if(!cars[i].isCarBookedOnDate(dateRequired)) {
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegNo();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}
	
	public String book(String firstName, String lastName, DateTime required, int numPassengers, String registrationNumber) throws InvalidBooking, InvalidDate
	{
		Car car = getCarById(registrationNumber);
		if(car != null)  {
			if(car.book(firstName, lastName, required, numPassengers)) {

				String message = "Thank you for your booking. \n" + car.getDriverName() 
		        + " will pick you up on " + required.getFormattedDate() + ". \n"
				+ "Your booking reference is: " + car.getBookingID(firstName, lastName, required);
				return message;
			} else {
				String message = "Booking could not be completed.";
				return message;
			}
        } else {
            return "Car with registration number: " + registrationNumber + " was not found.";
        }
	}
	
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers) {
		// Search all cars for bookings on a particular date.
		for(int i = 0; i <cars.length; i++) {
			if (cars[i] != null) {
				if(cars[i].isCarBookedOnDate(dateOfBooking)) {
					return cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				}
			}
		}
		return "Booking not found.";
	}
	
	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers) {
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegNo().equals(registrationNumber)) {
					car = cars[i];
					break;
				}
			}
		}

		if (car == null) {
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1) {
			return car.completeBooking(firstName, lastName, null, kilometers);
		}
		return "Error: Booking not found.";
	}
	
	public boolean getBookingByName(String firstName, String lastName, String registrationNumber) {
		//String bookingNotFound = "Error: Booking not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++) {
			if (cars[i] != null) {
				if (cars[i].getRegNo().equals(registrationNumber)) {
					car = cars[i];
					break;
				}
			}
		}
		
		if(car == null) {
			return false;
		}
		if(car.getBookingByName(firstName, lastName) == -1) {
			return false;
		}
		return true;
	}
	
	public String displaySpecificCar(String regNo) {
		for(int i = 0; i < cars.length; i++) {
			if(cars[i] != null) {
				if(cars[i].getRegNo().equals(regNo.toUpperCase())) {
					return cars[i].getDetails();
				}
			}
		}
		return "Error: The car could not be located.";
	}
	
	/*
	 * Displays which cars are available
	 * Checks for type of car
	 * 		Checks for availability
	 * 			If yes, returns getDetails
	 * 			If not, don't do anything
	 * 		If no available cars, return error message
	 * Wrong input returns error message
	 */
	public String displayAvailable(String type, DateTime dateInput) {
		if (type.equals("SD")) {
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					if (cars[i].getCarType() == "SD") {
						if (!cars[i].isCarBookedOnDate(dateInput)) {
							return cars[i].getDetails();
						}
					}
				}
			}
			return "Error - No cars found on this date";
		} else if (type.equals("SS")) {
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					if (cars[i].getCarType() == "SS") {
						if (!cars[i].isCarBookedOnDate(dateInput)) {
							return cars[i].getDetails();
						}
					}
				}
			}
			return "Error - No cars found on this date";
		} else {
			return "Error - invalid input";
		}
	}
	
	public boolean seedData() throws InvalidId, InvalidBooking, InvalidRefreshments, InvalidDate {
		for(int i = 0; i < cars.length; i++) {
			if(cars[i] != null) {
				return false;
			}
		}
		// Seed both regular and silver cars individually
		seedRegular();
		seedSilver();
		
		return true;
	}
	
	public void seedRegular() throws InvalidId, InvalidBooking, InvalidDate {
		// 2 cars not booked
		Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
		cars[itemCount] = honda;
		itemCount++;
		
		Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
		cars[itemCount] = lexus;
		itemCount++;
		
		// 2 cars booked
		Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
		cars[itemCount] = bmw;
		itemCount++;
		bmw.book("Craig", "Cocker", new DateTime(1), 3);
		
		Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
		cars[itemCount] = audi;
		itemCount++;
		audi.book("Rodney", "Cocker", new DateTime(3), 4);
		
		// 3 bookings, 1 booking complete
		Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
		cars[itemCount] = toyota;
		itemCount++;
		toyota.book("Rodney", "Cocker", new DateTime(1), 3);
		toyota.book("Craig", "Cocker", new DateTime(2), 7);
		toyota.book("Alan", "Smith", new DateTime(3), 4);
		
		DateTime inSixDays = new DateTime(6);
		toyota.book("John", "Page", inSixDays, 5);
		toyota.completeBooking("John", "Page", inSixDays, 50);
		
		// 1 booking, 1 booking completed
		Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
		cars[itemCount] = rover;
		itemCount++;
		rover.book("Shawn", "Penn", new DateTime(1), 3);
		DateTime inTwoDays = new DateTime(2);
		rover.book("Rodney", "Cocker", inTwoDays, 3);
		rover.completeBooking("Rodney", "Cocker", inTwoDays, 75);
	}
	
	public void seedSilver() throws InvalidId, InvalidBooking, InvalidRefreshments, InvalidDate{
		// 2 silver cars not booked
		String[] refreshment1 = "Cadbury,Lays,Toothpaste".split(",");
		Car mazda = new SilverServiceCar("MAZ385", "Mazda", "CX-9", "Jason Voorhees", 4, 3.5, refreshment1);
		cars[itemCount] = mazda;
		itemCount++;

		String[] refreshment2 = "Pepsi,7Up,Lollipop".split(",");
		Car chrysler = new SilverServiceCar("CHR945", "Chrysler", "300 SRT", "Royal Chaos", 5, 3.8, refreshment2);
		cars[itemCount] = chrysler;
		itemCount++;
		
		// 1 booking
		String[] refreshment3 = "Coke,Scheweppes,Chupa Chups".split(",");
		Car mitsubishi = new SilverServiceCar("MIT345", "Mitsubishi", "Triton", "Pat Nguyen", 5, 4.6, refreshment3);
		cars[itemCount] = mitsubishi;
		itemCount++;
		mitsubishi.book("Mike", "Wazowski", new DateTime(2), 4);
		
		// 2 bookings
		String[] refreshment4 = "Dr.Pepper,Smiths,Donuts".split(",");
		Car ford = new SilverServiceCar("FOR756", "Ford", "Ranger", "Magic Mike", 6, 3.2, refreshment4);
		cars[itemCount] = ford;
		itemCount++;
		ford.book("Jaime", "Drop", new DateTime(1), 5);
		ford.book("Federico", "Gaytan", new DateTime(2), 3);
		
		// 1 booked, 1 completed
		String[] refreshment5 = "Champagne,Wine,Beer".split(",");
		Car holden = new SilverServiceCar("HOL396", "Holden", "Commodore", "Nath Walker", 3, 4.5, refreshment5);
		cars[itemCount] = holden;
		itemCount++;
		holden.book("Auduin", "Wrynn", new DateTime(2), 3);
		DateTime inOneDay = new DateTime(1);
		holden.book("Kamina", "Simon", inOneDay, 2);
		holden.completeBooking("Kamina", "Simon", inOneDay, 50);
		
		// 1 booked, 1 completed
		String[] refreshment6 = "Mints,Soda Water,Water".split(",");
		Car kia = new SilverServiceCar("KIA047", "Kia", "Cerato", "Pike Johann", 4, 5, refreshment6);
		cars[itemCount] = kia;
		itemCount++;
		kia.book("Anthony", "Jig", new DateTime(1), 2);
		DateTime inTwoDays = new DateTime(2);
		kia.book("Evan", "Noss", inTwoDays, 2);
		kia.completeBooking("Evan", "Noss", inTwoDays, 23);
	}

	/*
	 * Store all of one car type in another array
	 */
	public String displayAllBookings(String type, String order) {
		if(itemCount == 0) {
			return "No cars have been added to the system.";
		}
		
		// Checks if there is the type of car in the array
		if (checkExist(type) == false) {
			return "No cars of that type has been added.";
		}
		
		changeArray(type, order);
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nSummary of all cars: ");
		sb.append("\n");

		for (int i = 0; i < sortCars.length; i++) {
			if (sortCars[i] != null) {
				sb.append(sortCars[i].getDetails());
			}
		}
		return sb.toString();
	}
	
	// Placing one type of car into another array for sorting
	private void changeArray(String type, String order) {
		int sortArray = 0;
		sortCars = new Car[15];
		
		if (type.equals("SD")) {
			for (int m = 0; m < cars.length; m++) {
				if (cars[m] != null) {
					if (cars[m].getCarType() == "SD") {
						sortCars[sortArray] = cars[m];
						sortArray++;
					}
				}
			}
			sortMethod(order);
		} else if (type.equals("SS")) {
			for (int m = 0; m < cars.length; m++) {
				if (cars[m] != null) {
					if (cars[m].getCarType() == "SS") {
						sortCars[sortArray] = cars[m];
						sortArray++;
					}
				}
			}
			sortMethod(order);
		}
	}
	
	// Sort method depending on order chosen
	private void sortMethod(String order) {
		Car temp = null;
		
		if (order.equals("A")) {
			for (int i = 0; i < sortCars.length; i++) {
				for (int j = 1; j < (sortCars.length - i); j++) {
					if (sortCars[j] != null) {
						if (sortCars[j - 1].getRegNo().compareTo(sortCars[j].getRegNo()) > 0) {
							temp = sortCars[j - 1];
							sortCars[j - 1] = sortCars[j];
							sortCars[j] = temp;
						}
					}
				}
			}
		} else if (order.equals("D")) {
			for (int i = 0; i < sortCars.length; i++) {
				for (int j = 1; j < (sortCars.length - i); j++) {
					if (sortCars[j] != null) {
						if (sortCars[j - 1].getRegNo().compareTo(sortCars[j].getRegNo()) < 0) {
							temp = sortCars[j - 1];
							sortCars[j - 1] = sortCars[j];
							sortCars[j] = temp;
						}
					}
				}
			}
		}
	}

	private boolean checkExist(String type) {
		if (type.equals("SD")) {
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					if (cars[i].getCarType() == "SD") {
						return true;
					} else if (i == cars.length - 1) {
						return false;
					}
				}
			}
		} else if (type.equals("SS")) {
			for (int j = 0; j < cars.length; j++) {
				if (cars[j] != null) {
					if (cars[j].getCarType() == "SS") {
						return true;
					} else if (j == cars.length - 1) {
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public String isValidId(String id) {
		return MiRidesUtilities.isRegNoValid(id);
	}
	
	public String isValidPassengerCapacity(int passengerNumber) {
		return MiRidesUtilities.isPassengerCapacityValid(passengerNumber);
	}
	
	public String isValidBookingFee(double bookingFee) {
		return MiRidesUtilities.isValidBookingFee(bookingFee);
	}

	public boolean checkIfCarExists(String regNo) {
		Car car = null;
		if (regNo.length() != 6) {
			return false;
		}
		car = getCarById(regNo);
		if (car == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private Car getCarById(String regNo) {
		Car car = null;

		for (int i = 0; i < cars.length; i++) {
			if(cars[i] != null) {
				if (cars[i].getRegNo().equals(regNo)) {
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	// For testing purposes
	public void printToString(String type) {
		if (type.equals("SD")) {
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					if (cars[i].getCarType().equals("SD")) {
						System.out.println(cars[i].toString());
					}
				}
			}
		} else if (type.equals("SS")) {
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					if (cars[i].getCarType().equals("SS")) {
						System.out.println(cars[i].toString());
					}
				}
			}
		}
	}
}
