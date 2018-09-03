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
		
		String[] command = {"/bin/bash", "-c", "python ~/Documents/pythonrealated/RNN/svm_lstm.py"};
		
		try {
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
			bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			eReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			
			String line = "";
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
