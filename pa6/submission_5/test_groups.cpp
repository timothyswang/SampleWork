#include <vector>
#include <string>
#include <iostream>
#include <cstdlib>
#include "network.h"
using namespace std;

// compile: g++ {user,network,test_groups}.cpp -o test_groups

int main(int argc, char* argv[]) {
   if (argc != 2) {
      cerr << "Usage: ./test_groups network_file" << endl;
      return -1;
   }

   Network n;
   n.read_friends(argv[1]);
   vector< vector<int> > sg = n.groups();
   
   cout << sg.size() << " groups:\n";
   for (unsigned int i=0; i < sg.size(); i++) {
      for (unsigned int j=0; j < sg[i].size(); j++) {
         cout << " " << sg[i][j];
      }
      if (i != sg.size()-1) cout << ";\n";
   }

   return 0;
}
