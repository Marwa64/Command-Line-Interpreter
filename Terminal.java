import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Terminal {
	static Scanner in = new Scanner(System.in);
	static Parser parser = new Parser();
	static String currentDir = "C:\\", defaultDir = "C:\\";
	static ArrayList<String> prevOutput = new ArrayList<String>();



	public static String getAbsolutePath(String inputPath){
		String abspath = inputPath;
		inputPath = inputPath.toLowerCase();
		String current = currentDir.toLowerCase();

		if (!(inputPath.contains(current))) {
			abspath = currentDir + '\\' + inputPath;
		}
		return abspath;
	}

	public static void cat(String inputPath) throws FileNotFoundException {

		String absPath = getAbsolutePath(inputPath);
		File toRead = new File(absPath);
		String line;

		Scanner scanner = new Scanner(toRead);

		if (toRead.exists()) {
			while(scanner.hasNextLine())
			{
				line = scanner.nextLine();
				if (parser.pipe() < 0 && parser.overwrite() < 0 && parser.append() < 0 ) {
					System.out.println(line);
				}
				prevOutput.add(line);
			}
		}
		else System.out.println("No such folder or file");


	}

	public static void more(String inputPath) throws FileNotFoundException {
		String absPath = getAbsolutePath(inputPath);
		File toRead = new File(absPath);
		Scanner scanner = new Scanner(toRead);
		String line;
		int counter =10;
		if (toRead.exists()) {

			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (parser.pipe() < 0 && parser.overwrite() < 0 && parser.append() < 0 ) {
					System.out.println(line);
				}
				prevOutput.add(line);
				counter--;
				if (counter == 0) {
					counter = in.nextInt();
				}
			}
		}
		else System.out.println("No such folder or file");


	}

	public static void cp(String sourcePath, String destinationPath) {

	}



	public static void mv(String sourcePath, String destinationPath) {

	}



	public static void rmdir(String inputPath) {
		String absPath = getAbsolutePath(inputPath);
		File toDelete = new File(absPath);
		if(toDelete.exists())
			recursiveDelete(toDelete);
		else System.out.println("No such folder or file");

	}

	public static void recursiveDelete(File toDelete){
		if(!toDelete.exists())
			return;

		if(toDelete.isDirectory()){
			for(File f : toDelete.listFiles()){
				recursiveDelete(f);
			}
		}

		toDelete.delete();

		System.out.println("Deleted file/folder: "+toDelete.getAbsolutePath());


	}

	public static void rm(String inputPath) {
		String absPath = getAbsolutePath(inputPath);
	
		File toDelete= new File(absPath);
	
	
		if (!toDelete.isDirectory() && toDelete.exists()) {
				if (toDelete.delete()) {
					System.out.println("Deleted the file: " + toDelete.getName());
				}
			}
		else System.out.println("No such file");
	}

	// Prints the current directory
	public static void pwd() {
		System.out.println(currentDir);
	};

	// Lists all the files/folders in the current or given directory
	public static void ls(String[] arguments) {
		File dir;
		if (arguments != null && !arguments[0].equals("|") && !arguments[0].equals(">") && !arguments[0].equals(">>")) {
			dir = new File(arguments[0]);
		} else {
			dir = new File(currentDir);
		}
		// If there are any errors caught, that means the directory the user entered is incorrect
		try {
			File allFiles[] = dir.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if (parser.pipe() < 0 && parser.overwrite() < 0 && parser.append() < 0 ) {
					System.out.print(allFiles[i].getName() + "  ");
				}
	           prevOutput.add(allFiles[i].getName());
	        }
		} catch (Exception e) {
			System.out.print("Cannot access '"+ arguments[0] + "' : No such file or directory");
		}
        System.out.println("\n");
	};
	

	// Changes the current directory
	public static void cd(String[] arguments) {
		String userPath, actualPath="";
		// If there are no arguments change the directory to the default directory
		if (arguments == null) {
			userPath = defaultDir;
		} else {
			userPath = arguments[0];
		}
		// This is to handle the short paths
		// If the path user inputed does not include the current directory and is not empty then we add the current directory to the path
		if (!userPath.contains(currentDir) && !userPath.equals(defaultDir) && !userPath.contains(":")) {
				actualPath = currentDir + userPath + "\\";
			
		// If the user enters : we go one folder back
		} else if (userPath.equals(":")){
			if (!currentDir.equals(defaultDir)) {
				boolean done = false;
				int index = currentDir.length() - 2;
				while (!done) {
					if (currentDir.charAt(index) == '\\') {
						done = true;
					} else {
						index--;
						if (index < 0) {
							break;
						}
					}
				}
				if (done == true) {
					for (int j = 0; j < index; j++) {
						actualPath += currentDir.charAt(j);
					}
					actualPath += "\\";
				} else {
					actualPath = currentDir;
				}
			} else {
				actualPath = currentDir;
			}

		} else {
			if (!userPath.equals(defaultDir)) {
				actualPath = userPath + "\\";
			} else {
				actualPath = userPath;
			}
		}
		File dir = new File(actualPath);
		// Checks if the directory the user entered exists or not
		if (dir.isDirectory()) {
			currentDir = actualPath;
		} else {
			System.out.println("'" + actualPath + "' : No such directory");
		}
	}

	// Add any other required command in the same structure

	public static void main(String[] args) throws FileNotFoundException {
		boolean run = true;
		String cmd;
		String [] arg;
		System.out.print("\n");

		while(run) {
			prevOutput.clear();
			if (defaultDir.equals(currentDir)) {
				System.out.print(" ~$ ");
			} else {
				System.out.print(" " + currentDir + "$ ");
			}
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
					case "rmdir":
						rmdir(arg[0]);
						break;
					case "pwd":
						pwd();
						break;
					case "cat":
						cat(arg[0]);
						break;
					case "more":
						more(arg[0]);
						break;
					case "ls":
						ls(arg);
						break;
					case "cd":
						cd(arg);
						break;
				}
				// | operator
				if (parser.pipe() > -1) {
					switch(arg[parser.pipe()+1]) {
						case "pwd":
							pwd();
							break;
						case "ls":
							ls(null);
							break;
						case "cat":
							for (int i = 0; i < prevOutput.size(); i++) {
								System.out.println(prevOutput.get(i));
							}
							break;
						case "more":
							int counter = 10;
							for (int i = 0; i < prevOutput.size(); i++) {
								System.out.println(prevOutput.get(i));
								counter--;
								if (counter == 0) {
									counter = in.nextInt();
								}
							}
							break;
					}
				}
				// > and >> operators
				if (parser.overwrite() > -1 || parser.append() > -1) {
					// This if checks if there is a file name after the operator
					if (arg.length > parser.overwrite()+1 || arg.length > parser.append()+1) {
						String fileName = "";
						// Gets the file name that the user entered
						if (parser.overwrite() > -1) {
							fileName = arg[parser.overwrite()+1];
						} else {
							fileName = arg[parser.append()+1];
						}
						
						File file;
						// Handling short and long paths
						if (fileName.contains(currentDir)) {
							file = new File(fileName);
						} else {
							file = new File(currentDir + "\\" + fileName);
						}
						BufferedWriter bufferWriter;
						try {
							FileWriter writer;
							// The appending and overwriting part
							if (parser.overwrite() > -1) {
								writer = new FileWriter(file);
							} else {
								writer = new FileWriter(file, true);
							}
							bufferWriter = new BufferedWriter(writer);
							// Writing the previous output in the file
							for (int i = 0; i < prevOutput.size(); i++) {
								bufferWriter.write(prevOutput.get(i));
								bufferWriter.newLine();
							}
							bufferWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}

