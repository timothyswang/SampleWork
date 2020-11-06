#include <sstream>
#include <iomanip>
#include "movie.h"
#include "util.h" // Do you need to include other functions?

using namespace std;

Movie::Movie(const std::string category, const std::string name, double price, int qty, std::string genre, 
        std::string rating): Product(category, name,  price, qty)
{
	genre_ = genre;
	rating_ = rating;

}

Movie::~Movie()
{

}


std::set<std::string> Movie::keywords() const{

    std::string new_string = name_ + " " + genre_;

    return parseStringToWords(new_string);

}

std::string Movie::displayString() const{

	std::string price_string = std::to_string(price_);

	std::string qty_string = std::to_string(qty_);

    return name_ + "\n" + "Genre: " + genre_ + " Rating: " + rating_ + "\n" + price_string + " " + qty_string + " left." + "\n";

}

void Movie::dump(std::ostream& os) const{ // How do you do this?

    os << "book" << "\n" << name_ << "\n" << price_ << "\n" << qty_ << "\n" << genre_ << "\n" << rating_ << endl;

}
