package messages;

/**
 * ErrorMessages is the class responsible for outputting
 * a set of error messages to the user for custom exceptions
 * 
 * @author Peter Bui : s3786794
 * @version 1.0
 */
public class ErrorMessages {
	/*
	 * Provides a list of error messages for custom exceptions
	 */ 
	
	public String errorBooking() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nInvalid booking input.");
		sb.append("\nRe-enter details.\n");
		
		return sb.toString();
	}
	
	public String errorDate() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nInvalid date input.");
		sb.append("\nRe-enter details.\n");
		
		return sb.toString();
	}
	
	public String errorId() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nInvalid regNo input.");
		sb.append("\nRe-enter details.\n");
		
		return sb.toString();
	}
	
	public String errorRefreshments() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nInvalid refreshments list.");
		sb.append("\nRe-enter details.\n");
		
		return sb.toString();
	}
	
	public String errorMismatch() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nInvalid car detail inputs.");
		sb.append("\nRe-enter details.\n");
		
		return sb.toString();
	}
	
	public String errorNumeric() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nNeeds numeric input.");
		sb.append("\nRe-enter details.\n");
		
		return sb.toString();
	}
}
