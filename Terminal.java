import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.ArrayList;

public class Terminal {
	static Scanner in = new Scanner(System.in);
	static Parser parser = new Parser();
	static String currentDir = "C:\\", defaultDir = "C:\\";
	static ArrayList<String> prevOutput = new ArrayList<String>();
	static boolean secondLS;

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

	public static void rmdir(String inputPath) {
		String absPath = getAbsolutePath(inputPath);
		File toDelete = new File(absPath);
		if(toDelete.exists())
			rm(inputPath);
		else System.out.println("No such folder or file");

	}


	public static void rm(String inputPath) {
		String absPath = getAbsolutePath(inputPath);
	
		File toDelete= new File(absPath);
	
		if (toDelete.exists()) {
				if (toDelete.delete()) {
					System.out.println("Deleted the directory: " + toDelete.getName());
				}
			}

		else System.out.println("No such file");
	}

	// Prints the current directory
	public static void pwd() {
		if (parser.overwrite() < 0 && parser.append() < 0) {
			System.out.println(currentDir);
		}
		prevOutput.add(currentDir);
	};

	// Lists all the files/folders in the current or given directory
	public static void ls(String[] arguments) {
		File dir;
		String path = "";
		if (arguments != null && !arguments[0].equals("|") && !arguments[0].equals(">") && !arguments[0].equals(">>")) {
			// handling short and long paths
			if (!arguments[0].contains(currentDir) && !arguments[0].equals(defaultDir) && !arguments[0].contains(":")) {
				path = currentDir + arguments[0] + "\\";
			} else if (!arguments[0].equals(defaultDir)) {
				path = arguments[0] + "\\";
			} else {
				path = arguments[0];
			}
			dir = new File(path);
		} else {
			dir = new File(currentDir);
		}
		// If there are any errors caught, that means the directory the user entered is incorrect
		try {
			File allFiles[] = dir.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if(!allFiles[i].isHidden()) {
					if (parser.pipe() < 0 && parser.overwrite() < 0 && parser.append() < 0 ) {
						System.out.print(allFiles[i].getName() + "  ");
					} else if(parser.pipe() > -1 && secondLS == true) {
						System.out.print(allFiles[i].getName() + "  ");
					}
		           prevOutput.add(allFiles[i].getName());
				}
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
		if (arguments == null || arguments[0].equals("|")) {
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

	public static void date() {
		java.util.Date currentDate = java.util.Calendar.getInstance().getTime();  
		System.out.println(currentDate);  
	}
	
	public static void mv(String sourcePath, String destinationPath) throws IOException {
		String editedSource="", editedDestination="";
		// Handling short and long paths
		if (!sourcePath.contains(currentDir) && !sourcePath.equals(defaultDir) && !sourcePath.contains(":")) {
			editedSource = currentDir + sourcePath;
		} else {
			editedSource = sourcePath;
		}
		File source = new File(editedSource);
		// Handling short and long paths
		if (!destinationPath.contains(currentDir) && !destinationPath.equals(defaultDir) && !destinationPath.contains(":")) {
			editedDestination = currentDir + destinationPath + "\\";
		} else if (!destinationPath.equals(defaultDir)) {
			editedDestination = destinationPath + "\\";
		} else {
			editedDestination = destinationPath;
		}
		File destination = new File(editedDestination);
		if (!source.exists())
			System.out.println("mv: cannot stat " + source + ": No such file or directory");
		if (!destination.exists()) {
			destination.mkdirs();
		}
		else {
			Files.move(source.toPath(), destination.toPath().resolve(source.toPath().getFileName()),StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	public static void mkdir(String directoryName) {
		String path = "";
		// Handling short and long paths
		if (!directoryName.contains(currentDir) && !directoryName.equals(defaultDir) && !directoryName.contains(":")) {
			path = currentDir + directoryName + "\\";
		} else if (!directoryName.equals(defaultDir)) {
			path = directoryName + "\\";
		} else {
			path = directoryName;
		}
		File newFile = new File(path);
		
		//state will be true if the file is create for the first time, false otherwise
		boolean state = newFile.mkdir();
		if (newFile.exists()) {
			if (!state) {
				System.out.println("mkdir: cannot create directory " + directoryName + " : File exists");
			}
		}
	}
	
	public static void cp(String sourcePath, String destinationPath) throws IOException { 
		String editedSource="", editedDestination="";
		// Handling short and long paths
		if (!sourcePath.contains(currentDir) && !sourcePath.equals(defaultDir) && !sourcePath.contains(":")) {
			editedSource = currentDir + sourcePath;
		} else {
			editedSource = sourcePath;
		}
		File source = new File(editedSource);
		// Handling short and long paths
		if (!destinationPath.contains(currentDir) && !destinationPath.equals(defaultDir) && !destinationPath.contains(":")) {
			editedDestination = currentDir + destinationPath + "\\";
		} else if (!destinationPath.equals(defaultDir)) {
			editedDestination = destinationPath + "\\";
		} else {
			editedDestination = destinationPath;
		}
		File destination = new File(editedDestination);
		if (!source.exists())
			System.out.println("mv: cannot stat " + source + ": No such file or directory");
		if (!destination.exists()) {
			destination.mkdirs();
		}
		else {
			Files.copy(source.toPath(), destination.toPath().resolve(source.toPath().getFileName()),StandardCopyOption.REPLACE_EXISTING);
		}
	}
	//clear screen
	public static void clear() {
		//'H' means move to top of the screen, '2J' means "clear the entire screen
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
	}
	
	public static void args(String command) {
		if (command.equals("help")) {
			System.out.println(command + ": no argument");
		} else if (command.equals("pwd")) {
			System.out.println(command + ": no argument");
		} else if (command.equals("date")) {
			System.out.println(command + ": no argument");
		} else if (command.equals("exit")) {
			System.out.println(command + ": no atgument");
		} else if (command.equals("clear")) {
			System.out.println(command + ": no argument");
		} else if (command.equals("mkdir")) {
			System.out.println(command + ": [arg1]: directoryName");
		} else if (command.equals("mv")) {
			System.out.println(command + ": [arg1]: sourcePath, [arg2]: destinationpath");
		} else if (command.equals("cd")) {
			System.out.println(command + ": [arg1]: directoryName");
		} else if (command.equals("ls")) {
			System.out.println(command + "[arg1]: directoryName");
		} else if (command.equals("cp")) {
			System.out.println(command + ": [arg1]: sourcePath, [arg2]: destinationPath");
		} else if (command.equals("cat")) {
			System.out.println(command + ": [arg1]: sourcePath");
		} else if (command.equals("more")) {
			System.out.println(command + ": [arg1]: directoryPath");
		} else if (command.equals("rmkdir")) {
			System.out.println(command + "[arg1]: directoryPath");
		} else if (command.equals("rm")) {
			System.out.println(command + ": [arg1]: sourcePath");
		}
		
	}
	public static void help() {
		System.out.println("args : List all command arguments \n"
							+ "date : Current date/time \n"
							+ "cd: Changes the current directory to another one \n"
							+ "pwd: Display current user directory \n"
							+ "ls: Lists all programs in given directory \n"
							+ "cp: If the last argument names an existing directory, cp copies each other given file into a file with the same name in that directory. Otherwise, if only two files are given, it copies the first onto the second \n"
							+ "cat: Prints the content of any given file \n"
							+ "more: Display and scroll down the output page by page or line by line \n"
							+ "mkdir: Creates a directory with the given name \n"
							+ "rmdir: Removes each given empty directory \n"
							+ "mv: Moves a given file to a given directory \n"
							+ "rm: Removes the given file, can also be used to remove directories and its subdirectories \n"
							+ "clear: Clears the current terminal screen \n"
							+ "exit : Stop all \n");
	}
	
	public static void main(String[] args) throws IOException {
		boolean run = true;
		String cmd;
		String [] arg;
		System.out.print("\n");

		while(run) {
			secondLS = false;
			prevOutput.clear();
			if (defaultDir.equals(currentDir)) {
				System.out.print("~$ ");
			} else {
				System.out.print(currentDir + "~$ ");
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
					case "date":
						date();
						break;
					case "mkdir":
						mkdir(arg[0]);
						break;
					case "help":
						help();
						break;
					case "clear":
						clear();
						break;
					case "args":
						args(arg[0]);
						break;
				}
				// | operator
				if (parser.pipe() > -1) {
					switch(arg[parser.pipe()+1]) {
						case "pwd":
							pwd();
							break;
						case "ls":
							secondLS = true;
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

