package exceptions;

public class InvalidRefreshments extends Exception{
	public InvalidRefreshments() {
		super("This is not a list of refreshments");
	}
}
