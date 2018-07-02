package com.darpa.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletContext;


public class DarpaCode {
	private static final String IP_PATTERN = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b \\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b (\\{.*?\\})";
	
	public static final String IP_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\edge_list_w\\";
	public static final String PREDICTED_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\predicted\\"; 
	
	public static final String ATTACK_FILE = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\anomal_edges_from_list.txt";
	public static final String IP_FILE = IP_PATH + "graph_";
	public static final String PREDICTED_FILE = PREDICTED_PATH + "graph_";

	private static final String PREDICTED_PATTERN = "(.*) - (.*) - (.*) - (.*)  - (.*)";

	private static final String ERROR_YES = "yes";

	private static final String PREDICTED_YES = "anomal";

	private static final String PREDICTED_YES_COLOR = "#00ff00";

	private static final String ERROR_COLOR = "#ffff00";

	private static final String PREDICTED_NO_COLOR = "#000000";

	private static final String LINE_SEPERATOR = System.lineSeparator();
	
	
	public String readIPFile(ServletContext servletContext) throws FileNotFoundException {
		return readIPFile(servletContext, IP_FILE);
	}
	
	public String readIPFile(ServletContext servletContext, String id) throws FileNotFoundException {
		String ipResult = ""; 
		String ipFilePath = IP_FILE + id;
		File ipFile = new File(ipFilePath);
		Pattern ipPattern = Pattern.compile(IP_PATTERN);
		Scanner ipScanner = new Scanner(ipFile);
		while(ipScanner.hasNextLine()) {
			String line = ipScanner.nextLine();
			Matcher matcher = ipPattern.matcher(line);
			if (matcher.matches()) {
				ipResult += "\"" + matcher.group(1) + "." + matcher.group(2) + "." + matcher.group(3) + "." + matcher.group(4) + "\"" + "," 
						+ "\"" + matcher.group(5) + "." + matcher.group(6) + "." + matcher.group(7) + "." + matcher.group(8) + "\""  + "," 
						+ matcher.group(9) + LINE_SEPERATOR;
			}
			else {
				System.out.println("IP(s) not valid!");
			}
		}
		ipScanner.close();
		
		if (Integer.parseInt(id) >= 32) {
		String result = "";
		String predidictedFilePath = PREDICTED_FILE + id;
		File predictedFile = new File(predidictedFilePath);
		Pattern predictedPattern = Pattern.compile(PREDICTED_PATTERN);
		Scanner predictedScanner = new Scanner(predictedFile);
		Scanner resultScanner = new Scanner(ipResult);
		//discard first line header
		if(predictedScanner.hasNextLine()) {
			predictedScanner.nextLine();
		}
		while(predictedScanner.hasNextLine() && resultScanner.hasNextLine()) {
			String line = predictedScanner.nextLine();
			Matcher matcher = predictedPattern.matcher(line);
			if (matcher.matches()) {
				if (matcher.group(4).equals(ERROR_YES)) {
					result += resultScanner.nextLine() + "," + ERROR_COLOR + LINE_SEPERATOR;
				}
				else {
					if (matcher.group(2).equals(PREDICTED_YES)) {
						result += resultScanner.nextLine() + "," + PREDICTED_YES_COLOR + LINE_SEPERATOR;
					}
					else {
						result += resultScanner.nextLine() + "," + PREDICTED_NO_COLOR + LINE_SEPERATOR;
					}
				}
			}
			else {
				System.out.println("Prediction not valid!");
				result += resultScanner.nextLine() + "," + PREDICTED_NO_COLOR + LINE_SEPERATOR;
			}
		}
		if (predictedScanner.hasNextLine() || resultScanner.hasNextLine()) {
			System.out.println("line missmatch");
		}
		predictedScanner.close();
		resultScanner.close();
		return result;
		}
		else {
			return ipResult;

		
		}
	}



}
