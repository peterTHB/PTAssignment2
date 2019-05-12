package exceptions;

public class InvalidDate extends Exception{
	public InvalidDate() {
		super("This is not a valid date");
	}
}
