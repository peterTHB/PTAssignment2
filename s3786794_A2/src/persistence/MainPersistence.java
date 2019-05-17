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
import java.util.StringTokenizer;

// class name is a placeholder

public class MainPersistence {
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
					wrMain.println(carString);
					wrBackUp.println(carString);
				}
			}
		}
		wrMain.close();
		wrBackUp.close();
	}
	
	public Car[] readData(String fileName) throws FileNotFoundException, IOException, InputMismatchException, 
													InvalidId, InvalidRefreshments, CorruptedFiles {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		
		Car[] newCarList = new Car[15];
		int i = 0;
		
		while ((line = br.readLine()) != null) {
			StringTokenizer readCar = new StringTokenizer(line, ":");
			
			checkFile(readCar);
			
			Car newCar = provideCars(readCar);
			
			newCarList[i] = newCar;
			i++;
		}
		br.close();
		return newCarList;
		
		
	}
	
	public void checkFile(StringTokenizer tokenReader) throws CorruptedFiles {
		if (tokenReader.countTokens() > 8) {
			throw new CorruptedFiles();
		}
	}
	
	@SuppressWarnings("unused")
	private Car provideCars(StringTokenizer tokenReader) throws InputMismatchException, InvalidId, InvalidRefreshments {
		Car car;
		
		String regNo = tokenReader.nextToken();
		String make = tokenReader.nextToken();
		String model = tokenReader.nextToken();
		String driverName = tokenReader.nextToken();
		int passengerNum = Integer.parseInt(tokenReader.nextToken());
		String available = tokenReader.nextToken();
		double tripFee = Integer.parseInt(tokenReader.nextToken());
		String[] refreshment = new String[10];
		
		for (int i = 0; i < refreshment.length; i++) {
			if (tokenReader.nextToken() != null) {
				refreshment[i] = tokenReader.nextToken();
			}
		}
		
		if (refreshment != null) {
			car = new SilverServiceCar(regNo, make, model, driverName, passengerNum, tripFee, refreshment);
		} else {
			car = new Car(regNo, make, model, driverName, passengerNum);
		}
		
		return car;
	}
}
