package wangtimo_CSCI201L_Assignment4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

public class HangmanServer {
	
	public static Vector<HangmanServerThread> serverThreads;
	private HangmanDatabase myDatabase;
	//Map that maps game name to game instance
	private Vector<HangmanGame> gameInstances;
	private String WordFile;
	public HangmanServer(String servername, int port, String DBConnection, String DBUsername, String DBPassword,String SecretWordFile) {
		// to do --> implement your constructor
		
		gameInstances = new Vector<HangmanGame>();
		
		try {
			System.out.print("Trying to connect to server...");
			ServerSocket ss = new ServerSocket(port);
			serverThreads = new Vector<HangmanServerThread>();
			System.out.println("Connected!");
			
			myDatabase = new HangmanDatabase(DBConnection, DBUsername, DBPassword);
			
			if (!myDatabase.successful()) {
				return;
			}
			
			this.WordFile = SecretWordFile;
			
			while(true) {
				Socket s = ss.accept(); // blocking
				HangmanServerThread st = new HangmanServerThread(s, this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("Unable to connect to server " + servername + " on port " + port + ".");
			return;
		}
	}
	
	public void broadcast(String message, HangmanServerThread st) {
		if (message != null) {
			//System.out.println(message);
			for(HangmanServerThread threads : serverThreads) {
				if (st != threads) {
					threads.sendMessage(message);
				}
			}
		}
	}
	
	public HangmanDatabase getDatabase() {
		return myDatabase;
	}
	
	public Vector<HangmanGame> getGameInstances(){
		return gameInstances;
	}
	
	public String getWordFile() {
		return WordFile;
	}
	
	public HangmanGame getSpecificGame(String name) {
		for (int i = 0; i < gameInstances.size(); i++) {
			if (gameInstances.get(i).getName().contentEquals(name)) {
				return gameInstances.get(i);
			}
		}
		return null;
	}
	
	public boolean doesGameExist(String name){
		for (int i = 0; i < gameInstances.size(); i++) {
			if (gameInstances.get(i).getName().contentEquals(name) || gameInstances.get(i).getInPlay() == false) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException{
		FileInputStream inputStream = null;
		Properties p = null;
		
		Scanner in = new Scanner(System.in);
		
		String fileName = "";
		
		String ServerHostname = "";
		String ServerPort = "";
		String DBConnection = "";
		String DBUsername = "";
		String DBPassword = "";
		String SecretWordFile = "";
		
		try {
			
			//Can the file be read?
			System.out.println("What is the name of the configuration file?");
			fileName = in.nextLine();
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
				in.close();
				return;
			}
			
			if (ServerPort == null || ServerPort.contentEquals("")) {
				System.out.println("ServerPort is a required parameter in the configuration file.");
				in.close();
				return;
			}
			
			if (DBConnection == null || DBConnection.contentEquals("")) {
				System.out.println("DBConnection is a required parameter in the configuration file.");
				in.close();
				return;
			}
			
			if (DBUsername == null || DBUsername.contentEquals("")) {
				System.out.println("DBUsername is a required parameter in the configuration file.");
				in.close();
				return;
			}
			
			if (DBPassword == null || DBPassword.contentEquals("")) {
				System.out.println("DBPassword is a required parameter in the configuration file.");
				in.close();
				return;
			}
			
			if (SecretWordFile == null || SecretWordFile.contentEquals("")) {
				System.out.println("SecretWordFile is a required parameter in the configuration file.");
				in.close();
				return;
			}
			
		} /*Catches if file's not found*/catch (FileNotFoundException fnfe) {
			System.out.println("Configuration file " + fileName + " could not be found.\n");
			in.close();
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
		
		//Creates new server
		in.close();
		HangmanServer myServer = new HangmanServer(ServerHostname, intPort, DBConnection, DBUsername, DBPassword, SecretWordFile);
		
		return;
	}

}
