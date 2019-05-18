package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

import app.Menu;
import exceptions.*;

public class Driver {

	public static void main(String[] args) throws InvalidId, InvalidRefreshments, InvalidBooking, 
													InvalidDate, NumberFormatException, InputMismatchException, 
													FileNotFoundException, IOException, CorruptedFiles, NullFile {
		Menu menu = new Menu();
		menu.run();
	}

}