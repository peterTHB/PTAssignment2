package cars;

import java.util.InputMismatchException;

import exceptions.*;
import utilities.DateTime;
import utilities.DateUtilities;

/*
 * Class:		SilverCarClass
 * Description:	The class represents a silver service car 
 * 				in a ride sharing system. 
 * Author:		Peter Bui - s3786794
 */

public class SilverServiceCar extends Car {

	private double bookingFee;
	private String[] refreshments;
	
	public SilverServiceCar(String regNo, String make, String model, String driverName, 
							int passengerCapacity, double bookingFee, String[] refreshments) throws InvalidId, InvalidRefreshments, InputMismatchException {
		super(regNo, make, model, driverName, passengerCapacity);
		
		this.bookingFee = bookingFee;
		tripFee = bookingFee;
		this.carType = "SS";
		
		if (refreshDupCheck(refreshments) && refreshLimitCheck(refreshments)) {
			this.refreshments = refreshments;
		} else {
			throw new InvalidRefreshments();
		}
	}
	
	public String[] stringSplit(String stringSplit) {
		String[] refreshList = stringSplit.split(",");
		
		return refreshList;
	}
	
	@Override
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers) throws InvalidBooking, InvalidDate {
		boolean booked = false;
		
		// Booking fee is equal to or greater than 3.00
		boolean validBookingFee = validBookingFee(bookingFee);
		// Check if there is any refreshments added
		boolean addRefreshments = checkRefreshments(refreshments);
		// Date is within range, not in past and within 3 days
		boolean dateValid = DateUtilities.dateIsNotMoreThan3Days(required);
		
		if (validBookingFee && addRefreshments && dateValid) {
			super.book(firstName, lastName, required, numPassengers);
			booked = true;
		} else {
			booked = false;
		}
		
		return booked;
	}
	
	@Override
	public String getDetails() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(super.printDetails());
		sb.append(printRefresh());
		sb.append(super.printCurrentBook());
		sb.append(super.printPastBook());
		
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
	 * Checks if the booking fee is the required amount of $3.00
	 */
	private boolean validBookingFee(double bookingFee) {
		if (bookingFee >= 3.00) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Checks if there is a list of refreshments to be qualified as a silver car
	 */
	private boolean checkRefreshments(String[] refreshments){
		if (refreshments != null) {
			return true;
		} else {
			return false;
		}
	}
	
	// Supplying list of refreshments that contain duplicate items
	private boolean refreshDupCheck(String[] refreshments) {
		for (int i = 0; i < refreshments.length; i++) {
			for (int j = i + 1; j < refreshments.length; j++) {
				if (refreshments[i] == refreshments[j]) {
					return false;
				}
			}
		}
		return true;
	}
		
	// Providing less than 3 items in refreshment list
	private boolean refreshLimitCheck(String[] refreshments) {
		if (refreshments.length < 3) {
			return false;
		} else {
			return true;
		}
	}
	
	private String printRefresh() {
		StringBuilder sb = new StringBuilder();
		
		if (checkRefreshments(refreshments) == true) {
			sb.append("\nRefreshments Available\n");
			for (int i = 0; i < refreshments.length; i++) {
				if (refreshments[i] != null) {
					sb.append(String.format("%-12s %s\n", "Item " + (i + 1) + ":", refreshments[i]));
				}
			}
		}
		
		return sb.toString();
	}
	
}
