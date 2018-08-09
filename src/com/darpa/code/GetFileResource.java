package com.darpa.code;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//import org.python.util.PythonInterpreter;


public class GetFileResource {
	static String path = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\anomal_edges_from_list.txt";
	static final String SHELL = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Node2Vec\\start.bat";

	public GetFileResource() {
		// TODO Auto-generated constructor stub
	}


	
	static File getFile(String fileName) {
		
		ClassLoader classLoader = GetFileResource.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		return file;
	}
	
	private static String getFileText(String fileName) {

		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = GetFileResource.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return result.toString();

	  }
}
