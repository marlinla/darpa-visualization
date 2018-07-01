package com.darpa.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;


public class DarpaCode {
	private static final String IP_PATTERN = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b \\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b (\\{.*?\\})";
	
	public static final String IP_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\edge_list_w\\";
	public static final String PREDICTED_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\predicted\\"; 
	
	public static final String ATTACK_FILE = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\anomal_edges_from_list.txt";
	public static final String IP_FILE = IP_PATH + "graph_";
	public static final String PREDICTED_FILE = PREDICTED_PATH + "graph_";
	
	
	public String readIPFile(ServletContext servletContext) throws FileNotFoundException {
		return readIPFile(servletContext, IP_FILE);
	}
	
	public String readIPFile(ServletContext servletContext, String id) throws FileNotFoundException {
		String result = ""; 
		String filePath = IP_FILE + id;
		File file = new File(filePath);
		Pattern pattern = Pattern.compile(IP_PATTERN);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				result += matcher.group(1) + "." + matcher.group(2) + "." + matcher.group(3) + "." + matcher.group(4) + "," 
						+ matcher.group(5) + "." + matcher.group(6) + "." + matcher.group(7) + "." + matcher.group(8) + "," 
						+ matcher.group(9) + System.lineSeparator();
			}
		}
		scanner.close();
		return result;
	}
	public String readPredictedFile(ServletContext servletContext, String id) throws FileNotFoundException {
		String result = "";
		String filePath = PREDICTED_FILE + id;
		System.out.println(filePath);
		File file = new File(filePath);
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			result += scanner.nextLine() + System.lineSeparator();
		}
		scanner.close();
		
		return result;
		
	}

	public String readAttackFile(ServletContext servletContext) throws FileNotFoundException {
		String result = "";
		String filePath = ATTACK_FILE;
		File file = new File(filePath);
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			result += scanner.nextLine() + System.lineSeparator();
		}
		scanner.close();
		
		return result;
	}


}
