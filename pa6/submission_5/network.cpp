#include "network.h"
#include <cstddef>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <fstream>
#include <vector>
#include <deque>

using namespace std;
//Constructor/Destructor
Network::Network(){
}

Network::~Network(){
}
//Creates new User object and adds it to the network.
void Network::add_user(string username, int yr, int zipcode){
    int init_depth = -1;
    int init_predecessor = -1;
    vector<int> friends_index;
    User input(list.size(), username, yr, zipcode, friends_index,
              init_depth, init_predecessor);
    list.push_back(input);
}

void Network::print_users(){
    cout << "ID" << setw(9) << "Name" << setw(30) << "Year" 
        << setw(10) << "Zip" << endl;
    for (int i = 0; i < 75; i++){
        cout << "=";
    }
    cout << endl;
    for (int j=0; j<list.size(); j++){
        if (list[j].get_id()<10){
            //Use of setw is modified depending on the
            //lenght of the name to ensure straight
            //columns.
            cout << list[j].get_id() << "." 
                << setw(8 -3 +list[j].get_name().length()) 
                << list[j].get_name() 
                << setw(30 - (list[j].get_name().length()-4)) 
                << list[j].get_year() 
                << setw(12) << list[j].get_zip() << endl;
        }
        else{
            //If index is greater than 1 digit, then the
            //output has to be modified such that the
            //columns still line up.
            // 8-3 becomes 8-4
            cout << list[j].get_id() << "." 
                << setw(8 -4 +list[j].get_name().length()) 
                << list[j].get_name() 
                << setw(30 - (list[j].get_name().length()-4)) 
                << list[j].get_year() 
                << setw(12) << list[j].get_zip() << endl;
        }
    }
}
//Returns id
int Network::get_id(string username){
    int index = 0;
    for (int i=0; i<list.size();i++){
        if(list[i].get_name() == username){
            index = i;
        }
    }
    return index;
}

int Network::add_connection(string name1, string name2){
    bool check_name1 = false;
    bool check_name2 = false;
    int name1_id = 0;
    int name2_id = 0;
    int index1 = 0;
    int index2 = 0;
    //Checks to make sure both names actually exist.
    for (int i=0; i<list.size();i++){
        if(list[i].get_name() == name1){
            check_name1 = true;
            name1_id = get_id(name1);
            index1 = i;
        }
        if(list[i].get_name() == name2){
            check_name2 = true;
            name2_id = get_id(name2);
            index2 = i;
        }
    }
    if (check_name1 == false || check_name2 == false){
        return -1;
    }
    //Adds friend connection.
    list[index1].add_friend(name2_id);
    list[index2].add_friend(name1_id);
    
    return 0;
            
}

int Network::remove_connection(string name1, string name2){
    bool check_name1 = false;
    bool check_name2 = false;
    int name1_id = 0;
    int name2_id = 0;
    int index1 = 0;
    int index2 = 0;
    //Checks to make sure that names exist.
    for (int i=0; i<list.size();i++){
        if(list[i].get_name() == name1){
            check_name1 = true;
            name1_id = get_id(name1);
            index1 = i;
        }
        if(list[i].get_name() == name2){
            check_name2 = true;
            name2_id = get_id(name2);
            index2 = i;
        }
    }
    if (check_name1 == false || check_name2 == false){
        return -1;
    }
    //Checks to make sure they are each other's friend
    //already.
    bool check_friend1 = false;
    bool check_friend2 = false;
    vector<int>* f1_in_f2 = list[index2].get_friends();
    vector<int>* f2_in_f1 = list[index1].get_friends();
    for (int i = 0; i < (*f1_in_f2).size(); i++){
        if((*f1_in_f2)[i] == index1){
            check_friend1 = true;
        }
        if((*f2_in_f1)[i] == index2){
            check_friend2 = true;
        }
    }
    if (check_friend1 == false || check_friend2 == false){
        return -1;
    }
    
    list[index1].delete_friend(name2_id);
    list[index2].delete_friend(name1_id);
    return 0;
}
    

