package tcpserver;

import java.io.BufferedReader;  
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiThreadServer implements Runnable {

	Socket csocket;
	private final static Logger LOGGER = Logger.getLogger(Logger.class.getName());
	static FileHandler fh; 

	MultiThreadServer(Socket csocket) {
		this.csocket = csocket;
	}

	public void run() {
		try {
			  

			PrintWriter outToClient = new PrintWriter(csocket.getOutputStream(), true);
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(csocket.getInputStream()));

			String cmd = inFromClient.readLine();
			//get input from socket
			PrintStream pstream = new PrintStream(csocket.getOutputStream());
			Unix cli = new Unix();
			String reply = cli.execute(cmd);
			pstream.println(reply);
			outToClient.write(reply);
			pstream.close();
			csocket.close();
			LOGGER.setLevel(Level.INFO);
			LOGGER.info("TCP Connection Closed!");
			
		} catch (IOException e) {
			System.out.println(e);
			LOGGER.setLevel(Level.SEVERE);
			LOGGER.severe("IO Exception detected!");
			LOGGER.warning("Exception occured!");
		}
	}

	public static void main(String args[]) throws Exception { 
		fh = new FileHandler("/home/mganesan/workspace/MultithreadedTcp/src/tcpserver/tcplogs.log",true);  
		LOGGER.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();  
		fh.setFormatter(formatter);
		
		ServerSocket ssock = new ServerSocket(8090);
		//System.out.println("Listening");
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("Server is Listening to port: 8090");

		while (true) {
			Socket sock = ssock.accept();
			//System.out.println("Connected");
			LOGGER.setLevel(Level.INFO);
			LOGGER.info("New TCP connection established");
			new Thread(new MultiThreadServer(sock)).start();
		}
	}

}