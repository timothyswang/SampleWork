package wangtimo_CSCI201L_Assignment4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class HangmanClient extends Thread{
	
	private BufferedReader br;
	private PrintWriter pw;
	public HangmanClient(String hostname, int port) {
		try {
			System.out.println("Trying to connect to " + hostname + ":" + port);
			Socket s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
			this.start();
			Scanner scan = new Scanner(System.in);
			while(true) {
				String line = scan.nextLine();
				pw.println(line);
				pw.flush();
			}
			
		} catch (IOException ioe) {
			System.out.println("ioe in HangmanClient constructor: " + ioe.getMessage());
		}
	}
	
	
	
	public void run() {
		try {
			while(true) {
				String line = br.readLine();
				System.out.println(line);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in Hangmanclient.run(): " + ioe.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
		FileInputStream inputStream = null;
		Properties p = null;
		
		//Scanner myin = new Scanner(System.in);
		
		String fileName = "";
		
		String ServerHostname = "";
		String ServerPort = "";
		String DBConnection = "";
		String DBUsername = "";
		String DBPassword = "";
		String SecretWordFile = "";
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
			//Can the file be read?
			System.out.println("What is the name of the configuration file?");
			fileName = reader.readLine();
			inputStream = new FileInputStream(fileName);
			
			System.out.println("Reading config file...");
			
			//Can the file be parsed correctly?
			p = new Properties();
			p.load(inputStream);
			
			ServerHostname = p.getProperty("ServerHostname");
			ServerPort = p.getProperty("ServerPort");
			DBConnection = p.getProperty("DBConnection");
			DBUsername = p.getProperty("DBUsername");
			DBPassword = p.getProperty("DBPassword");
			SecretWordFile = p.getProperty("SecretWordFile");
			
			if (ServerHostname == null || ServerHostname.contentEquals("")) {
				System.out.println("ServerHostname is a required parameter in the configuration file.");
				//myin.close();
				return;
			}
			
			if (ServerPort == null || ServerPort.contentEquals("")) {
				System.out.println("ServerPort is a required parameter in the configuration file.");
				//myin.close();
				return;
			}
			
			if (DBConnection == null || DBConnection.contentEquals("")) {
				System.out.println("DBConnection is a required parameter in the configuration file.");
				//myin.close();
				return;
			}
			
			if (DBUsername == null || DBUsername.contentEquals("")) {
				System.out.println("DBUsername is a required parameter in the configuration file.");
				//myin.close();
				return;
			}
			
			if (DBPassword == null || DBPassword.contentEquals("")) {
				System.out.println("DBPassword is a required parameter in the configuration file.");
				//myin.close();
				return;
			}
			
			if (SecretWordFile == null || SecretWordFile.contentEquals("")) {
				System.out.println("SecretWordFile is a required parameter in the configuration file.");
				//myin.close();
				return;
			}
			
		} /*Catches if file's not found*/catch (FileNotFoundException fnfe) {
			System.out.println("Configuration file " + fileName + " could not be found.\n");
			//myin.close();
			return;
		}
		
		System.out.println("Server Hostname - " + ServerHostname);
		System.out.println("Server Port - " + ServerPort);
		System.out.println("Database Connection String - " + DBConnection);
		System.out.println("Database Username - " + DBUsername);
		System.out.println("Database Password - " + DBPassword);
		System.out.println("Secret Word File - " + SecretWordFile);
		
		int intPort;
		try {
		   intPort = Integer.parseInt(ServerPort);
		}
		catch (NumberFormatException e)
		{
		   intPort = 0;
		}
		
		
		//myin.close();
		
		inputStream.close();

		HangmanClient HC = new HangmanClient(ServerHostname, intPort);
		
	}
	
	
	
}