void Network::print_friends(string username){
    //Checks if the name is in the network
    bool check_name = false;
    int index = 0;
    for (int i=0; i<list.size();i++){
        if(list[i].get_name() == username){
            check_name = true;
            index = i;
        }
    }
    if (check_name == false){
        cout << "Name is invalid." << endl;
        return;
    }
    
    vector<int>* friends_list = list[index].get_friends();
    
    cout << "ID" << setw(9) << "Name" 
        << setw(30) << "Year" << setw(10) << "Zip" << endl;
    for (int i = 0; i < 75; i++){
        cout << "=";
    }
    cout << endl;
    
    for (int j=0; j<(*friends_list).size(); j++){
        int friend_index = (*friends_list)[j];

        if (friend_index < 10){
            cout << friend_index << "." 
                << setw(8 -3 + list[friend_index].get_name().length()) 
                << list[friend_index].get_name() 
                << setw(30 - (list[friend_index].get_name().length()-4)) 
                << list[friend_index].get_year() << setw(12) 
                << list[friend_index].get_zip() << endl;
        }
        else{
            cout << friend_index << "." 
                << setw(8 -4 + list[friend_index].get_name().length()) 
                << list[friend_index].get_name() 
                << setw(30 - (list[friend_index].get_name().length()-4)) 
                << list[friend_index].get_year() << setw(12) 
                << list[friend_index].get_zip() << endl;
        }
    }
}

int Network::read_friends(const char *filename){
    ifstream ifile(filename);
    
    string myfile = "";
    stringstream ss;
    //Checks how many users are in the file.
    getline(ifile, myfile);
    ss << myfile;
    int counter = 0;
    ss >> counter;
    if (ss.fail()){
        return -1;
    }
    
    ss.clear();
    ss.str("");
    //For each user, import their information.
    for (int i = 0; i<counter; i++){

        getline(ifile, myfile);
        ss << myfile;
        int index = 0;
        ss >> index;
        
        ss.clear();
        ss.str("");
        
        getline(ifile, myfile);
        ss << myfile;
        string firstname = "";
        string lastname = "";
        ss >> firstname >> lastname;
        string name = firstname + " " + lastname;
        
        ss.clear();
        ss.str("");
        
        getline(ifile, myfile);
        ss << myfile;
        int year = 0;
        ss >> year;
        
        ss.clear();
        ss.str("");
        
        getline(ifile, myfile);
        ss << myfile;
        int zip = 0;
        ss >> zip;
        
        ss.clear();
        ss.str("");
        //Add user.
        add_user(name, year, zip);
        //Update their friend list.
        getline(ifile, myfile);
        ss << myfile;
        int x;
        while (ss>>x){
            //Adds friends until there are no longer
            //any more friends to add.
            (*(list[i].get_friends())).push_back(x);
        }
        
        ss.clear();
        ss.str("");
   
    }

    return 0;
}

