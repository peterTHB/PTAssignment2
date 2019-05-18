package persistence;

//import app.MiRideApplication;
import cars.*;
import exceptions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;

// class name is a placeholder

public class MainPersistence {
	/*
	 * 
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
	
	/*
	 * 
	 */
	public Car[] readData(String fileName) throws FileNotFoundException, IOException, InputMismatchException, 
													InvalidId, InvalidRefreshments, CorruptedFiles, NullFile {
		// Use scanner instead?
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
	
	/*
	 * 
	 */
	public void checkFile(String[] splitCar) throws CorruptedFiles {
		if (splitCar.length < 7) {
			throw new CorruptedFiles();
		}
	}
	
	/*
	 * 
	 */
	private Car provideCars(String[] splitCar) throws InputMismatchException, InvalidId, InvalidRefreshments {
		Car car;
		
		String regNo = splitCar[0];
		String make = splitCar[1];
		String model = splitCar[2];
		String driverName = splitCar[3];
		int passengerNum = Integer.parseInt(splitCar[4]);
		double tripFee = Double.parseDouble(splitCar[6]);
		
		String[] refreshment = new String[5];
		int refreshCount = 0;
		
		if (splitCar.length < 8) {
			car = new Car(regNo, make, model, driverName, passengerNum);
		} else {
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
			car = new SilverServiceCar(regNo, make, model, driverName, passengerNum, tripFee, refreshment);
		}
		return car;
	}
}
