package exceptions;

public class InvalidBooking extends Exception{
	public InvalidBooking() {
		super("This is not a valid booking");
	}
}
