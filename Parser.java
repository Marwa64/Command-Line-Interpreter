import java.util.*;

public class Parser {
	String[] args;
	String cmd;
	
	public boolean parse(String input) {
		cmd = input;
		return true;
	};
	
	public String getCmd() {
		return cmd;
	};
	
	public String[] getArguments() {
		return args;
	};
}
