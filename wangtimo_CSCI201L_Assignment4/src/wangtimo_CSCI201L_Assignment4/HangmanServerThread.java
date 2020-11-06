package wangtimo_CSCI201L_Assignment4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

//Game object instance



public class HangmanServerThread extends Thread {
	private HangmanGame myGame;
	
	private String Username;
	private String Password;
	
	private PrintWriter pw;
	private BufferedReader br;
	private HangmanServer HS;
	
	private String inputType;
	private String Choice;
	
	private HangmanServerThread thisServerThread;
	private Socket mys;
	
	public HangmanServerThread(Socket s, HangmanServer HS) {
		try {
			this.mys = s;
			this.HS = HS;
			this.thisServerThread = this;
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}

	public void sendMessage(String message) {
		pw.println(message);
		pw.flush();
	}
	
	public String getUsername() {
		return Username;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public HangmanServer getServer() {
		return HS;
	}
	
	public void StartGame() {
		myGame.GameBroadcast("Determining secret word...", null);
		
		Vector<String> myWords = new Vector<String>();
		
		try {
			FileReader fr = new FileReader(myGame.getWordFile());
			
			BufferedReader br = new BufferedReader(fr);
			
			String myline = br.readLine();
			while(myline != null) {
			
				myWords.add(myline);
				
				myline = br.readLine();
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Random rand = new Random();
		
		int myNumber = rand.nextInt(4372);
		
		String tempWord = myWords.get(myNumber);
		
		//System.out.println(tempWord);
		
		System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myGame.getName() + " has " + myGame.getMaxPlayers() + " so starting game. Secret word is " + tempWord + ".");

		
		
		int tempWordLength = tempWord.length();
		
		myGame.modifyWord(tempWord);
		
		myGame.modifyWordLength(tempWordLength);
		
		
		Vector<String> secretWord = new Vector<String>(tempWordLength);
		Vector<String> displayWord = new Vector<String>(tempWordLength);
		
		for (int i = 0; i < tempWordLength; i++) {
			secretWord.add(Character.toString(tempWord.charAt(i)));
		}
		
		for (int i = 0; i < tempWordLength; i++) {
			displayWord.add("_");
		}
		
		myGame.modifySecretWord(secretWord);
		myGame.modifyDisplayWord(displayWord);
		
		// ^^ Sets up the game variables
		
		String message = "Secret Word ";
		
		for (int i = 0; i < tempWordLength; i++) {
			message += displayWord.get(i) + " ";
		}
		
		myGame.GameBroadcast(message, null); // "Secret Word _______"
		
		message = "You have 7 incorrect guesses remaining.";
		
		myGame.GameBroadcast(message, null);
		
		message = "Waiting for " + Username + " to do something...";
		
		myGame.GameBroadcast(message, this);
		
		message = "\t1) Guess a letter.\n\t2) Guess the word.\n";
		
		sendMessage(message);
		
		myGame.modifyGuesses(7);
		
		inputType = "number";
		Choice = "-1";
		
		message = "What would you like to do?";
		sendMessage(message);
		
		//GameInput();
	}
	
	public void GameInput() {
		//put lock here
		
		while(true) {
			
		
		
			String myInput = "";
			try {
				myInput = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(true) {
				
				//Number input
				if (inputType.contentEquals("number")) {
					
					//If number input is not valid
					if (!myInput.contentEquals("1") && !myInput.contentEquals("2")) {
						sendMessage("That is not a valid option. Please try again.");
						break;
					}
					
					//If it is valid
					else {
						
						//Record either 1 or 2
						Choice = myInput;
						
						
						//Change input type to letter.
						inputType = "letter";
						
						if (Choice.contentEquals("1")) {
							sendMessage("Letter to guess - ");
						}
						
						if (Choice.contentEquals("2")) {
							sendMessage("What is the secret word?");
						}
					}
					break;
				}
				
				//Letter input
				if (inputType.contentEquals("letter")) {
					
					//If letter input is not valid
					if (!myInput.matches("[a-zA-Z]+")) {
						sendMessage("That is not a valid option. Please try again.");
						break;
					}
					
					myInput = myInput.toLowerCase();
	
					//If option 1 (guessing a letter)
					if (Choice.contentEquals("1")) {
						if (myInput.length() > 1) {
							sendMessage("That is not a valid option. Please try again.");
							break;
						}
						
						String message = Username + " has guessed letter \'" + myInput + "\'";
						
						System.out.println(java.time.LocalTime.now() + " " + Username + " - guessed letter " + myInput + ".");

						
						myGame.GameBroadcast(message, this);
						
						//Check if letter is in word.
						String tempWord = myGame.getWord();
						int tempWordLength = myGame.getWordLength();
						Vector<Integer> letterPlace = new Vector<Integer>();
						
						for (int i = 0; i < tempWordLength; i++) {
							if (myInput.contentEquals(Character.toString(tempWord.charAt(i)))) {
								letterPlace.add(i);
							}
						}
						
						if (letterPlace.isEmpty()) { //Wrong letter, subtract guess
							message = "The letter '" + myInput + "' is not in the secret word.";
							int tempGuesses = myGame.getGuesses();
							myGame.modifyGuesses(tempGuesses - 1);
							myGame.GameBroadcast(message, null);
							
							System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myInput + " is not in " + tempWord + ". " + myGame.getName() + " now has " + myGame.getGuesses() + " guesses remaining.");

							
							if (myGame.getGuesses() == 0) {
								
								myGame.GameBroadcast(Username + " ran out of guesses. You lose.", null);
								
								for(int i = 0; i < myGame.serverThreadsSize(); i++) {
									String thisUsername = myGame.getMyServerThreads().get(i).getUsername();
									String thisPassword = myGame.getMyServerThreads().get(i).getPassword();
									
									HS.getDatabase().incrementLosses(thisUsername, thisPassword);
									
									int Wins = myGame.getMyServerThreads().get(i).getServer().getDatabase().getWins(thisUsername, thisPassword);
									int Losses = myGame.getMyServerThreads().get(i).getServer().getDatabase().getLosses(thisUsername, thisPassword);
									
									myGame.GameBroadcast(thisUsername + "'s Record" + "\n" + "____________" + "\n" + "Wins - " + Wins + "\n" + "Losses - " + Losses + "\n", null);

								}

								myGame.GameBroadcast("Thank you for playing Hangman!", null);
								return;
							}
						}
						else { //Correct letter, modify display word.
							message = "The letter '" + myInput + "' is in the secret word.";
							
							String forOutput = "";
							Vector<String> tempDisplayWord = myGame.getDisplayWord();
							for (int i = 0; i < letterPlace.size(); i++) {
								tempDisplayWord.set(letterPlace.get(i), myInput.toUpperCase());
								forOutput += letterPlace.get(i) + " ";
							}
							myGame.modifyDisplayWord(tempDisplayWord);
							
							myGame.GameBroadcast(message, null);
							
							String mymessage1 = "";
							for (int i = 0; i < myGame.getWordLength(); i++) {
								mymessage1 += myGame.getDisplayWord().get(i) + " ";
							}
							
							System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myInput + " is in " + tempWord + " in position(s) " + forOutput + ". Secret word now shows " +  mymessage1 + ".");

						}
						
						
					}
					//If option 2 (guessing a word)
					else {
						
						String message = Username + " has guessed word '" + myInput + "'";
						
						myGame.GameBroadcast(message, this);
						
						System.out.println(java.time.LocalTime.now() + " " + Username + " - guessed word" + myInput + ".");

						
						if (myInput.contentEquals(myGame.getWord())) {
							
							sendMessage("That is correct! You win!");
							myGame.GameBroadcast(Username + " has guessed the word '" + myGame.getWord() + "'. " + Username + " guessed the word correctly. You lose!", this);
							

							
							Vector<HangmanServerThread> tempMyServerThreads = myGame.getMyServerThreads();
							
							String tempForInput = "";
							
							for(int i = 0; i < myGame.serverThreadsSize(); i++) {
								
								String thisUsername = myGame.getMyServerThreads().get(i).getUsername();
								String thisPassword = myGame.getMyServerThreads().get(i).getPassword();
								
								if (myGame.getMyServerThreads().get(i) == this) {
									HS.getDatabase().incrementWins(thisUsername, thisPassword);
								}
								else {
									HS.getDatabase().incrementLosses(thisUsername, thisPassword);
									tempForInput += thisUsername + " ";
								}

								int Wins = myGame.getMyServerThreads().get(i).getServer().getDatabase().getWins(thisUsername, thisPassword);
								int Losses = myGame.getMyServerThreads().get(i).getServer().getDatabase().getLosses(thisUsername, thisPassword);
								
								myGame.GameBroadcast(thisUsername + "'s Record" + "\n" + "____________" + "\n" + "Wins - " + Wins + "\n" + "Losses - " + Losses + "\n", null);			

								
							}
							
							System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myInput + " is correct. " + Username + " wins game. " + tempForInput + "have lost the game.");

	
							myGame.GameBroadcast("Thanks for playing Hangman!", null);
							return;
							
						}
						else {
							HS.getDatabase().incrementLosses(Username, Password);
							
							myGame.GameBroadcast(Username + " guessed the incorrect word. They lose.", null);
							
							System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myInput + " is incorrect. " + Username + " has lost the game and is no longer in the game.");
							
							int Wins = HS.getDatabase().getWins(Username, Password);
							int Losses = HS.getDatabase().getLosses(Username, Password);
							
							myGame.GameBroadcast(Username + "'s Record" + "\n" + "____________" + "\n" + "Wins - " + Wins + "\n" + "Losses - " + Losses + "\n", null);
							
							sendMessage("Thank you for playing Hangman!");
							
							if (myGame.serverThreadsSize() == 1) {
								return;
							}
							
							for (int i = 0; i < myGame.serverThreadsSize(); i++) {
								if (myGame.getMyServerThreads().get(i) == this) {
									myGame.removeServerThread(i);
								}
							}
							
							String mymessage = "Secret Word ";
							
							for (int i = 0; i < myGame.getWordLength(); i++) {
								mymessage += myGame.getDisplayWord().get(i) + " ";
							}
							
							myGame.GameBroadcast(mymessage, null); // "Secret Word _______"
							
							mymessage = "You have " + myGame.getGuesses() + " incorrect guesses remaining.";
				
							myGame.GameBroadcast(mymessage, null);
							
							message = "\t1) Guess a letter.\n\t2) Guess the word.\n";
							
							myGame.GameBroadcast(message, null);
							
							inputType = "number";
							Choice = "-1";
							
							message = "What would you like to do?";
							myGame.GameBroadcast(message, null);
							

							while (myGame.serverThreadsSize() != 0) {
								
							}
							return;
							
						}
						//check if word is the correct word
						
					}
				}
				//display new guessing word
				String message = "Secret Word ";
				
				for (int i = 0; i < myGame.getWordLength(); i++) {
					message += myGame.getDisplayWord().get(i) + " ";
				}
				
				myGame.GameBroadcast(message, null); // "Secret Word _______"
				
				message = "You have " + myGame.getGuesses() + " incorrect guesses remaining.";
	
				myGame.GameBroadcast(message, null);
	
				
				Vector<HangmanServerThread> tempMyServerThreads = myGame.getMyServerThreads();
				int currentThread = -1;
				for(int i = 0; i < tempMyServerThreads.size(); i++) {
					if (tempMyServerThreads.get(i) == this) {
						currentThread = i;
					}
				}
				currentThread++;
				if (currentThread >= tempMyServerThreads.size()) {
					currentThread = 0;
				}
				
				HangmanServerThread nextThread = tempMyServerThreads.get(currentThread);
				
				message = "Waiting for " + nextThread.getUsername() + " to do something...";
				
				myGame.GameBroadcast(message, nextThread);
				
				message = "\t1) Guess a letter.\n\t2) Guess the word.\n";
				
				nextThread.sendMessage(message);
				
				inputType = "number";
				Choice = "-1";
				
				message = "What would you like to do?";
				nextThread.sendMessage(message);
				
				break;
			}
		}
	}//End of gameInput
	
	
	public void run() {
		try {
			
			while(true) {
				
				//Get Username and Password
				sendMessage("Username: ");
				Username = br.readLine();

				
//				
//				while (line == null) {
//					line = br.readLine();
//					sendMessage(line);
//				}
//				Username = line;
//				sendMessage(Username);

				sendMessage("Password: ");
				Password = br.readLine();
				
				System.out.println(java.time.LocalTime.now() + " " + Username + " - trying to log in with password " + Password + ".");
				
				//Check User
				boolean checkUser = HS.getDatabase().checkUser(Username, Password);
				if (checkUser == true) {
					System.out.println(java.time.LocalTime.now() + " " + Username + " - successfully logged in.");
					break;
				}
				else {
					
					checkUser = HS.getDatabase().checkUser1(Username);
					
					if (checkUser == true) {
						System.out.println(java.time.LocalTime.now() + " " + Username + " - has an account but not successfully logged in.");

					}
					else {
						System.out.println(java.time.LocalTime.now() + " " + Username + " - does not have an account so not successfully logged in.");
					}
					
					
					sendMessage("No account exists with those credentials.");
					sendMessage("Would you like to create a new account? ");
					
					String input = br.readLine();
					
					if (input.contentEquals("Yes")) {
						sendMessage("Would you like to use the username and password above? ");
						
						String input2 = br.readLine();
						
						if (input2.contentEquals("Yes")) {
							HS.getDatabase().addUser(Username, Password);
							
							System.out.println(java.time.LocalTime.now() + " " + Username + " - created an account with " + Password + ".");

							
							break;
						}
					}
				}
			}
			
			sendMessage("Great! You are now logged in as " + Username + "!");
			sendMessage("");
			sendMessage(Username + "'s Record");
			sendMessage("____________");
					
			int Wins = HS.getDatabase().getWins(Username, Password);
			int Losses = HS.getDatabase().getLosses(Username, Password);
			sendMessage("Wins: " + Wins + "\n" + "Losses " + Losses);
			
			System.out.println(java.time.LocalTime.now() + " " + Username + " - has record " + Wins + " wins and " + Losses + " losses.");

			
			//Start Join Game
			
			//Ask if you want to start or join a game
			String joinGame = "";
			while (true) {
				sendMessage("\t" + "1) Start a Game");
				sendMessage("\t" + "2) Join a Game");
				sendMessage("");
				
				sendMessage("Would you like to start a game or join a game? ");
				
				joinGame = br.readLine();
				
				if (!joinGame.contentEquals("1") && !joinGame.contentEquals("2")) {
					continue;
				}
				else {
					break;
				}
			}
			
			
			
			if(joinGame.contentEquals("1")) {
				//Starting a game
				
				//Check if name of the game is already taken.
				while(true) {
					
					sendMessage("What is the name of the game? ");
					//System.out.println("1");
					String gameName = br.readLine();
					//System.out.println("2");
					gameName = gameName.toLowerCase();
					
					System.out.println(java.time.LocalTime.now() + " " + Username + " - wants to start a game called " + gameName + ".");

					
					if (HS.doesGameExist(gameName) == true) {
						//System.out.println("3");
						sendMessage(gameName + " already exists.");
						System.out.println(java.time.LocalTime.now() + " " + Username + " - " + gameName + " already exists, so unable to start " + gameName + ".");

						
						continue;
					}
					else {
						//System.out.println("4");
						myGame = new HangmanGame(gameName);
						//System.out.println(thisServerThread.getUsername());
//						HangmanServerThread test = new HangmanServerThread(mys, HS);
						myGame.addServerThread(thisServerThread);
						myGame.modifyWordFile(HS.getWordFile());
						HS.getGameInstances().add(myGame);
						//sendMessage("false");
						
						System.out.println(java.time.LocalTime.now() + " " + Username + " - successfully started game " + gameName + ".");

					}
					break;
				}
				
				//How many players
				String numPlayers;
				while(true) {
					
					sendMessage("How many users will be playing (1-4)? ");
					
					numPlayers = br.readLine();
					
					if (!numPlayers.contentEquals("1") && !numPlayers.contentEquals("2") && !numPlayers.contentEquals("3") && !numPlayers.contentEquals("4")) {
						sendMessage("A game can only have between 1-4 players.");
						continue;
					}
					break;
				}
				
				//If singleplayer
				if (numPlayers.contentEquals("1")) {
					sendMessage("All users have joined.");
					StartGame();
				}
				//If multiplayer
				else if (numPlayers.contentEquals("2")) {
					
					System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myGame.getName() + " needs 1 player to start game.");

					
					sendMessage("Waiting for 1 other user to join...");
					int Size = 2;
					myGame.setMaxPlayers(Size);
					while (true) {
						if (myGame.serverThreadsSize() < 2) {
							continue;
						}
						sendMessage("All users have joined.");
						StartGame();
						break;
					}
				}
				else if(numPlayers.contentEquals("3")){
					
					System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myGame.getName() + " needs 2 players to start game.");

					
					sendMessage("Waiting for 2 other user to join...");
					int Size = 3;
					myGame.setMaxPlayers(Size);
					while (true) {
						
						if (myGame.serverThreadsSize() < 3) {
							continue;
						}
						sendMessage("All users have joined.");
						StartGame();
						break;
					}
				}
				else if(numPlayers.contentEquals("4")) {
					
					System.out.println(java.time.LocalTime.now() + " " + Username + " - " + myGame.getName() + " needs 3 players to start game.");

					
					sendMessage("Waiting for 3 other user to join...");
					int Size = 4;
					myGame.setMaxPlayers(Size);
					while (true) {
						
						if (myGame.serverThreadsSize() < 4) {
							continue;
						}
						sendMessage("All users have joined.");
						StartGame();
						break;
					}
				}

			}
			
