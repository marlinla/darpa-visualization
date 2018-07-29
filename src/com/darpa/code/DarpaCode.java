package com.darpa.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;

public class DarpaCode {
	static final String SHELL = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Node2Vec\\start.bat";

	private static final String IP_PATTERN = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b \\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b ([0-9]+)";
	private static final String PREDICTED_PATTERN = "(.*) - (.*) - (.*) - (.*)  - (.*)";

	public static final String IP_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Node2Vec\\Data\\edge_list_w2\\";
	public static final String PREDICTED_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\predicted\\";
	public static final String ATTACK_PATH = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\anomal_edges_from_list.txt";

	public static final String ATTACK_FILE = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Visualization\\anomal_edges_from_list.txt";
	public static final String IP_FILE = IP_PATH + "graph_";
	public static final String PREDICTED_FILE = PREDICTED_PATH + "graph_";

	private static final String ERROR_YES = "yes";

	private static final String PREDICTED_YES = "anomal";

	private static final String PREDICTED_YES_COLOR = "#00ff00";

	private static final String ERROR_YES_COLOR = "#ffff00";

	private static final String PREDICTED_NO_COLOR = "#000000";

	private static final String LINE_SEPERATOR = System.lineSeparator();

	private static final String ERROR_NO_COLOR = "#ff0000";

	private static final String NV_DIRECTORY = "C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\Node2Vec\\";
	
	private static final Map<Integer, List<String>> anormalMap;
	
		static {
		HashMap<Integer, List<String>> aMap;
		try {
			aMap = queryAnormalFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			aMap = null;
		}
		anormalMap = Collections.unmodifiableMap(aMap);
	}


	public String readIPFile(ServletContext servletContext) throws IOException {
		return readIPFile(servletContext, IP_FILE);
	}

	public String readIPFile(ServletContext servletContext, String id) throws IOException {
		processPythonFiles("py", "-2", "main4.py", "--id", id);
		processPythonFiles("py", "-2", "get_edge_embedding.py", id);
		
		System.out.println(anormalMap);
		
		String ipResult = processIPFiles(id);

		if (Integer.parseInt(id) >= 32) {
			return processPredictedFiles(id, ipResult);
		} else {
			return ipResult;

		}
	}

	private String processIPFiles(String id) throws FileNotFoundException {
		String result = "";
		String ipFilePath = IP_FILE + id;
		File ipFile = new File(ipFilePath);
		Pattern ipPattern = Pattern.compile(IP_PATTERN);
		Scanner ipScanner = new Scanner(ipFile);
		while (ipScanner.hasNextLine()) {
			String line = ipScanner.nextLine();
			Matcher matcher = ipPattern.matcher(line);
			if (matcher.matches()) {
				result += "\"" + matcher.group(1) + "." + matcher.group(2) + "." + matcher.group(3) + "."
						+ matcher.group(4) + "\"" + "," + "\"" + matcher.group(5) + "." + matcher.group(6) + "."
						+ matcher.group(7) + "." + matcher.group(8) + "\"" + "," + matcher.group(9) + LINE_SEPERATOR;
			} else {
				System.out.println("IP(s) not valid!");
			}
		}
		ipScanner.close();
		return result;
	}

	private String processPredictedFiles(String id, String ipResult) throws FileNotFoundException {
		String result = "";
		String predidictedFilePath = PREDICTED_FILE + id;
		File predictedFile = new File(predidictedFilePath);
		Pattern predictedPattern = Pattern.compile(PREDICTED_PATTERN);
		Scanner predictedScanner = new Scanner(predictedFile);
		Scanner resultScanner = new Scanner(ipResult);
		
	
		
		// discard first line header
		if (predictedScanner.hasNextLine()) {
			predictedScanner.nextLine();
		}
		while (predictedScanner.hasNextLine() && resultScanner.hasNextLine()) {
			String line = predictedScanner.nextLine();
			Matcher matcher = predictedPattern.matcher(line);
			if (matcher.matches()) {
				if (matcher.group(4).equals(ERROR_YES)) {
					if (matcher.group(2).equals(PREDICTED_YES)) {
						result += resultScanner.nextLine() + "," + ERROR_YES_COLOR + LINE_SEPERATOR;
					} else {
						result += resultScanner.nextLine() + "," + ERROR_NO_COLOR + LINE_SEPERATOR;
					}
				} else {
					if (matcher.group(2).equals(PREDICTED_YES)) {
						result += resultScanner.nextLine() + "," + PREDICTED_YES_COLOR + LINE_SEPERATOR;
					} else {
						result += resultScanner.nextLine() + "," + PREDICTED_NO_COLOR + LINE_SEPERATOR;
					}
				}
			} else {
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

	
	private void processPythonFiles(String... cmd) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.directory(new File(NV_DIRECTORY));
		processBuilder.redirectOutput(Redirect.INHERIT);
		Process process = processBuilder.start();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	private static HashMap<Integer, List<String>> queryAnormalFile() throws FileNotFoundException{
		HashMap<Integer, List<String>> aMap = new HashMap<>();
		Scanner scanner = new Scanner(new File("C:\\Users\\Marlin\\Documents\\Development\\Web\\darpa\\data\\classification_task\\anomal_edges_from_list.txt"));
		int key = 0;
		
		while (scanner.hasNextLine()){
			List<String> value = new ArrayList<>();
			String[] line = scanner.nextLine().split(",");
			//add pairs of anormal connections 
			for (int i = 2; i < line.length - 1; i += 2) {
				value.add(line[i] + "," + line[i+1]);	
			}
			aMap.put(key, value);
			key++;
		}
		scanner.close();
		return aMap;
	}
}
