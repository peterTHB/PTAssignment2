package cars;

import utilities.DateTime;
import utilities.DateUtilities;
import utilities.MiRidesUtilities;

/*
 * Class:		Car
 * Description:	The class represents a car in a ride sharing system. 
 * Author:		Peter Bui(Originally Rodney Cocker) - s3786794
 */
public class Car {
	// Car attributes
	protected String regNo;
	protected String make;
	protected String model;
	protected String driverName;
	protected int passengerCapacity;
	protected String carType;

	// Tracking bookings
	protected Booking[] currentBookings;
	protected Booking[] pastBookings;
	protected boolean available;
	protected int bookingSpotAvailable = 0;
	protected double tripFee = 0;

	// Constants
	protected final double STANDARD_BOOKING_FEE = 1.5;
	protected final int MAXIUM_PASSENGER_CAPACITY = 10;
	protected final int MINIMUM_PASSENGER_CAPACITY = 1;

	public Car(String regNo, String make, String model, String driverName, int passengerCapacity) {
		setRegNo(regNo); // Validates and sets registration number
		setPassengerCapacity(passengerCapacity); // Validates and sets passenger capacity

		this.make = make;
		this.model = model;
		this.driverName = driverName;
		this.carType = "SD";
		available = true;
		currentBookings = new Booking[5];
		pastBookings = new Booking[15];
	}

	/*
	 * Checks to see if the booking is permissible such as a valid date, number of
	 * passengers, and general availability. Creates the booking only if conditions
	 * are met and assigns the trip fee to be equal to the standard booking fee.
	 */

