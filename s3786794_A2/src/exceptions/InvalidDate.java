package exceptions;

@SuppressWarnings("serial")
public class InvalidDate extends Exception{
	public InvalidDate() {
		super("\nInvalid date input.");
	}
}
