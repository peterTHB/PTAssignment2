package exceptions;

public class ManageExceptions {
	
	// Invalid bookings
	// Book prior to day
	// Book on a booked day
	// Book when five current bookings exist
	
	// Invalid dates
	public void dateCheck(String date) throws InvalidDate {
		if (!date.contains("/")) {
			throw new InvalidDate("This is not a correctly formatted date.");
		}
	}
	
	
	// Invalid ID
	public void regNoCheck(String regNo) throws InvalidId {
		if (regNo.length() != 6 || !regNo.substring(0, 3).matches("[a-zA-Z]+")
				|| !regNo.substring(3, 6).matches("[0-9]+")) {
			throw new InvalidId("This is not a correctly typed registration number.");
		}
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
