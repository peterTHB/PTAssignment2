package exceptions;

@SuppressWarnings("serial")
public class InvalidBooking extends Exception{
	public InvalidBooking(String message) {
		super(message);
	}
	
	// Invalid bookings
	// Book prior to day
	// Book on a booked day
	// Book when five current bookings exist
}
