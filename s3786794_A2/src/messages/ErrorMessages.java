package messages;

public class ErrorMessages {
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
