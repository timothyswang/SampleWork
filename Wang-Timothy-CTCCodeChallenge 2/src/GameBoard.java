import java.util.ArrayList;
import java.util.Scanner;

public class GameBoard {
	
	//Board stores the where all the players' pieces are.
	static ArrayList<ArrayList<String> > Board;
	
	//This method displays the boards to the terminal.
	static void displayBoard() {
		for (int i = 5; i>=0; i--) {//i represents rows
			for(int j = 0; j<7; j++) {//j represents columns
				if (Board.get(j).size() > i) {//Checks if there is a player's piece at index i for column j
					System.out.print("| " + Board.get(j).get(i) + " ");
				}
				else { //If there are no player's piece, display "*"
					System.out.print("| * ");
				}
			}
			System.out.print("|\n");
		}
		System.out.print("\n");
	}

	public static void main(String[] args) {
		
		//Initializes a new 7x6 board.
		Board = new ArrayList<ArrayList<String> >(7);
		for(int i = 0; i < 7; i++) {
			Board.add(new ArrayList<String>(6));
		}
		
		//Scanner to read in user's response.
		Scanner in = new Scanner(System.in);
		
		//This integer will switch between 1 and 2 representing player 1 and player 2.
		String player = "1";
		
		//While loop that never breaks out
		boolean neverTrue = false;
		while(neverTrue == false) {

			//Displays board after the start of every loop.
			displayBoard();
			
			//This determines what column the user wants to play.
			int intInput = -1;
			
			//Loops until the user inputs a valid input.
			boolean repeat = true;
			while (repeat == true) {
				
				//Asks whichever player to make a move.
				System.out.println("Player " + player + ", which column would you like to use?");
				String input = in.nextLine();
				
				//Verifies that the player's input is a valid one. Will only let the user move on if the input is valid.
				if (!input.contentEquals("0") && !input.contentEquals("1") && !input.contentEquals("2") && !input.contentEquals("3") && !input.contentEquals("4") && !input.contentEquals("5") && !input.contentEquals("6")) {
					System.out.println("Sorry, that is not a valid option.\n");
					continue;
				}
				
				//Changes input into int type.
				intInput = Integer.parseInt(input);
				
				//Checks if the column is completely full. Will only let the user move on if the column is not full.
				if (Board.get(intInput).size() == 6) {
					System.out.println("Sorry, that is not a valid option.\n");
					continue;
				}
				
				repeat = false;
			}
			
			//Adds the player's input onto the board.
			Board.get(intInput).add(player);

			//Switches player each time.
			if (player.contentEquals("1")) {
				player = "2";
			}
			else {
				player = "1";
			}
		}
	}

}
