import java.util.*;

public class Parser {
	String[] args;
	String cmd;
	int pipeLocation, overwriteOP, appendOP;
	
	public boolean parse(String input) {
		String[] inputArray; 
		int n = 0;
		pipeLocation = -1;
		overwriteOP = -1;
		appendOP = -1;
		inputArray = input.split(" ");
		// Checks if the command is valid
		if (!inputArray[0].equals("cd") && !inputArray[0].equals("more") && !inputArray[0].equals("ls") && !inputArray[0].equals("cp") && !inputArray[0].equals("mv") && !inputArray[0].equals("rm")  && !inputArray[0].equals("mkdir") && !inputArray[0].equals("rmdir") && !inputArray[0].equals("cat") && !inputArray[0].equals("more") && !inputArray[0].equals("pwd") && !inputArray[0].equals("args") && !inputArray[0].equals("date") && !inputArray[0].equals("help") && !inputArray[0].equals("clear")) {
			System.out.println("Command '" + inputArray[0] +"' not found");
			return false;
		} else {
			for (int i = 0; i < inputArray.length; i++) {
				if (inputArray[i].equals("|")) {
					pipeLocation = i;
					break;
				}
				if (inputArray[i].equals(">")) {
					overwriteOP = i;
					break;
				}
				if (inputArray[i].equals(">>")) {
					appendOP = i;
					break;
				}
			}
			if (pipeLocation != -1 || overwriteOP != -1 || appendOP != -1) {
				n = 2;
			}
			// If the command should have at least 2 arguments
			if (inputArray[0].equals("cp") || inputArray[0].equals("mv")) { 
				if (inputArray.length < 3+n) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// If the command should have at least 1 argument
			if (inputArray[0].equals("mkdir") || inputArray[0].equals("rmdir") || inputArray[0].equals("cat") || inputArray[0].equals("more")) { 
				if (inputArray.length < 2+n) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// If the command should have 0 arguments
			if ((inputArray[0].equals("pwd") || inputArray[0].equals("clear")) && inputArray.length > 1+n) { 
				System.out.println("Invalid number of arguments");
				return false;
			}
			// If the command has more than 1 argument
			if (inputArray[0].equals("cd")) {
				if (inputArray.length > 2+n && !inputArray[2].equals("|")) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// If the command should have only 1 argument
			if ( inputArray[0].equals("rm")) {
				if (inputArray.length != 2+n) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// Otherwise the commands not mentioned can have no commands or one or more command
		}
		cmd = inputArray[0];
		args = new String[inputArray.length-1];
		for (int i = 1; i < inputArray.length; i++) {
			args[i-1] = inputArray[i];
		}
		return true;
	};
	
	public String getCmd() {
		return cmd;
	};
	
	public String[] getArguments() {
		if (args.length < 1) {
			return null;
		}
		return args;
	};
	
	public int pipe() {
		return pipeLocation-1;
	}
	public int overwrite() {
		return overwriteOP-1;
	}
	public int append() {
		return appendOP-1;
	}
}