	/*
	 * ALGORITHM BEGIN CHECK if car has five booking CHECK if car has a booking on
	 * date requested CHECK if the date requested is in the past. CHECK if the
	 * number of passengers requested exceeds the capacity of the car. IF any checks
	 * fail return false to indicate the booking operation failed ELSE CREATE the
	 * booking ADD the booking to the current booking array UPDATE the available
	 * status if there are now five current bookings. RETURN true to indicate the
	 * success of the booking. END
	 * 
	 * TEST Booking a car to carry 0, 10, & within/without passenger capacity.
	 * Booking car on date prior to today Booking a car on a date that is more than
	 * 7 days in advance. Booking car on a date for which it is already booked
	 * Booking six cars
	 */

	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) {
		boolean booked = false;
		// Does car have five bookings
		available = bookingAvailable();
		boolean dateAvailable = notCurrentlyBookedOnDate(required);
		// Date is within range, not in past and within the next week
		boolean dateValid = dateIsValid(required);
		// Number of passengers does not exceed the passenger capacity and is not zero.
		boolean validPassengerNumber = numberOfPassengersIsValid(numPassengers);

		// Booking is permissible
		if (available && dateAvailable && dateValid && validPassengerNumber) {
			tripFee = STANDARD_BOOKING_FEE;
			Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
			currentBookings[bookingSpotAvailable] = booking;
			bookingSpotAvailable++;
			booked = true;
		}
		return booked;
	}

	/*
	 * Completes a booking based on the name of the passenger and the booking date.
	 */
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers) {
		// Find booking in current bookings by passenger and date
		int bookingIndex = getBookingByDate(firstName, lastName, dateOfBooking);

		if (bookingIndex == -1) {
			return "Booking not found.";
		}

		return completeBooking(bookingIndex, kilometers);
	}

	/*
	 * Completes a booking based on the name of the passenger.
	 */
	public String completeBooking(String firstName, String lastName, double kilometers) {
		int bookingIndex = getBookingByName(firstName, lastName);

		if (bookingIndex == -1) {
			return "Booking not found.";
		} else {
			return completeBooking(bookingIndex, kilometers);
		}
	}

	/*
	 * Checks the current bookings to see if any of the bookings are for the current
	 * date. ALGORITHM BEGIN CHECK All bookings IF date supplied matches date for
	 * any booking date Return true ELSE Return false END
	 * 
	 */
	public boolean isCarBookedOnDate(DateTime dateRequired) {
		boolean carIsBookedOnDate = false;
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				if (DateUtilities.datesAreTheSame(dateRequired, currentBookings[i].getBookingDate())) {
					carIsBookedOnDate = true;
				}
			}
		}
		return carIsBookedOnDate;
	}

	/*
	 * Retrieves a booking id based on the name and the date of the booking
	 */
	public String getBookingID(String firstName, String lastName, DateTime dateOfBooking) {
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				int days = DateTime.diffDays(dateOfBooking, booking.getBookingDate());
				if (firstNameMatch && lastNameMatch && days == 0) {
					return booking.getID();
				}
			}
		}
		return "Booking not found";
	}

	/*
	 * Human readable presentation of the state of the car.
	 */
	public String getDetails() {
		StringBuilder sb = new StringBuilder();

		sb.append(getRecordMarker());
		sb.append(String.format("%-15s %s\n", "Reg No:", regNo));
		sb.append(String.format("%-15s %s\n", "Make & Model:", make + " " + model));

		sb.append(String.format("%-15s %s\n", "Driver Name:", driverName));
		sb.append(String.format("%-15s %s\n", "Capacity:", passengerCapacity));

		if (bookingAvailable()) {
			sb.append(String.format("%-15s %s\n", "Available:", "YES"));
		} else {
			sb.append(String.format("%-15s %s\n", "Available:", "NO"));
		}
		
		if (hasBookings(currentBookings)) {
			sb.append("\nCURRENT BOOKINGS");
			for (int i = 0; i < currentBookings.length; i++) {
				if (currentBookings[i] != null) {
					sb.append("\n" + currentBookings[i].getDetails());
				}
			}
		}

		if (hasBookings(pastBookings)) {
			sb.append("\nPAST BOOKINGS");
			for (int i = 0; i < pastBookings.length; i++) {
				if (pastBookings[i] != null) {
					sb.append("\n" + pastBookings[i].getDetails());
				}
			}
		}
		
		return sb.toString();
	}

	/*
	 * Computer readable state of the car
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (!hasBookings(currentBookings)) {
			sb.append(firstBuilder());
			sb.append("\n");
		}
		
		if (hasBookings(currentBookings)) {
			sb.append(firstBuilder());
			sb.append("|");
			for (int i = 0; i < currentBookings.length; i++) {
				if (currentBookings[i] != null) {
					sb.append(currentBookings[i].toString());
				}
			}
			sb.append("\n");
		}
		
		if (hasBookings(pastBookings)) {
			sb.append(firstBuilder());
			sb.append("|");
			for (int i = 0; i < pastBookings.length; i++) {
				if (pastBookings[i] != null) {
					sb.append(pastBookings[i].toString());
				}
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	// Returns first formatted part of toString()
	public String firstBuilder() {
		StringBuilder sb = new StringBuilder();
		sb.append(regNo + ":" + make + ":" + model);
		
		if (driverName != null) {
			sb.append(":" + driverName);
		}
		
		sb.append(":" + passengerCapacity);
		
		if (bookingAvailable()) {
			sb.append(":" + "YES");
		} else {
			sb.append(":" + "NO");
		}
		
		return sb.toString();
	}

	// Required getters
	public String getRegNo() {
		return regNo;
	}

	public String getDriverName() {
		return driverName;
	}
	
	public String getCarType() {
		return carType;
	}
	
	public double getTripFee() {
		return tripFee;
	}

	/*
	 * Checks to see if any past bookings have been recorded
	 */
	protected boolean hasBookings(Booking[] bookings) {
		boolean found = false;
		for (int i = 0; i < bookings.length; i++) {
			if (bookings[i] != null) {
				found = true;
			}
		}
		return found;
	}

	/*
	 * Processes the completion of the booking
	 */
	protected String completeBooking(int bookingIndex, double kilometers) {
		tripFee = 0;
		Booking booking = currentBookings[bookingIndex];
		// Remove booking from current bookings array.
		currentBookings[bookingIndex] = null;
		bookingSpotAvailable = bookingIndex;

		// call complete booking on Booking object
		double fee = kilometers * (STANDARD_BOOKING_FEE * 0.3);
		tripFee += fee;
		booking.completeBooking(kilometers, fee, STANDARD_BOOKING_FEE);
		// add booking to past bookings
		for (int i = 0; i < pastBookings.length; i++) {
			if (pastBookings[i] == null) {
				pastBookings[i] = booking;
				break;
			}
		}
		String result = String.format("Thank you for riding with MiRide.\nWe hope you enjoyed your trip.\n$"
				+ "%.2f has been deducted from your account.", tripFee);
		tripFee = 0;
		return result;
	}

	/*
	 * Gets the position in the array of a booking based on a name and date. Returns
	 * the index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	protected int getBookingByDate(String firstName, String lastName, DateTime dateOfBooking) {
		System.out.println();
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				Booking booking = currentBookings[i];
				boolean firstNameMatch = booking.getFirstName().toUpperCase().equals(firstName.toUpperCase());
				boolean lastNameMatch = booking.getLastName().toUpperCase().equals(lastName.toUpperCase());
				boolean dateMatch = DateUtilities.datesAreTheSame(dateOfBooking, currentBookings[i].getBookingDate());
				if (firstNameMatch && lastNameMatch && dateMatch) {
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * Gets the position in the array of a booking based on a name. Returns the
	 * index of the booking if found. Otherwise it returns -1 to indicate the
	 * booking was not found.
	 */
	public int getBookingByName(String firstName, String lastName) {
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				boolean firstNameMatch = currentBookings[i].getFirstName().toUpperCase()
						.equals(firstName.toUpperCase());
				boolean lastNameMatch = currentBookings[i].getLastName().toUpperCase().equals(lastName.toUpperCase());
				if (firstNameMatch && lastNameMatch) {
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * A record marker mark the beginning of a record.
	 */
	protected String getRecordMarker() {
		final int RECORD_MARKER_WIDTH = 60;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < RECORD_MARKER_WIDTH; i++) {
			sb.append("_");
		}
		sb.append("\n");
		return sb.toString();
	}

	/*
	 * Checks to see if the number of passengers falls within the accepted range.
	 */
	protected boolean numberOfPassengersIsValid(int numPassengers) {
		if (numPassengers >= MINIMUM_PASSENGER_CAPACITY && numPassengers < MAXIUM_PASSENGER_CAPACITY
				&& numPassengers <= passengerCapacity) {
			return true;
		}
		return false;
	}

	/*
	 * Checks that the date is not in the past or more than 7 days in the future.
	 */
	protected boolean dateIsValid(DateTime date) {
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan7Days(date);
	}

	/*
	 * Indicates if a booking spot is available. If it is then the index of the
	 * available spot is assigned to bookingSpotFree.
	 */
	protected boolean bookingAvailable() {
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] == null) {
				if(i == currentBookings.length - 1) {
					available = false;
				} else {
					available = true;
				}
				bookingSpotAvailable = i;
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks to see if if the car is currently booked on the date specified.
	 */
	protected boolean notCurrentlyBookedOnDate(DateTime date) {
		boolean foundDate = true;
		for (int i = 0; i < currentBookings.length; i++) {
			if (currentBookings[i] != null) {
				int days = DateTime.diffDays(date, currentBookings[i].getBookingDate());
				if (days == 0) {
					return false;
				}
			}
		}
		return foundDate;
	}

	/*
	 * Validates and sets the registration number
	 */
	protected void setRegNo(String regNo) {
		if (!MiRidesUtilities.isRegNoValid(regNo).contains("Error:")) {
			this.regNo = regNo;
		} else {
			this.regNo = "Invalid";
		}
	}

	/*
	 * Validates and sets the passenger capacity
	 */
	protected void setPassengerCapacity(int passengerCapacity) {
		boolean validPasengerCapcity = passengerCapacity >= MINIMUM_PASSENGER_CAPACITY
				&& passengerCapacity < MAXIUM_PASSENGER_CAPACITY;

		if (validPasengerCapcity) {
			this.passengerCapacity = passengerCapacity;
		} else {
			this.passengerCapacity = -1;
		}
	}
}
