#include <sstream>
#include <iomanip>
#include "book.h"
#include "util.h"

using namespace std;

Book::Book(const std::string category, const std::string name, double price, int qty, std::string isbn, 
        std::string author) : Product(category, name,  price, qty)
{
	isbn_ = isbn;
	author_ = author;

}

Book::~Book()
{

}


std::set<std::string> Book::keywords() const{

    std::string new_string = name_ + " " + isbn_ + " " + author_;

    return parseStringToWords(new_string);

}

std::string Book::displayString() const{

	std::string price_string = std::to_string(price_);

	std::string qty_string = std::to_string(qty_);

    return name_ + "\n" + "Author: " + author_ + " ISBN: " + isbn_ + "\n" + price_string + " " + qty_string + " left." + "\n";

}

void Book::dump(std::ostream& os) const{ // How do you do this?

    os << "book" << "\n" << name_ << "\n" <<  price_ << "\n" << qty_ << "\n" << isbn_ << "\n" << author_ << endl;

}
