package shell_command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class shell_test {
	public static void main(String[] args)
	{
		Process process = null;
		BufferedReader bReader = null;
		BufferedReader eReader = null;
		
		String[] command = {"/bin/sh", "/Users/Harrypeas/scripts/mute.sh"};
		String[] command2 = {"/bin/sh", "/Users/Harrypeas/scripts/antimute.sh"};
//		String [] command3 = {"/bin/sh", "/Users/Harrypeas/scripts/open_app.sh Pages"};
		
		try {
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
			
			String line = "";

			bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			eReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while((line = bReader.readLine()) != null)
			{
				System.out.println(line);
			}
			
			while((line = eReader.readLine()) != null)
			{
				System.out.println(line);
			}
			
			Thread.sleep(5000);
			process = Runtime.getRuntime().exec(command2);
			process.waitFor();
			bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			eReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while((line = bReader.readLine()) != null)
			{
				System.out.println(line);
			}
			
			while((line = eReader.readLine()) != null)
			{
				System.out.println(line);
			}

			bReader.close();
			eReader.close();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if (process != null) {
			process.destroy();
		}
	}
}
