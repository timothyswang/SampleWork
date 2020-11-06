#ifndef NETWORK_H
#define NETWORK_H
#include <string>
#include <vector>
#include "user.h"

class Network {
 public:
  Network();
  ~Network();
  int read_friends(const char *filename);
  int write_friends(const char *filename);
  void add_user(std::string username, int yr, int zipcode);
  int add_connection(std::string name1, std::string name2);
  int remove_connection(std::string name1, std::string name2);
  int get_id(std::string username);
    void print_users();
    void print_friends(std::string username);
    void bfs(int from);
    std::vector<int> shortest_path(int from, int to);
    std::vector<std::vector<int> > groups();
    std::vector<int> suggest_friends(int who, int& score);
    std::string get_name(int id);

 private:
    std::vector<User> list;

  
};


#endif
