package wangtimo_CSCI201L_Assignment1;

import java.util.Comparator;

public class ContactSort implements Comparator<Contact> {

	public int compare(Contact a, Contact b) {
		if((a.lastName).compareTo(b.lastName) != 0) {
			return( (a.lastName).compareTo(b.lastName));
		}
		else {
			return( (a.firstName).compareTo(b.firstName));
		}
		
	}
	

}
