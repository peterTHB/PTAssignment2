package exceptions;

@SuppressWarnings("serial")
public class NullFile extends Exception {
	public NullFile() {
		super("\nFile doesn't have data.");
	}
}
