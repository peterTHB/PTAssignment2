package utilities;

/**
 * MiRidesUtilities is the class responsible for doing partial
 * error checking of data of the objects within the MiRidesSystem.
 * 
 * @author Peter Bui : s3786794
 * @version 1.0
 */
public class MiRidesUtilities {
	private final static int ID_LENGTH = 6;
	
	/*
	 * Checks if registration number is valid
	 */
	public static String isRegNoValid(String regNo) {
		int regNoLength = regNo.length();
		if(regNoLength != ID_LENGTH) {
			return "Error: registration number must be 6 characters";
		}
		boolean letters = regNo.substring(0,3).matches("[a-zA-Z]+");
		if (!letters) {
			return "Error: The registration number should begin with three alphabetical characters.";
		}
		boolean numbers = regNo.substring(3).matches("[0-9]+");
		if (!numbers) {
			return "Error: The registration number should end with three numeric characters.";
		}
		return regNo;
	}

	/*
	 * Checks if passenger capacity is between 1 and 10
	 */
	public static String isPassengerCapacityValid(int passengerCapacity)
	{
		if(passengerCapacity > 0 && passengerCapacity < 10) {
			return "OK";
		} else {
			return "Error: Passenger capacity must be between 1 and 9.";
		}
	}
	
	/*
	 * Checks if booking fee for SilverServiceCars is valid
	 */
	public static String isValidBookingFee(double bookingFee) {
		if (bookingFee >= 3.00) {
			return "OK";
		} else {
			return "Error: Booking Fee must be 3.00 or greater";
		}
	}
}
