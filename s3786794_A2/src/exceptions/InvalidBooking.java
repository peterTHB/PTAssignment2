package exceptions;

@SuppressWarnings("serial")
public class InvalidBooking extends Exception {
	public InvalidBooking() {
		super("\nInvalid booking inputs.");
	}
	
	// Invalid bookings
	// Book prior to day
	// Book on a booked day
	// Book when five current bookings exist
}