int Network::write_friends(const char *filename){
    ofstream ofile(filename);
    //Find the size of the user list in the network.
    ofile << list.size() << endl;
    //Outputs information for each user.
    for (int i = 0; i < list.size(); i++){
        ofile << list[i].get_id() << endl;
        ofile << '\t' << list[i].get_name() << endl;
        ofile << '\t' << list[i].get_year() << endl;
        ofile << '\t' << list[i].get_zip() << endl;
        
        ofile << '\t';
        for (int j = 0; j < (*(list[i].get_friends())).size(); j++){
            ofile << (*(list[i].get_friends()))[j] << " ";
        }
        ofile << endl;
    }
    return 0;
}
//Peforms breadth first search
//on the network from a certain point.
//Notes on each "vertices" (user) its
//"depth" and its "predecessor" relative
//to the start point.
void Network::bfs(int from){
    
    list[from].depth = 0;
    //Creates temporary deque
    deque<int> b;
    //Adds the starting point to the deque
    b.push_back(from);
    //Checks to make sure there are no users left in the
    //deque
    while (b.size() > 0){
        int front = b.front();
        b.pop_front();
        int current_depth = list[front].depth;
        //Checks the users who are friends with the front of the deque.
        for(int i = 0; i<(*(list[front].get_friends())).size(); i++){
            //Makes sure the user has not already been visited
            if (list[(*(list[front].get_friends()))[i]].depth == -1){ 
                list[(*(list[front].get_friends()))[i]].depth = current_depth+1;
                list[(*(list[front].get_friends()))[i]].predecessor 
                    = list[front].get_id();
                //Gets rid of the front of the queue.
                b.push_back(list[(*(list[front].get_friends()))[i]].get_id());
            }
        }
    }
}
//Helper get_name function
string Network::get_name(int id){
    return list[id].get_name();
}
//Finds the shortest path between two users.
vector<int> Network::shortest_path(int from, int to){
    //Resets the depth/predecessor variables
    for (int i = 0; i<list.size(); i++){
        list[i].depth = -1;
        list[i].predecessor = -1;
    }
    //Call BFS
    bfs(from);
    vector<int> path;
    
    vector<int> path_final;
    //If the two users are not connected in the
    //network graph.
    if(list[to].depth == -1){
        return path;
    }
    else{
        path.push_back(to);
        int curr = path.front();
        while (curr != from){
            //Retraces using predecessor
            //back to the start.
            curr = list[curr].predecessor;
            path.push_back(curr);
        }

        for (int i = path.size() -1; i >= 0; i--) {
            path_final.push_back(path[i]);
        }
        
    }
    
    return path_final;
}
//Finds network groups.
vector<vector<int> > Network::groups(){
    //Resets variable
    for (int i = 0; i<list.size(); i++){
        list[i].depth = -1;
        list[i].predecessor = -1;
        list[i].group = -1;
    }
    
    int group_id = 0;
    //Checks to make sure there are new groups
    //that need to be accounted for.
    bool new_group = false;
    for(int i = 0; i<list.size(); i++){
        if (list[i].group == -1){
            new_group = true;
        }
        else{
            new_group = false;
        }
    }
    
    while(new_group == true){
        //Finds the lowest id that is not a
        //part of an accounted for group.
        int lowest_id = list.size()-1;
        for (int i = list.size() -1; i>=0; i--){
            if (list[i].group == -1){
                if (i < lowest_id){
                    lowest_id = i;
                }
            }
        }
        
        bfs(lowest_id);
        //For each new group, assign the users a new
        //group id
        for (int i = 0; i <list.size(); i++){
            if (list[i].group == -1 && list[i].depth > -1){
                list[i].group = group_id;
            }
        }
        group_id++;
        //Checks again for new, unaccounted for
        //groups
        for(int i = 0; i<list.size(); i++){
            if (list[i].group == -1){
                new_group = true;
                break;
            }
            else{
                new_group = false;
            }
        }
    }
    vector<vector<int> > v(group_id);
    //Fill in the vector.
    for(int i = 0; i < list.size(); i++){
        v[list[i].group].push_back(i);
    }
    return v;
    
}
//This function suggests friends.
vector<int> Network::suggest_friends(int who, int& score){
    //Initializes variables
    for (int i = 0; i<list.size(); i++){
        list[i].depth = -1;
        list[i].predecessor = -1;
        list[i].ind_score = 0;
    }
    //Checks users who are of a depth 2
    //away from the user.
    bfs(who);
    vector<int> suggested;
    for (int i = 0; i < list.size(); i++){
        if (list[i].depth == 2){
            suggested.push_back(i);
        }
    }
    //Checks for matches between the friend list
    //of the target person and the people
    //of a depth of 2 away from the target person.
    for (int i = 0; i < suggested.size(); i++){
        for (int j = 0; j< (*(list[suggested[i]].get_friends())).size(); j++){
            for (int k = 0; k < (*(list[who].get_friends())).size(); k++){
                if ( (*(list[suggested[i]].get_friends()))[j] == 
                    (*(list[who].get_friends()))[k]){
                    list[suggested[i]].ind_score++;
                }
            }
        }
    }
    int curr = 0;
    for (int i = 0; i < suggested.size(); i++){
        if (curr <= list[suggested[i]].ind_score){
            curr = list[suggested[i]].ind_score;
        }
    }
    //If a person does not have the highest score,
    //get rid of that person from the vector.
    for (int i = 0; i < suggested.size(); i++){
        if (curr > list[suggested[i]].ind_score){
            suggested.erase(suggested.begin() + i);
            i--;
        }
    }
    if (curr == 0){
        score = -1;
    }
    else{
        score = curr;
    }
    return suggested;
}

