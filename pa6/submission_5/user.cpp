#include "user.h"

#include <cstddef>

using namespace std;
//Constructor initializes a User object and
//fills in a vector that represents a list
//of their friends
User::User(int input_id, string input_name, 
           int input_year, int input_zip, 
           vector<int> input_friends,
          int input_depth,
           int input_predecessor){
    id = input_id;
    name = input_name;
    year = input_year;
    zip = input_zip;
    depth = input_depth;
    predecessor = input_predecessor;
    for (int i= 0; i<input_friends.size(); i++){
        friends[i] = input_friends[i];
    }
}

User::~User(){
}
//This function adds a friend to the user's
//friend list.
void User::add_friend(int id){
    for (int i =0; i< friends.size(); i++){
        if (friends[i] == id){
            return;
        }
    }
    friends.push_back(id);
}
// This function removes a friend from the 
//user's friend list.
void User::delete_friend(int id){
    for (int i =0; i< friends.size(); i++){
        if (friends[i] == id){
            friends.erase(friends.begin() + i);
            return;
        }
    }
}
//Accessors:
int User::get_id() const{
    return id;
}

string User::get_name() const{
    return name;
}

int User::get_year() const{
    return year;
}

int User::get_zip() const{
    return zip;
}

vector<int>* User::get_friends(){
    return &friends;
}
