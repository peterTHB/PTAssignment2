package persistence;

import cars.*;
import exceptions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;

/**
 * MainPersistence is the class responsible for dealing with
 * external data and save files states, saving upon exiting 
 * and reading upon starting to and from a main file so that 
 * the program will return to its original state.
 * A backup file is also created in case the main file has
 * been corrupted.
 * 
 * @author Peter Bui : s3786794
 * @version 1.0
 */
public class MainPersistence {
	/**
	 * Method is responsible for saving Car objects to an external file,
	 * writing to the file if it exists, or creating a new file if file 
	 * does not exist. 
	 * A backup file is also created to ensure that if the main files does
	 * not exist, the backup file is used instead.
	 * 
	 * @param cars				cars in memory. Takes car array
	 * @throws IOException		If an input or output exception has occurred
	 */
	public void saveCars(Car[] cars) throws IOException {
		PrintWriter wrMain = new PrintWriter(new BufferedWriter(new FileWriter("MainData.txt")));
		PrintWriter wrBackUp = new PrintWriter(new BufferedWriter(new FileWriter("BackUpData.txt")));
		
		for (Car car: cars) {
			if (car != null) {
				String carString = car.toString();
				if (carString.contains("|")) {
					String[] carSplit = carString.split("\\|");
					wrMain.println(carSplit[0]);
					wrBackUp.println(carSplit[0]);
				} else {
					wrMain.print(carString);
					wrBackUp.print(carString);
				}
			}
		}
		wrMain.close();
		wrBackUp.close();
	}
	
	/**
	 * Method is responsible for reading data from an external 
	 * file into the system to create new Car objects. 
	 * 
	 * @param fileName					file name. Takes string input
	 * @return							Returns a list of Car objects from 
	 * 									an external file while the file still
	 * 									has lines to be read.
	 * @throws CorruptedFiles			If a corrupted files exception has occurred
	 * @throws InputMismatchException	If an input mismatch exception has occurred
	 * @throws InvalidId				If an invalid id exception has occurred
	 * @throws InvalidRefreshments		If an invalid refreshments exception has occurred	
	 * @throws IOException				If an input or output exception has occurred
	 * @throws NullFile					If a null file exception has occurred
	 */
	public Car[] readData(String fileName) throws CorruptedFiles, InputMismatchException, InvalidId, 
											InvalidRefreshments, IOException, NullFile {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		
		Car[] newCarList = new Car[15];
		int i = 0;
		
		while ((line = br.readLine()) != null) {
			String[] splitCar = line.split(":");
			
			checkFile(splitCar);
			
			Car newCar = provideCars(splitCar);
			
			newCarList[i] = newCar;
			i++;
		}
		
		br.close();
		return newCarList;
	}
	
	/**
	 * Method is responsible for ensuring that an external file
	 * has the correct amount of items to be read into the system
	 * to create a fully functioning Car object.
	 * 
	 * @param splitCar			list of car details. Takes string array
	 * @throws CorruptedFiles	If an corrupted files exception has occurred
	 */
	public void checkFile(String[] splitCar) throws CorruptedFiles {
		if (splitCar.length < 7) {
			throw new CorruptedFiles();
		}
	}
	
	/**
	 * Method is responsible for splitting a string array of car
	 * details into their respective components so that a new Car
	 * object could be created.
	 * 
	 * @param splitCar 					list of car details. Takes string array
	 * @return							Returns a fully created Car object of 
	 * 									either the parent Car or the child 
	 * 									SilverServiceCar if successful.
	 * @throws InputMismatchException	If an input mismatch exception has occurred
	 * @throws InvalidId				If an invalid id exception has occurred
	 * @throws InvalidRefreshments		If an invalid refreshments exception has occurred
	 */
	private Car provideCars(String[] splitCar) throws InputMismatchException, InvalidId, InvalidRefreshments {
		Car car;
		
		String regNo = splitCar[0];
		String make = splitCar[1];
		String model = splitCar[2];
		String driverName = splitCar[3];
		int passengerNum = Integer.parseInt(splitCar[4]);
		double tripFee = Double.parseDouble(splitCar[6]);
		
		if (splitCar.length < 8) {
			car = new Car(regNo, make, model, driverName, passengerNum);
		} else {
			String[] refreshment = splitRefresh(splitCar);
			car = new SilverServiceCar(regNo, make, model, driverName, passengerNum, tripFee, refreshment);
		}
		return car;
	}
	
	/**
	 * Method is responsible for splitting a refreshment list
	 * that is read from an external file to be used in the 
	 * creation of a SilverServiceCar object
	 * 
	 * ALGORITHM - Splitting refreshment list
	 * BEGIN
	 * 		DEFINE new values
	 * 		CHECKS if string input is valid
	 * 		IF string input is valid
	 * 			STORES value into other memory
	 * 			CHECKS if value has white spaces
	 * 			IF has white spaces
	 * 				SPLITS string accordingly
	 * 				STORES value into new memory
	 * 			IF it does not
	 * 				STORES value into new memory
	 * 			INCREASE counter
	 * 		RETURNS list of values
	 * ENDS
	 * 
	 * @param splitCar		cat details split. Takes string input
	 * @return				Returns an array of refreshments.
	 */
	private String[] splitRefresh(String[] splitCar) {
		String[] refreshment = new String[5];
		int refreshCount = 0;
		
		for (int i = 7; i < splitCar.length; i++) {
			if (splitCar[i] != null) {
				String[] splitRefresh = splitCar[i].split(" ");
				if (splitRefresh.length == 3) {
					refreshment[refreshCount] = splitRefresh[2];
				} else if (splitRefresh.length > 3) {
					refreshment[refreshCount] = splitRefresh[2] + " " + splitRefresh[3];
				}
				refreshCount++;
			}
		}
		
		return refreshment;
	}
}
