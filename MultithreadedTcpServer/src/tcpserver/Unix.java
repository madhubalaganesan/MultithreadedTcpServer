package tcpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Unix {
	String reply = "";
	public String execute(String command) {
		StringBuffer outputt = new StringBuffer();

		Process p;
		try {
			if(command.toLowerCase().contains("&&") || command.toLowerCase().contains("|")){
				String[] cmd1 =  { "/bin/bash", "-c", command};
				p = Runtime.getRuntime().exec(cmd1);
			}else{
				p = Runtime.getRuntime().exec(command);
			}
			p.waitFor(); //Causes the current thread to wait, if necessary, until the process represented by this Process object has terminated. This method returns immediately if the subprocess has already terminated. If the subprocess has not yet terminated, the calling thread will be blocked until the subprocess exits.
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine())!= null) {
				outputt.append(line + "\n");
			}
			reply = outputt.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reply;
	}
}
