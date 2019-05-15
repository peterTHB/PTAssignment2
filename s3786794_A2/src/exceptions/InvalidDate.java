package exceptions;

@SuppressWarnings("serial")
public class InvalidDate extends Exception{
	public InvalidDate() {
		super("\nInvalid date input.");
	}
	
	// Invalid dates
//	public void dateCheck(String date) throws InvalidDate {
//		if (!date.contains("/")) {
//			throw new InvalidDate("This is not a correctly formatted date.");
//		}
//	}
}
