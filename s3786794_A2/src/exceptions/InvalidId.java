package exceptions;

public class InvalidId extends Exception{
	public InvalidId(String message) {
		super(message);
	}
	
	// Invalid ID
	public void regNoCheck(String regNo) throws InvalidId {
		if (regNo.length() != 6 || !regNo.substring(0, 3).matches("[a-zA-Z]+")
				|| !regNo.substring(3, 6).matches("[0-9]+")) {
			throw new InvalidId("This is not a correctly typed registration number.");
		}
	}
}
