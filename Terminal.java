import java.util.*;
import java.util.Scanner;

public class Terminal {
	static Scanner in = new Scanner(System.in);
	static Parser parser = new Parser();
	
	public static void cp(String sourcePath, String destinationPath ) {
		
	};
	public static void mv(String sourcePath, String destinationPath) {
		
	};
	public static void rm(String sourcePath) {
		
	};
	public static void pwd() {
		
	};
	public static void cat(String[] paths) {
		
	};
	// Add any other required command in the same structure
	
	public static void main(String[] args) {
		boolean run = true;
		String cmd;
		String [] arg;
		System.out.print("\n");
		
		while(run) {
			System.out.print(" ~$ ");
			String input = in.nextLine();
			if (input.equals("exit")) {
				run = false;
			} else if (parser.parse(input)) {
				cmd = parser.getCmd();
				arg = parser.getArguments();
				
				switch(cmd) {
					case "cp":
						cp(arg[0], arg[1]);
						break;
					case "mv":
						mv(arg[0], arg[1]);
						break;
					case "rm":
						rm(arg[0]);
						break;
					case "pwd":
						pwd();
						break;
					case "cat":
						cat(arg);
						break;
				}

			}
		}
	}
}

