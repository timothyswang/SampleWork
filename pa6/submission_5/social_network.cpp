#include "network.h"
#include "user.h"
#include <iostream>
#include <iomanip>

using namespace std;

int main(int argc, char *argv[])
{
    Network Contact; //Creates a network object.
    
    if (argc == 2){ //Checks if there is an input file.
        const char* file_input = argv[1];
        //Reads in the file.
        int result = Contact.read_friends(file_input);
        if (result == -1){
            cout << "Problem reading file. Please try again." << endl;
            return 0;
        }
    }
    
    bool continue_menu = true;
    while (continue_menu == true){ //Keep looping through the menu
        //Menu display:
        cout << "Please select a menu option." << endl;
        cout << "Option 1: Add a user. ";
        cout << "Please provide first name, last name, birth year, zip code" 
            << endl;
        
        cout << "Option 2: Add a friend connection. ";
        cout << "Please provide the two names to make friends." << endl;
        
        cout << "Option 3: Remove a friend connection. ";
        cout << "Please provide the two names to remove their friendship." 
            << endl;
        
        cout << "Option 4: Print users." << endl;
        
        cout << "Option 5: List friends. Please provide a name." << endl;
        
        cout << "Option 6: Write to file. Please provide a file name." << endl;
        
        cout << "Option 7: Shortest path between two people. ";
        cout << "Please provide the two names." << endl;
        
        cout << "Option 8: List the disjoint sets." << endl;
        
        cout << "Option 9: List friend suggestions. ";
        cout << "Please provde a name." << endl;
        
        cout << "Any other option will quit the program." << endl;
        
        int input;
        cin>>input; // Checks which option user picks.
        
        if (input == 1){
            
            string first_name;
            string last_name;
            int birth_year;
            int zip_code;
            
            cin >> first_name >> last_name >> birth_year >> zip_code;
            //Puts together first and last name into one name
            string name = first_name + " " + last_name;
            //Calls function to add user.
            Contact.add_user(name, birth_year, zip_code);
        }
        else if (input == 2){
            string first_name1;
            string last_name1;
            string first_name2;
            string last_name2;
            
            cin>> first_name1 >> last_name1 >> first_name2 >> last_name2;
            
            string name1 = first_name1 + " " + last_name1;
            string name2 = first_name2 + " " + last_name2;
            //Calls function to add connection.
            int yes_no = Contact.add_connection(name1, name2);
            if (yes_no == -1){
                cout << "The names are invalid. Try again." << endl;
            }
        }
        else if (input == 3){
            string first_name1;
            string last_name1;
            string first_name2;
            string last_name2;
            
            cin>> first_name1 >> last_name1 >> first_name2 >> last_name2;
            
            string name1 = first_name1 + " " + last_name1;
            string name2 = first_name2 + " " + last_name2;
            //Calls function to remove connection
            int yes_no = Contact.remove_connection(name1, name2);
            if (yes_no == -1){
                cout << "The names are invalid or a friendship doesn't exist.";
                cout << "Try again." << endl;
            }
        }
        else if (input == 4){
            //Calls function to print users
            Contact.print_users();
        }
        else if (input == 5){
            string firstname;
            string lastname;
            cin >> firstname >> lastname;
            string username = firstname + " " + lastname;
            //Calls function to print all of the friends
            //of a certain name.
            Contact.print_friends(username);
        }
        else if (input == 6){
            string filename_s = "";
            cin >> filename_s;
            const char* filename = filename_s.c_str();
            //Writes to a file.
            Contact.write_friends(filename);
        }
        else if(input == 7){
            string first_name1;
            string last_name1;
            string first_name2;
            string last_name2;
            
            cin>> first_name1 >> last_name1 >> first_name2 >> last_name2;
            
            string name1 = first_name1 + " " + last_name1;
            string name2 = first_name2 + " " + last_name2;
            //Calls function to find shortest path.
            vector<int> path = 
                Contact.shortest_path(Contact.get_id(name1),
                                      Contact.get_id(name2));
            
            if (path.size() == 0){
                cout << "None" << endl;
            }
            else{
                //Formatting output
                cout << "Distance: " << path.size() - 1 << endl;
                for (int i = 0; i<path.size(); i++){
                    cout << Contact.get_name(path[i]);
                    if (i < path.size() - 1){
                        cout << " -> ";
                    }
                }
                cout << endl;
            }
        }
        else if(input == 8){
            //Calls function to find groups within
            //the network.
            vector<vector<int> > set = Contact.groups();
            //Formatting output.
            for (int i = 0; i<set.size(); i++){
                cout << "Set " << i << " => ";
                for (int j = 0; j<set[i].size(); j++){
                    cout << Contact.get_name(set[i][j]);
                    if (j < set[i].size() - 1){
                        cout << ", ";
                    }
                }
                cout << endl;
            }
        }
        else if(input == 9){
            string firstname;
            string lastname;
            cin >> firstname >> lastname;
            string username = firstname + " " + lastname;
            
            int score = -1;
            //Calls function to suggest friends.
            vector<int> suggested = 
                Contact.suggest_friends(Contact.get_id(username),
                                        score);
            
            if (score == -1){
                cout << "None" << endl;
            }
            else{
                //Formatting output
                cout << "The suggested friend(s) is/are:"<< endl;
                for (int i = 0; i < suggested.size();
                     i++){
                    cout << '\t' << 
                        Contact.get_name(suggested[i]) 
                        << setw(30 - Contact.get_name(suggested[i]).length())
                        << "Score: "
                        << score << endl;                 
                }
            }
        }
        else{
            //If invalid input.
            continue_menu = false;
        }
    }
    return 0;
}
