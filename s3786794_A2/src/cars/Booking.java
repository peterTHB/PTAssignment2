package cars;

import utilities.DateTime;
import utilities.DateUtilities;
/*
 * Booking Class
 * Represents a booking in a ride sharing system.
 * This class can be used by other objects not just cars.
 * Author: Peter Bui(Originally Rodney Cocker) - s3786794
 */

/**
 * Booking is the class responsible for creating a new Booking
 * object and ensuring all required details are correct.
 * 
 * @author Peter Bui : s3786794
 * @version 1.0
 */
public class Booking {
	
	private String id;
	private String firstName;
	private String lastName;
	private DateTime dateBooked;
	private int numPassengers;
	private double bookingFee;
	private double kilometersTravelled;
	private double tripFee;
	private Car car;
	
	private final int NAME_MINIMUM_LENGTH = 3;

	
	/**
	 * Class constructor.
	 * 
	 * @param firstName			user's first name. Takes 
	 * 							string input
	 * @param lastName			user's last name. Takes
	 * 							string input
	 * @param required			date required. Takes custom
	 * 							DateTime input
	 * @param numPassengers		number of passengers. Takes
	 * 							numeric input
	 * @param car				car object. Takes custom Car
	 * 							object 
	 */
	public Booking(String firstName, String lastName, DateTime required, int numPassengers, Car car) {
		generateId(car.getRegNo(), firstName, lastName, required);
		validateAndSetDate(required);
		validateName(firstName, lastName);
		this.numPassengers = numPassengers;
		this.car = car;
		this.bookingFee = car.getTripFee();
	}
	
	/**
	 * Method is responsible for associating user's parameters with
	 * objects' parameters
	 * 
	 * @param kilometersTravelled	kilometers traveled. Takes numeric input
	 * @param tripFee				trip fee. Takes numeric input
	 * @param bookingFee			booking fee. Takes numeric input
	 */
	public void completeBooking(double kilometersTravelled, double tripFee, double bookingFee) {
		this.kilometersTravelled = kilometersTravelled;
		this.tripFee = tripFee;
		this.bookingFee = bookingFee;
	}
	
	/**
	 * Method is responsible for returning all of this <Booking> 
	 * object's parameters.
	 * 
	 * @return 		Returns a formatted string of details of
	 * 				this booking object.
	 */
	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-16s%-20s\n", " ", getRecordMarker()));
		sb.append(String.format("%-16s%-20s %s\n", " ", "id:", id));
		sb.append(String.format("%-16s%-20s $%.2f\n", " ", "Booking Fee:", bookingFee));
		if(dateBooked != null) {
			sb.append(String.format("%-16s%-20s %s\n", " ", "Pick Up Date:", dateBooked.getFormattedDate()));
		} else {
			sb.append(String.format("%-16s%-20s %s\n", " ", "Pick Up Date:", "Invalid"));
		}
		sb.append(String.format("%-16s%-20s %s\n", " ", "Name:", firstName + " " + lastName));
		sb.append(String.format("%-16s%-20s %s\n", " ", "Passengers:", numPassengers));
		if(kilometersTravelled == 0) {
		sb.append(String.format("%-16s%-20s %s\n", " ", "Travelled:", "N/A"));
		sb.append(String.format("%-16s%-20s %s\n", " ", "Trip Fee:", "N/A"));
		} else {
			sb.append(String.format("%-16s%-20s %.2f\n", " ", "Travelled:", kilometersTravelled));
			sb.append(String.format("%-16s%-20s %.2f\n", " ", "Trip Fee:", tripFee));
		}
		sb.append(String.format("%-16s%-20s %s\n", " ", "Car Id:", car.getRegNo()));
		
		return sb.toString();
	}
	
	/**
	 * Method is responsible for providing a computer readable
	 * version of this <Booking> object's parameters.
	 * 
	 * @return		Returns a formatted string of details
	 * 				of this booking object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(":" + bookingFee);
		if(dateBooked != null) {
			sb.append(":" + dateBooked.getEightDigitDate());
		} else {
			sb.append(":" + "Invalid");
		}
		sb.append(":" + firstName + " " + lastName);
		sb.append(":" + numPassengers);
		sb.append(":" + kilometersTravelled);
		sb.append(":" + tripFee);
		sb.append(":" + car.getRegNo());
		
		return sb.toString();
	}
	
	// Required getters
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public DateTime getBookingDate() {
		return dateBooked;
	}
	
	public String getID() {
		return id;
	}

	/**
	 * Method is responsible for making a new record marker
	 * to differentiate between different booking objects.
	 * 
	 * @return 		Returns a formatted string of underscores.
	 */
	private String getRecordMarker() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 40; i++) {
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Method is responsible for generating an id from user's input.
	 * 
	 * @param regNo			registration number. Takes string input
	 * @param firstName		user's first name. Takes string input
	 * @param lastName		user's last name. Takes string input
	 * @param date			date required. Takes custom DateTime input
	 * @return 				Returns a formatted string containing required
	 * 						parameters, or an error message if parameters
	 * 						do not meet requirements.
	 */
	private void generateId(String regNo, String firstName, String lastName, DateTime date) {
		if(firstName.length() < 3 || lastName.length() < 3  || date == null) {
			id = "Invalid";
		} else {
			id = regNo + firstName.substring(0, 3).toUpperCase() + lastName.substring(0, 3).toUpperCase()
				+ date.getEightDigitDate();
		}
	}

	/**
	 * Method is responsible for validating if user's name fits
	 * the requirements for an id.
	 * 
	 * @param firstName		user's first name. Takes string input
	 * @param lastName		user's last name. Takes string input
	 */
	private void validateName(String firstName, String lastName) {
		if(firstName.length() >= NAME_MINIMUM_LENGTH && lastName.length() >= NAME_MINIMUM_LENGTH) {
			this.firstName = firstName;
			this.lastName = lastName;
		} else {
			firstName = "Invalid";
			lastName = "Invalid";
		}
	}
	
	/**
	 * Method is responsible if date parameter fits
	 * requirements for a valid date
	 * 
	 * @param date		date required. Takes custom 
	 * 					DateTime input
	 */
	private void validateAndSetDate(DateTime date) {
		if(DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan7Days(date)) {
			dateBooked = date;
		} else {
			dateBooked = null;
		}
	}
}
