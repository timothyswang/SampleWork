#include <sstream>
#include <iomanip>
#include "clothing.h"
#include "util.h" // Do you need to include other functions?

using namespace std;

Clothing::Clothing(const std::string category, const std::string name, double price, int qty, std::string size, 
        std::string brand) : Product(category, name,  price, qty)
{
	size_ = size;
	brand_ = brand;

}

Clothing::~Clothing()
{

}


std::set<std::string> Clothing::keywords() const{

    std::string new_string = name_ + " " + brand_;

    return parseStringToWords(new_string);

}

std::string Clothing::displayString() const{

	std::string price_string = std::to_string(price_);

	std::string qty_string = std::to_string(qty_);

    return name_ + "\n" + "Size: " + size_ + " Brand: " + brand_ + "\n" + price_string + " " + qty_string + " left." + "\n";

}

void Clothing::dump(std::ostream& os) const{ // How do you do this?

    os << "book" << "\n" << name_ << "\n" << price_ << "\n" << qty_ << "\n" << size_ << "\n" << brand_ << endl;

}