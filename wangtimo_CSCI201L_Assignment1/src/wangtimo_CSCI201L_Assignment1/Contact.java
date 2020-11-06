package wangtimo_CSCI201L_Assignment1;

import java.util.ArrayList;

public class Contact {
	
	//Data members
	String firstName;
	String lastName;
	String emailAddress;
	int age;
	boolean nearCampus;
	ArrayList<String> notes;
	
	//Contact Constructor
	Contact(String firstName, String lastName, String emailAddress, int age, boolean nearCampus, ArrayList<String> notes){
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.age = age;
		this.nearCampus = nearCampus;
		this.notes = notes;
	}
}