#ifndef USER_H
#define USER_H
#include <vector>
#include <string>
#include <cstdlib>

class User {
 public:
    User(int input_id, std::string intput_name, 
         int input_year, int input_zip, 
         std::vector<int> input_friends,
        int input_depth,
           int input_predecessor);
    ~User();
    void add_friend(int id);
    void delete_friend(int id);
    int get_id() const;
    std::string get_name() const;
    int get_year() const;
    int get_zip() const;
    std::vector<int>* get_friends();
    int depth;
    int predecessor;
    int group;
    int ind_score;

 private:
    int id;
    std::string name;
    int year;
    int zip;
    std::vector<int> friends;

};


#endif
