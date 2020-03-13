
package dhbw.flight;

import java.util.Scanner;


public interface CmdIf
{
	public abstract void printHelp();
	public abstract void execute(Scanner sc);
}