			else {
				//Joining a game
				
				//Check if name of the game exists
				String gameName = "";
				while(true) {
//					System.out.println("What is the name of the game?");
//					gameName = in.nextLine();
					
					sendMessage("What is the name of the game? ");
					
					gameName = br.readLine();
					
					gameName = gameName.toLowerCase();
					
					System.out.println(java.time.LocalTime.now() + " " + Username + " - wants to join a game called " + gameName + ".");

					
					if (HS.doesGameExist(gameName) == true) {
					}
					else {
						sendMessage("There is no game with name " + gameName);
						continue;
					}
					break;
				}
				
				//Check if there is space for user to join game
				while(true) {
					
					HangmanGame testGame = HS.getSpecificGame(gameName);
					
					if (testGame.serverThreadsSize() < testGame.getMaxPlayers()) {
						myGame = testGame;
						int myWins = HS.getDatabase().getWins(Username, Password);
						int myLosses = HS.getDatabase().getLosses(Username, Password);
						
						myGame.addServerThread(thisServerThread);
						
						String message = "User " + Username + " is in the game." + "\n\n" + Username + "'s Record" + "\n" + "____________" + "\n" + "Wins - " + myWins + "\n" + "Losses - " + myLosses + "\n";
								
						HS.broadcast(message, this);
						
						System.out.println(java.time.LocalTime.now() + " " + Username + " - successfully joined game " + gameName + ".");

						
					}
					else {
						sendMessage("The game " + gameName + "  does not have space for another user to join.");
						
						System.out.println(java.time.LocalTime.now() + " " + Username + " - " + gameName + " exists, but " + Username + " unable to join because maximum number of players have already joined " + gameName + ".");
		
						continue;
					}
					break;
				}
				
				//Get other  player information.
				sendMessage(myGame.getOtherPlayers(Username, Password));
//				String input = br.readLine();
//				sendMessage(input);
				
				//Wait until all players have joined the game.
				while (true) {
					HangmanGame testGame = HS.getSpecificGame(gameName);
					if (testGame.serverThreadsSize() == testGame.getMaxPlayers()) {
						break;
					}
					else {
						continue;
					}
				}
				
				sendMessage("All users have joined.");

			}
			
			inputType = "number";
			Choice = "-1";
			
			GameInput();
			
			
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		}
		
	return;
		
	}

	
}
