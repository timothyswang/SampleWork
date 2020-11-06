#include <iostream>
#include <sstream>
#include <cctype>
#include <algorithm>
#include "util.h"

using namespace std;
std::string convToLower(std::string src)
{
    std::transform(src.begin(), src.end(), src.begin(), ::tolower);
    return src;
}

/** Complete the code to convert a string containing a rawWord
    to a set of words based on the criteria given in the assignment **/
std::set<std::string> parseStringToWords(string rawWords)
{

    for (int i = 0; i < (int)(rawWords.size()); i++){
        if (rawWords[i] <= 'Z' && rawWords[i] >= 'A'){
            rawWords[i] = rawWords[i] - ('Z' - 'z');
        }
    }

    set<std::string> return_list;

    std::vector<int> space_vector;

    space_vector.push_back(-1);

    for (int i = 0; i < (int)(rawWords.size()); i++){
        if (rawWords[i] == ' ' || rawWords[i] == ',' || rawWords[i] == '.' || rawWords[i] == '\'' || rawWords[i] == '?'){
            space_vector.push_back(i);
        }
    }

    space_vector.push_back(rawWords.size()+1);


    for (int j = 0; j < (int)(space_vector.size() - 1); j++){

        if (space_vector[j+1] - space_vector[j] > 2){
            string temp = rawWords.substr(space_vector[j] + 1, space_vector[j+1]-space_vector[j] -1 );
            return_list.insert(temp);
        }
    }

    return return_list;

}

/**************************************************
 * COMPLETED - You may use the following functions
 **************************************************/

// Used from http://stackoverflow.com/questions/216823/whats-the-best-way-to-trim-stdstring
// trim from start
std::string &ltrim(std::string &s) {
    s.erase(s.begin(), 
	    std::find_if(s.begin(), 
			 s.end(), 
			 std::not1(std::ptr_fun<int, int>(std::isspace))));
    return s;
}

// trim from end
std::string &rtrim(std::string &s) {
    s.erase(
	    std::find_if(s.rbegin(), 
			 s.rend(), 
			 std::not1(std::ptr_fun<int, int>(std::isspace))).base(), 
	    s.end());
    return s;
}

// trim from both ends
std::string &trim(std::string &s) {
    return ltrim(rtrim(s));
}
