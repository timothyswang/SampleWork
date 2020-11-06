package wangtimo_CSCI201L_Assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HangmanDatabase {
	
	//Database should contain table with username, password, number of wins, number of losses
	
	private static final long serialVersionUID = 1L;
	public static String CREDENTIALS_STRING;
	static Connection connection;
	boolean success;
	
	HangmanDatabase(String DBConnection, String DBUsername, String DBPassword){
		System.out.println("Trying to connect to database...");
		
		CREDENTIALS_STRING = DBConnection + "&user=" + DBUsername + "&password=" + DBPassword;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			System.out.println("Connected!");
			success = true;
					
		} catch (Exception e){
			System.out.println("Unable to connect to database " + DBConnection + " with username " + DBUsername + " and password " + DBPassword + ".");
			success = false;
		}
	}
	
	boolean successful() {
		return success;
	}
	
	boolean checkUser1(String Username){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hw4 WHERE Username=?");
			preparedStatement.setString(1, Username);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				//username exists
				return true;
			} else {
				//username doesn't exist
				return false;
			}
			
			//Use "Where" command
					
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	boolean checkUser(String Username, String Password){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hw4 WHERE Username=? AND Password=?");
			preparedStatement.setString(1, Username);
			preparedStatement.setString(2,  Password);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				//username exists
				return true;
			} else {
				//username doesn't exist
				return false;
			}
			
			//Use "Where" command
					
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	void addUser(String Username, String Password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
					
			//Add username and password
			PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO hw4 (Username,Password, Wins, Losses) VALUES (?,?,0,0)");
			preparedStatement2.setString(1, Username);
			preparedStatement2.setString(2, Password);			
			preparedStatement2.executeUpdate();
					
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		
		//"INSERT INTO PasswordTable (Username,Password) VALUES (?,?)"
	}
	
	int getWins(String Username, String Password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hw4 WHERE Username=? AND Password=?");
			preparedStatement.setString(1, Username);
			preparedStatement.setString(2,  Password);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				
				return resultSet.getInt("Wins");
				
				
			} else {
				return -1;
			}
			
			//Use "Where" command
					
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	int getLosses(String Username, String Password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hw4 WHERE Username=? AND Password=?");
			preparedStatement.setString(1, Username);
			preparedStatement.setString(2,  Password);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				
				return resultSet.getInt("Losses");
				
				
			} else {
				return -1;
			}
			
			//Use "Where" command
					
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	void incrementWins(String Username, String Password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE hw4 SET Wins = Wins + 1 WHERE Username=? AND Password=?");
			preparedStatement.setString(1, Username);
			preparedStatement.setString(2,  Password);
			preparedStatement.executeUpdate();

			//Use "Where" command
					
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	void incrementLosses(String Username, String Password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(CREDENTIALS_STRING);
			
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE hw4 SET Losses = Losses + 1 WHERE Username=? AND Password=?");
			preparedStatement.setString(1, Username);
			preparedStatement.setString(2,  Password);
			preparedStatement.executeUpdate();

			//Use "Where" command
					
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
