package exceptions;

@SuppressWarnings("serial")
public class InvalidRefreshments extends Exception{
	public InvalidRefreshments() {
		super("\nInvalid refreshments list.");
	}
}
