package utilities;

public class DateUtilities {

	/*
	 * Checks if date input is not in the past
	 */
	public static boolean dateIsNotInPast(DateTime date) {
		final int OFFSET_FOR_DAYS_IN_MILLISECONDS = 1;
		boolean notInPast = false;
		
		DateTime today = new DateTime();
		
		int daysInPast = DateTime.diffDays(date, today) + OFFSET_FOR_DAYS_IN_MILLISECONDS;
		if(daysInPast >=0) {
			notInPast = true;
		}
		
		return notInPast;
	}
	
	/*
	 * Checks if date inputs are the same
	 */
	public static boolean datesAreTheSame(DateTime date1, DateTime date2) {
		if(date1.getEightDigitDate().equals(date2.getEightDigitDate())) {
			return true;
		}
		return false;
	}
	
	/*
	 * Checks if date is more than 7 days
	 */
	public static boolean dateIsNotMoreThan7Days(DateTime date) {
		boolean within7Days = false;
		DateTime nextWeek = new DateTime(7);
		
		int daysInFuture = DateTime.diffDays(nextWeek, date);
		if(daysInFuture >0 && daysInFuture <8) {
			within7Days = true;
		}
		return within7Days;
	}
	
	/*
	 * Checks if date is more than 3 days
	 */
	public static boolean dateIsNotMoreThan3Days(DateTime date) {
		boolean within3Days = false;
		DateTime next3Days = new DateTime(3);
		
		int daysInFuture = DateTime.diffDays(next3Days, date);
		if(daysInFuture >0 && daysInFuture <4) {
			within3Days = true;
		}
		return within3Days;
	}
}
