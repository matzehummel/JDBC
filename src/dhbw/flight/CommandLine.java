
package dhbw.flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class CommandLine
{
	private Map<String,CmdIf> mCommands = new HashMap<String,CmdIf>();

	private void printHelp() {
		System.out.println("List of commands:");
		System.out.println("  help - print this message");
		System.out.println("  quit/exit - exit command line");
		mCommands.forEach((k,v)->v.printHelp());
	}

	private void addCommands() {
		mCommands.put("find", new CmdFind());
		mCommands.put("overbooked", new CmdOverbooked());
		mCommands.put("book", new CmdBook());
		mCommands.put("clear", new CmdClear());
	}

	private void commandLine() {
		try {
			// Loop forever, exit via command
			while (true) {
				Scanner sc = null;
				try {
					System.out.print("DB> ");
					String line = (new BufferedReader(new InputStreamReader(System.in))).readLine();
					sc = new Scanner(line);
					String cmd = sc.next();
					// Special commands handled here
					if (cmd.equals("quit") || cmd.equals("exit")) {
						System.out.println("Exiting...");
						break;
					}
					if (cmd.equals("help")) {
						printHelp();
						continue;
					}
					// Try to find and run command
					if (mCommands.containsKey(cmd)) {
						mCommands.get(cmd).execute(sc);
						continue;
					}
					// Command not found
					System.out.println("Unknown command: " + cmd);
					printHelp();
				}
				catch (InputMismatchException ex) {
					System.out.println("Failed to parse command");
					printHelp();
				}
				catch (NoSuchElementException ex) {
					System.out.println("Failed to parse command");
					printHelp();
				}
				finally {
					// Clean-up scanner resources
					if (sc != null) sc.close();
				}
			}
		}
		catch (IOException e) {
			System.out.println("IO EXCEPTION: " + e.getMessage());
		}
		finally { }
	}

	public static void main (String[] args) {
		CommandLine cl = new CommandLine();
		cl.addCommands();
		cl.commandLine();
	}
}
