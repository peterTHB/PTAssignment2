package cars;

import utilities.DateTime;
import utilities.DateUtilities;

public class SilverServiceCar extends Car {

	private double bookingFee;
	private String[] refreshments;
	
	public SilverServiceCar(String regNo, String make, String model, String driverName, 
							int passengerCapacity, double bookingFee, String[] refreshments) {
		super(regNo, make, model, driverName, passengerCapacity);
		
		this.carType = "SS";
		this.bookingFee = bookingFee;
		this.refreshments = refreshments;
	}
	
	public String[] stringSplit(String stringSplit) {
		String[] refreshList = stringSplit.split(",");
		
		return refreshList;
	}
	
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) {
		boolean booked = false;
		// Does car have five bookings
		available = bookingAvailable();
		// If the car is available to be booked on that date
		boolean dateAvailable = notCurrentlyBookedOnDate(required);
		// Date is within range, not in past and within 3 days
		boolean dateValid = dateIsValid(required);
		// Number of passengers does not exceed the passenger capacity and is not zero.
		boolean validPassengerNumber = numberOfPassengersIsValid(numPassengers);
		// Booking fee is equal to or greater than 3.00
		boolean validBookingFee = validBookingFee(bookingFee);
		// Check if there is any refreshments added
		boolean addRefreshments = checkRefreshments(refreshments);

		// Booking is permissible
		if (available && dateAvailable && dateValid && validPassengerNumber && 
			validBookingFee && addRefreshments) {
			tripFee = bookingFee;
			Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
			currentBookings[bookingSpotAvailable] = booking;
			bookingSpotAvailable++;
			booked = true;
		}
		return booked;
	}
	
	/*
	 * Processes the completion of the booking
	 */
	@Override
	protected String completeBooking(int bookingIndex, double kilometers) {
		tripFee = 0;
		Booking booking = currentBookings[bookingIndex];
		// Remove booking from current bookings array.
		currentBookings[bookingIndex] = null;
		bookingSpotAvailable = bookingIndex;

		// call complete booking on Booking object
		double fee = kilometers * (bookingFee * 0.4);
		tripFee += fee;
		booking.completeBooking(kilometers, fee, bookingFee);
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
	
	@Override
	public String getDetails() {
		StringBuilder sb = new StringBuilder();

		sb.append(getRecordMarker());
		sb.append(String.format("%-15s %s\n", "Reg No:", regNo));
		sb.append(String.format("%-15s %s\n", "Make & Model:", make + " " + model));

		sb.append(String.format("%-15s %s\n", "Driver Name:", driverName));
		sb.append(String.format("%-15s %s\n", "Capacity:", passengerCapacity));
		sb.append(String.format("%-15s %s\n", "Standard Fee:", "$" + bookingFee));

		if (bookingAvailable()) {
			sb.append(String.format("%-15s %s\n", "Available:", "YES"));
		} else {
			sb.append(String.format("%-15s %s\n", "Available:", "NO"));
		}
		
		if (checkRefreshments(refreshments) == true) {
			sb.append("\nRefreshments Available\n");
			for (int i = 0; i < refreshments.length; i++) {
				if (refreshments[i] != null) {
					sb.append(String.format("%-12s %s\n", "Item " + (i + 1) + ":", refreshments[i]));
				}
			}
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
	
	@Override
	public String firstBuilder() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(super.firstBuilder());
		sb.append(":");
		
		if (refreshments != null) {
			for (int i = 0; i < refreshments.length; i++) {
				if (refreshments[i] != null && !(i == refreshments.length - 1)) {
					sb.append("Item " + (i + 1) + " " + refreshments[i] + ":");
				}
				if (i == refreshments.length - 1) {
					sb.append("Item " + (i + 1) + " " + refreshments[i]);
				}
			}
		}
		
		return sb.toString();
	}
	
	/*
	 * Checks that the date is not in the past or more than 3 days in the future.
	 */
	@Override
	protected boolean dateIsValid(DateTime date) {
		return DateUtilities.dateIsNotInPast(date) && DateUtilities.dateIsNotMoreThan3Days(date);
	}
	
	/*
	 * Checks if the booking fee is the required amount of $3.00
	 */
	protected boolean validBookingFee(double bookingFee) {
		if (bookingFee >= 3.00) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Checks if there is a list of refreshments to be qualified as a silver car
	 */
	protected boolean checkRefreshments(String[] refreshments) {
		if (refreshments != null) {
			return true;
		} else {
			return false;
		}
	}
	
}
