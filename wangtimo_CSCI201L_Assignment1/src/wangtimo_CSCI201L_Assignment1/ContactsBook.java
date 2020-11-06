package wangtimo_CSCI201L_Assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ContactsBook {
	
	static ArrayList<Contact> Book;
	
	//--------------------------
	
	//Is the first name valid?
	public static boolean validFirstName(String firstName) {
		
		return firstName.matches("[a-zA-Z]+");
	}
	
	//Is the last name valid?
	public static boolean validLastName(String lastName) {
		return lastName.matches("[a-zA-Z]+");
	}
	
	//Is the email address valid?
	public static boolean validEmailAddress(String emailAddress) {
		
		if(!emailAddress.contains("@")) {
			return false;
		}
		if(!emailAddress.contains(".com") && !emailAddress.contains(".net") && !emailAddress.contains(".edu")) {
			return false;
		}
		
		emailAddress = emailAddress.replaceAll("@","");
		emailAddress = emailAddress.substring(0, emailAddress.length() - 4);
		
		return emailAddress.matches("[a-zA-Z]+");
	}
	
	//Is the age valid?
	public static boolean validAge(String age) {
		
		int test = -1;
		
		try { 
            test = Integer.parseInt(age);
        }  
        catch (NumberFormatException e)  
        { 
            return false;
        }
		
		if (test < 0) {
			return false;
		}
		
		return true;
	}
	
	//Is nearCampus valid?
	public static boolean validNearCampus(String nearCampus) {
		nearCampus = nearCampus.toLowerCase();
		
		if (nearCampus.contentEquals("false") || nearCampus.equals("true")) {
			return true;
		}
		
		return false;
	}
	
	//----------------------

	public static boolean parseLine(String line, String fileName) {
		
		String[] stringArray;
		stringArray = new String[1];
		stringArray = line.split(",");
		
		if (stringArray.length < 6) {
			System.out.println("This file " + fileName + " is not formatted properly.");
			System.out.println("There are not enough parameters on line");
			System.out.println("'" + line + "'" + ".\n");
			return false;
		}
		
		stringArray[0].trim();		
		if (validFirstName(stringArray[0]) == false) { //
			System.out.println("This file " + fileName + " is not formatted properly.");
			System.out.println("The parameter \"" + stringArray[0] + "\" cannot be parsed as a first name.\n");
			return false;
		}
		
		stringArray[1].trim();	
		if (validLastName(stringArray[1]) == false) { //
			System.out.println("This file " + fileName + " is not formatted properly.");
			System.out.println("The parameter \"" + stringArray[1] + "\" cannot be parsed as a last name.\n");
			return false;
		}
		
		stringArray[2].trim();
		if (validEmailAddress(stringArray[2]) == false) { //
			System.out.println("This file " + fileName + " is not formatted properly.");
			System.out.println("The parameter \"" + stringArray[2] + "\" cannot be parsed as an email address.\n");
			return false;
		}
		
		stringArray[3].trim();	
		if (validAge(stringArray[3]) == false) { //	
			System.out.println("This file " + fileName + " is not formatted properly.");
			System.out.println("The parameter \"" + stringArray[3] + "\" cannot be parsed as an age.\n");
			return false;
		}
		
		int newAge = Integer.parseInt(stringArray[3]);
		
		stringArray[4].trim();	
		if (validNearCampus(stringArray[4]) == false) { //
			System.out.println("This file " + fileName + " is not formatted properly.");
			System.out.println("The parameter \"" + stringArray[4] + "\" cannot be parsed as a near campus response.\n");
			return false;
		}
		
		boolean newNearCampus = Boolean.parseBoolean(stringArray[4]);
		
		ArrayList<String> Notes = new ArrayList<String>();
		
		for (int i = 5; i<stringArray.length; i++) {
			stringArray[i].trim();
			Notes.add(stringArray[i]);
		}
		
		Contact newContact = new Contact(stringArray[0], stringArray[1], stringArray[2], newAge, newNearCampus, Notes);
		
		Book.add(newContact);
		
		return true;
	}
		
	//Sort the array function
	static void sortBook() {
		Collections.sort(Book, new ContactSort());
	}
	
	//Search the array function
	static ArrayList<Integer> searchBook(String searchName){
		
		searchName = searchName.toLowerCase();
		
		ArrayList<Integer> returnSelection = new ArrayList<Integer>();
		
		for(int i = 0; i < Book.size(); i++) {
			String lowercaseName = Book.get(i).lastName.toLowerCase();
			if (lowercaseName.contentEquals(searchName)) {
				returnSelection.add(i);
			}
		}
		return returnSelection;
	}
	
	
	public static void main(String [] args) throws IOException {
		
		Book = new ArrayList<Contact>();
		
		boolean isValid = false;
		FileReader fr = null;
		BufferedReader br = null;
		
		Scanner in = new Scanner(System.in);
		
		while (isValid == false) {
			fr = null;
			br = null;
			String fileName = "";
			try {
				
				//Can the file be read?
				System.out.println("What is the name of the contacts file? ");
				fileName = in.nextLine();
				fr = new FileReader(fileName);
				
				//Can the file be parsed correctly?
				br = new BufferedReader(fr);
				
				String line = br.readLine();
				while(line != null) {
				
					isValid = parseLine(line, fileName);
					
					//If a line is invalid, clear the Book and break out of the loop.
					if (isValid == false) {
						Book.clear();
						break;
					}
					
					line = br.readLine();
					
				}
				
			} /*Catches if file's not found*/catch (FileNotFoundException fnfe) {
				System.out.println("The file " + fileName + " could not be found.\n");
				isValid = false;
			}
			
		}
		
		boolean endLoop = false;
		
		boolean showMenu = true;
		
		while (endLoop == false) {
			
			sortBook();
			
			if (showMenu == true) {
				System.out.println("\n\t" + "1) Contact Lookup");
				System.out.println("\t" + "2) Add Contact");
				//If invalid contact, make a message.
				System.out.println("\t" + "3) Delete Contact");
				System.out.println("\t" + "4) Print to a file");
				System.out.println("\t" + "5) Exit");
			}
			
			System.out.println("\nWhat option would you like to select? ");
			
			String input = in.nextLine();
			
			if (!input.contentEquals("1") && !input.contentEquals("2") && !input.contentEquals("3") && !input.contentEquals("4") && !input.contentEquals("EXIT")) {
				System.out.println("\nThat is not a valid option.");
				showMenu = false;
				continue;
			}
			
			showMenu = true;
			
			if (input.contentEquals("1")) {
				
				System.out.println("\nEnter the contact's last name. ");
				
				String inputName = in.nextLine();
				
				ArrayList<Integer> Selection = new ArrayList<Integer>();
				
				Selection = searchBook(inputName);
				
				if(Selection.isEmpty()) {
					System.out.println("\nThere is no one with the name " + inputName + " in your contact book.");
					continue;
				}
				
				for(int i = 0; i<Selection.size(); i++) {
					
					String outputBool = "";
					if (Book.get(Selection.get(i)).nearCampus == true) {
						outputBool = "Yes";
					}
					else if (Book.get(Selection.get(i)).nearCampus == false) {
						outputBool = "No";
					}
					
					System.out.println("\nName: " + Book.get(Selection.get(i)).firstName + " " + Book.get(Selection.get(i)).lastName);
					System.out.println("Email: " + Book.get(Selection.get(i)).emailAddress);
					System.out.println("Age: " + Book.get(Selection.get(i)).age);
					System.out.println("Near Campus: " + outputBool);
					
					ArrayList<String> noteList = new ArrayList<String>();
					noteList = Book.get(Selection.get(i)).notes;
					
					String outputNote = "";
					
					for(int j = 0; j<noteList.size(); j++) {
						outputNote += noteList.get(j) + ", ";
					}
					outputNote = outputNote.substring(0, outputNote.length() - 2);
					System.out.println("Notes: " + outputNote);
				}
				continue;	
			}
			
			if (input.contentEquals("2")) {
				
				String inputFirstName = "";
				String inputLastName = "";
				String inputEmailAddress = "";
				String inputAge = "";
				String inputNearCampus = "";
				ArrayList<String> inputNotes = new ArrayList<String>();
				
				boolean getOut = false;
				while(getOut == false) {
					System.out.println("\nWhat is the first name of your new contact? ");
					inputFirstName = in.nextLine();
					inputFirstName.trim();
					if (validFirstName(inputFirstName) == false) {
						System.out.println("\nThis is not a valid first name. A first name cannot have any numbers or special characters.");
						continue;
					}
					getOut = true;
				}
				
				getOut = false;
				while(getOut == false) {
					System.out.println("\nWhat is the last name of your new contact? ");
					inputLastName = in.nextLine();
					inputLastName.trim();
					if (validLastName(inputLastName) == false) {
						System.out.println("\nThis is not a valid last name. A last name cannot have any numbers or special characters.");
						continue;
					}
					getOut = true;
				}
				
				getOut = false;
				while(getOut == false) {
					System.out.println("\nWhat is the email of your new contact? ");
					inputEmailAddress = in.nextLine();
					inputEmailAddress.trim();
					if (validEmailAddress(inputEmailAddress) == false) {
						System.out.println("\nThis is not a valid email address. An email address must have this formatting:");
						System.out.println("xxx@yyy.com");
						continue;
					}
					getOut = true;
				}
				
				getOut = false;
				while(getOut == false) {
					System.out.println("\nWhat is the age of your new contact? ");
					inputAge = in.nextLine();
					inputAge.trim();
					if (validAge(inputAge) == false) {
						System.out.println("\nThis is not a valid age. An age must be a numerical number.");
						continue;
					}
					getOut = true;
				}
				
				getOut = false;
				while(getOut == false) {
					System.out.println("\nDoes your new contact live near campus? ");
					inputNearCampus = in.nextLine();
					inputNearCampus.trim();
					inputNearCampus = inputNearCampus.toLowerCase();
					if(inputNearCampus.contentEquals("yes")) {
						inputNearCampus = "true";
					}
					else if (inputNearCampus.contentEquals("no")) {
						inputNearCampus = "false";
					}
					else {
						System.out.println("\nThis is not a valid answer. Please answer either yes or no.");
						continue;
					}
					getOut = true;
				}
				
				getOut = false;
				while(getOut == false) {
					System.out.println("\nAdd a note about your new contact. ");
					String firstNote = in.nextLine();
					firstNote.trim();
					if (firstNote.contentEquals("")) {
						System.out.println("\nYou must have at least one note.");
						continue;
					}
					inputNotes.add(firstNote);
					getOut = true;
				}
				
				getOut = false;
				while(getOut == false) {
					System.out.println("\nDo you want to add another note? ");
					String response = in.nextLine();
					response = response.toLowerCase();
					response.trim();
					if (response.contentEquals("yes")) {
						System.out.println("\nAdd a note about your new contact. ");
						String newNote = in.nextLine();
						newNote.trim();
						inputNotes.add(newNote);
					}
					else if (response.contentEquals("no")) {
						getOut = true;
					}
					else {
						System.out.println("\nThat is not a valid response. Please try again.");
					}
				}
				
				int newAge = Integer.parseInt(inputAge);
				
				boolean newNearCampus = Boolean.parseBoolean(inputNearCampus);
				
				Contact newContact = new Contact(inputFirstName, inputLastName, inputEmailAddress, newAge, newNearCampus, inputNotes);
				
				Book.add(newContact);
				
				System.out.println(inputFirstName + " " + inputLastName + " has been added to your contact list.");
				
				continue;
				
				
			}
			
			if (input.contentEquals("3")) {
				boolean getOut = false;
				while(getOut == false) {
					System.out.println("\nEnter the email of the contact you would like to delete." + "\n");
					String inputEmail = in.nextLine();
					inputEmail = inputEmail.toLowerCase();
					boolean findEmail = false;
					int contactIndex = -1;
					for(int i = 0; i < Book.size(); i++) {
						String test = Book.get(i).emailAddress.toLowerCase();
						if (test.contentEquals(inputEmail)) {
							findEmail = true;
							contactIndex = i;
							break;
						}
					}
					if (findEmail == false) {
						System.out.println("\n" + inputEmail + " does not exist in your contact list.");
						continue;
					}
					else {
						System.out.println("\n" + Book.get(contactIndex).firstName + " " + Book.get(contactIndex).lastName + " was successfully deleted from your contact list.");
						Book.remove(contactIndex);
						getOut = true;
					}
				}
			}
			
			if (input.contentEquals("4")) {
				System.out.println("\nEnter the name of the file that you would like to print your contact list to.");
				String inputFileName = in.nextLine();
				FileWriter filewriter = new FileWriter(inputFileName);
				
				for (int i = 0; i < Book.size(); i++) {
					String noteString = "";
					for (int j = 0; j < Book.get(i).notes.size(); j++) {
						noteString += Book.get(i).notes.get(j) + ",";
					}
					noteString = noteString.substring(0, noteString.length() - 1);
					
					String outputBool = "";
					if (Book.get(i).nearCampus == true) {
						outputBool = "Yes";
					}
					else if (Book.get(i).nearCampus == false) {
						outputBool = "No";
					}
					
					filewriter.write("\nName: " + Book.get(i).firstName + " " + Book.get(i).lastName + "\n");
					filewriter.write("Email: " + Book.get(i).emailAddress + "\n");
					filewriter.write("Age: " + Book.get(i).age + "\n");
					filewriter.write("Near Campus: " + outputBool + "\n");
					filewriter.write("Notes: " + noteString + "\n\n");
				}
				filewriter.close();
				System.out.println("\nSuccessfully printed all your contacts to " + inputFileName);	

			}
			
			if (input.contentEquals("EXIT")) {
				System.out.println("\nThank you for using my contacts program. Goodbye!");
				return;
			}
		}
	}
}