package wangtimo_CSCI201L_Assignment4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class HangmanGame {
	private String Name;
	private boolean inPlay;
	private int MaxPlayers;
	
	private String myWordFile;
	
	//For Game
	private int Guesses;
	
	private String Word;
	
	private int wordLength;
	
	Vector<String> secretWord;
	Vector<String> displayWord;
	//For Game
	
	//List of "Users" in this game
	private Vector<HangmanServerThread> myServerThreads;
	
	HangmanGame(String myName){
		this.Name = myName;
		this.inPlay = true;
		this.myServerThreads = new Vector<HangmanServerThread>();
	}
	
	public void removeServerThread(int i) {
		myServerThreads.remove(i);
	}
	
	public void modifySecretWord(Vector<String> tempSecretWord) {
		this.secretWord = tempSecretWord;
	}
	public Vector<String> getSecretWord(){
		return secretWord;
	}
	
	public void modifyDisplayWord(Vector<String> tempDisplayWord) {
		this.displayWord = tempDisplayWord;
	}
	public Vector<String> getDisplayWord(){
		return displayWord;
	}
	
	
	public void modifyGuesses(int myGuesses) {
		this.Guesses = myGuesses;
	}
	
	public int getGuesses() {
		return Guesses;
	}
	
	
//	public void modifyInputType(String myInputType) {
//		this.inputType = myInputType;
//	}
//	
//	public String getInputType() {
//		return inputType;
//	}
//	
//	
//	public void modifyChoice(String myChoice) {
//		this.Choice = myChoice;
//	}
//	
//	public String getChoice() {
//		return Choice;
//	}
	
	
	public void modifyWord(String myWord) {
		this.Word = myWord;
	}
	
	public String getWord() {
		return Word;
	}
	
	
	public void modifyWordLength(int myWordLength) {
		this.wordLength = myWordLength;
	}
	
	public int getWordLength() {
		return wordLength;
	}
	
	

	public void modifyWordFile(String newWordFile) {
		this.myWordFile = newWordFile;
	}
	
	public String getWordFile() {
		return myWordFile;
	}
	
	public void GameBroadcast(String message, HangmanServerThread st){
		if (message != null) {
			System.out.println(message);
			for(HangmanServerThread threads : myServerThreads) {
				if (st != threads) {
					threads.sendMessage(message);
				}
			}
		}
	}
	
	public String getOtherPlayers(String Username, String Password){
		
		String message = "";
		
		//System.out.println("Getting Other Players:");
		
		for (int i = 0; i < myServerThreads.size(); i++) {
			
			//System.out.println(myServerThreads.get(i).getUsername());
			
			if (myServerThreads.get(i).getUsername().contentEquals(Username)) {
				
			}
			else {
				
				String thisUsername = myServerThreads.get(i).getUsername();
				String thisPassword = myServerThreads.get(i).getPassword();
				int Wins = myServerThreads.get(i).getServer().getDatabase().getWins(thisUsername, thisPassword);
				int Losses = myServerThreads.get(i).getServer().getDatabase().getLosses(thisUsername, thisPassword);
				
				message += "User " + thisUsername + " is in the game." + "\n\n" + thisUsername + "'s Record" + "\n" + "____________" + "\n" + "Wins - " + Wins + "\n" + "Losses - " + Losses + "\n";
			}
		}
		
		
		return message;
	}
	
	public String getName() {
		return Name;
	}
	
	public boolean getInPlay() {
		return inPlay;
	}
	
	public void startGame() {
		this.inPlay = true;
	}
	
	public void endGame() {
		this.inPlay = false;
	}
	
	public void addServerThread(HangmanServerThread myServerThread) {
		myServerThreads.add(myServerThread);
	}
	
	public int serverThreadsSize() {
		return myServerThreads.size();
	}
	
	public void setMaxPlayers(int nPlayers) {
		this.MaxPlayers = nPlayers;
	}
	
	public int getMaxPlayers() {
		return MaxPlayers;
	}
	
	Vector<HangmanServerThread> getMyServerThreads(){
		return myServerThreads;
	}
	
}
