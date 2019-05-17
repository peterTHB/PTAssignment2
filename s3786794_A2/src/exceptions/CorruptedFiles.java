package exceptions;

@SuppressWarnings("serial")
public class CorruptedFiles extends Exception {
	public CorruptedFiles() {
		super("\nFile has been corrupted");
	}
}
