package main;

import app.Menu;
import exceptions.*;

public class Driver {

	public static void main(String[] args) throws InvalidId, InvalidRefreshments, InvalidBooking, InvalidDate {
		Menu menu = new Menu();
		menu.run();
	}

}