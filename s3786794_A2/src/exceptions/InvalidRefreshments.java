package exceptions;

public class InvalidRefreshments extends Exception{
	public InvalidRefreshments(String message) {
		super(message);
	}
	
	// Invalid refreshments
	// Supplying list of refreshments that contain duplicate items
	public void refreshDupCheck(String refreshments) throws InvalidRefreshments {
		String[] refreshList = refreshments.split(",");
		for (int i = 0; i < refreshList.length; i++) {
			for (int j = i + 1; j < refreshList.length; j++) {
				if (refreshList[i] == refreshList[j]) {
					throw new InvalidRefreshments("There are duplicated refreshments.");
				}
			}
		}
	}
	
	// Providing less than 3 items in refreshment list
	public void refreshLimitCheck(String refreshments) throws InvalidRefreshments {
		String[] refreshList = refreshments.split(",");
		if (refreshList.length < 3) {
			throw new InvalidRefreshments("There are less than 3 refreshments.");
		}
	}
}
