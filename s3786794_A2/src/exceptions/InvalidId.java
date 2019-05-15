package exceptions;

@SuppressWarnings("serial")
public class InvalidId extends Exception{
	public InvalidId() {
		super("\nInvalid regNo input.");
	}
}
