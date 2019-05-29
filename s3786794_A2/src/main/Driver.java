package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

import app.Menu;
import exceptions.*;

/**
 * Driver is the class responsible for running the program
 * and outputting data to the user.
 * 
 * @author Peter Bui : s3786794
 * @version 1.0
 */
public class Driver {

	public static void main(String[] args) throws InvalidId, InvalidRefreshments, InvalidBooking, 
													InvalidDate, NumberFormatException, InputMismatchException, 
													FileNotFoundException, IOException, CorruptedFiles, NullFile {
		Menu menu = new Menu();
		menu.run();
	}

}