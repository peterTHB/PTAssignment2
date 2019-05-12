package exceptions;

public class InvalidId extends Exception{
	public InvalidId() {
		super("This is not a valid ID");
	}
}
