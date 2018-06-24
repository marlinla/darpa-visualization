package com.darpa.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.servlet.ServletContext;

public class DarpaCode {
	
	public String read(ServletContext servletContext) throws FileNotFoundException {
		String result = ""; 
		File file = new File(servletContext.getRealPath("/") + "\\darpa-newTime-sorted-000.txt");
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			result += scanner.nextLine() + "\n";
		}
		scanner.close();
		return result;
	}

}
