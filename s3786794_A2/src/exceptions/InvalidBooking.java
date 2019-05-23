package exceptions;

@SuppressWarnings("serial")
public class InvalidBooking extends Exception {
	public InvalidBooking() {
		super("\nInvalid booking inputs.");
	}
}
