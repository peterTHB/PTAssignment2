package cars;

import java.util.InputMismatchException;

import exceptions.*;
import utilities.DateTime;
import utilities.DateUtilities;

/**
 * SilverServiceCar is the child class responsible for 
 * creating a new SilverServiceCar object and ensuring
 * the required parameters are correct.
 * 
 * @author Peter Bui : s3786794
 * @version 1.0
 */
public class SilverServiceCar extends Car {

	private double bookingFee;
	private String[] refreshments;
	
	/**
	 * Class constructor.
	 * 
	 * @param regNo						registration number. Takes 
	 * 									string input
	 * @param make						car make. Takes string input
	 * @param model						car model. Takes string input
	 * @param driverName				driver's name. Takes string input
	 * @param passengerCapacity			passenger capacity. Takes numeric
	 * 									input
	 * @param bookingFee				
	 * @param refreshments
	 * @throws InvalidId				If an invalid id exception has
	 * 									occurred
	 * @throws InputMismatchException	If an input mismatch exception has
	 * 									occurred
	 * @throws InputMismatchException	If an input mismatch exception has
	 * 									occurred
	 */
	public SilverServiceCar(String regNo, String make, String model, String driverName, 
							int passengerCapacity, double bookingFee, String[] refreshments) throws InvalidId, InvalidRefreshments, InputMismatchException {
		super(regNo, make, model, driverName, passengerCapacity);
		
		this.bookingFee = bookingFee;
		tripFee = bookingFee;
		this.carType = "SS";
		
		if (!refreshDupCheck(refreshments) && refreshLimitCheck(refreshments)) {
			this.refreshments = refreshments;
		} else {
			throw new InvalidRefreshments();
		}
	}
	
	/**
	 * Method is responsible for splitting a refreshment
	 * string into separate strings.
	 * 
	 * @param stringSplit		refreshment lists. Takes
	 * 							string input
	 * @return					Returns a string array
	 * 							as a list of refreshments
	 */
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
	
	/**
	 * Method is responsible for ensuring the bookingFee
	 * parameter is 3 or greater.
	 * 
	 * @param bookingFee		booking fee. Takes numeric
	 * 							input
	 * @return 					Returns true if bookingFee
	 * 							is 3 or above, otherwise
	 * 							false.
	 */
	private boolean validBookingFee(double bookingFee) {
		if (bookingFee >= 3.00) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method is responsible for ensuring the refreshments
	 * parameter is not null, so that a silver service car
	 * object could be fully created.
	 * 
	 * @param refreshments		list of refreshments. Takes
	 * 							string array
	 * @return					Returns true if string array
	 * 							is not empty, otherwise false.
	 */
	private boolean checkRefreshments(String[] refreshments) {
		if (refreshments != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method is responsible for checking if there is any
	 * duplicated refreshments within the refreshments
	 * parameters.
	 * 
	 * @param refreshments		list of refreshments. Takes
	 * 							string array
	 * @return					Returns true if there are
	 * 							duplicated items, otherwise
	 * 							false.
	 */
	private boolean refreshDupCheck(String[] refreshments) {
		for (int i = 0; i < refreshments.length; i++) {
			for (int j = i + 1; j < refreshments.length; j++) {
				if (refreshments[i] != null && refreshments[j] != null)
				if (refreshments[i] == refreshments[j]) {
					return true;
				}
			}
		}
		return false;
	}
		
	/**
	 * Method is responsible if refreshments list has less
	 * than 3 items or more than 5 items.
	 * 
	 * @param refreshments		list of refreshments. Takes
	 * 							string array
	 * @return					Returns true if refreshment 
	 * 							list fits requirements, 
	 * 							otherwise false.
	 */
	private boolean refreshLimitCheck(String[] refreshments) {
		if (refreshments.length < 3 || refreshments.length > 5) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Method is responsible for returning a formatted string
	 * of the list of refreshment this child <Car> object has.
	 * 
	 * @return		Returns a string of a formatted list of 
	 * 				refreshments.
	 */
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
