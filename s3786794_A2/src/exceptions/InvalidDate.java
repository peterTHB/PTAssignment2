package exceptions;

public class InvalidDate extends Exception{
	public InvalidDate(String message) {
		super(message);
	}
	
	// Invalid dates
	public void dateCheck(String date) throws InvalidDate {
		if (!date.contains("/")) {
			throw new InvalidDate("This is not a correctly formatted date.");
		}
	}
}
