#include <vector>
#include <string>
#include <iostream>
#include <cstdlib>
#include "network.h"
using namespace std;

// compile: g++ {user,network,test_shortest_path}.cpp -o test_shortest_path

int main(int argc, char* argv[]) {
   if (argc != 4) {
      cerr << "Usage: ./test_shortest_path network_file start finish" << endl;
      return -1;
   }
   int start = atoi(argv[2]);
   int finish = atoi(argv[3]);

   Network n;
   n.read_friends(argv[1]);
   vector<int> sp = n.shortest_path(start, finish);
   
   cout << "Path (length " << sp.size() << "):";
   for (unsigned int i=0; i < sp.size(); i++)
      cout << " " << sp[i];

   return 0;
}
