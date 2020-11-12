import java.util.*;

public class Parser {
	String[] args;
	String cmd;
	
	public boolean parse(String input) {
		String[] inputArray; 
		inputArray = input.split(" ");
		// Checks if the command is valid
		if (!inputArray[0].equals("cd") && !inputArray[0].equals("ls") && !inputArray[0].equals("cp") && !inputArray[0].equals("mv") && !inputArray[0].equals("rm") && !inputArray[0].equals("mkdir") && !inputArray[0].equals("rmdir") && !inputArray[0].equals("cat") && !inputArray[0].equals("more") && !inputArray[0].equals("pwd") && !inputArray[0].equals("args") && !inputArray[0].equals("date") && !inputArray[0].equals("help")) {
			System.out.println("Command '" + inputArray[0] +"' not found");
			return false;
		} else {
			// If the command should have at least 2 arguments
			if (inputArray[0].equals("cp") || inputArray[0].equals("mv")) { 
				if (inputArray.length < 3) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// If the command should have at least 1 argument
			if (inputArray[0].equals("mkdir") || inputArray[0].equals("rmdir") || inputArray[0].equals("cat") || inputArray[0].equals("more")) { 
				if (inputArray.length < 2) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// If the command should have 0 arguments
			if (inputArray[0].equals("pwd") && inputArray.length > 1) { 
				System.out.println("Invalid number of arguments");
				return false;
			}
			// If the command has more than 1 argument
			if (inputArray[0].equals("cd")) {
				if (inputArray.length > 2 && !inputArray[2].equals("|")) {
					System.out.println("Invalid number of arguments");
					return false;
				}
			}
			// If the command should have only 1 argument
			if ( inputArray[0].equals("rm")) {
				if (inputArray.length != 2) {
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
}
