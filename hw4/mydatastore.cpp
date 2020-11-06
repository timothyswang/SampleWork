#include <string>
#include <set>
#include <vector>
#include <map>
#include <iostream>
#include "mydatastore.h"

using namespace std;
//Constructor
MyDataStore::MyDataStore(){

}

MyDataStore::~MyDataStore() {

}
//Deconstructor
void MyDataStore::addProduct(Product* p){
	ProductList.push_back(p);
}

//Add user
void MyDataStore::addUser(User* u){
	string name = u->getName();
	UserList.insert(pair<string, User*>(name, u));
}
//Search
vector<Product*> MyDataStore::search(std::vector<std::string>& terms, int type){
	

	set<string> term_set;
	//Makes everything lowercase
	for(int i = 0; i< (int)(terms.size()); i++){
		
		for (int j = 0; j < (int)(terms[i].size()); j++){
        	if (terms[i][j] <= 'Z' && terms[i][j] >= 'A'){
           		terms[i][j] = terms[i][j] - ('Z' - 'z');
        	}
   		}

		term_set.insert(terms[i]);
	}

	vector<Product*> returnsearch;
	//AND search
	for(int i = 0; i < (int)(ProductList.size()); i++){
		
		Product* p = ProductList[i];

		set<string> keyword_list = p->keywords();

		if (type == 0){

			bool breaktest = false;
			for(typename set<string>::iterator it = term_set.begin(); it != term_set.end(); ++it){
				if (keyword_list.find(*it) == keyword_list.end()){
					breaktest = true;
				}
				if (breaktest == true){
					break;
				}
			}
			if (breaktest == false){
				returnsearch.push_back(p);
			}

			/*
			set<string> intersection = setIntersection(term_set, keyword_list);

			if (intersection.empty() == false){
				returnsearch.push_back(p);
			}
			*/

		}
		//OR search
		else if (type == 1){

			for(typename set<string>::iterator it2 = term_set.begin(); it2 != term_set.end(); ++it2){
				if (keyword_list.find(*it2) != keyword_list.end()){
					returnsearch.push_back(p);
					break;
				}
			}

		}
	}

	return returnsearch;

}
//ADD to cart
void MyDataStore::addCart(Product* p, string name){

	(Cart[name]).push_back(p);

}
//View cart function
void MyDataStore::viewCart(string name){

	for(int i = 0; i < (int)((Cart[name]).size()); i++){

		cout << "Item: " << i+1 << endl;

		cout << (Cart[name])[i]->displayString(); 

	}

}
//Buy cart function
void MyDataStore::buyCart(string name){

	for (vector<Product*>::iterator it = (Cart[name]).begin() ; it != (Cart[name]).end(); ++it){
		if ((*it)->getPrice() < (UserList[name])->getBalance()){
			(UserList[name])->deductAmount((*it)->getPrice());
			(*it)->subtractQty(1);
			(Cart[name]).erase(it);
			--it;
		}
	}

}
//Output to txt file.
void MyDataStore::dump(std::ostream& ofile){

	ofile << "<products>" << endl;

	for(int i = 0; i < (int) (ProductList.size()); i++){

		ProductList[i]->dump(ofile);

	}

	ofile << "<products>" << endl;

	ofile << "<users>" << endl;

	for (map<string, User*>::iterator it = UserList.begin() ; it != UserList.end(); ++it){
		it->second->dump(ofile);
	}

	ofile << "<users>" << endl;	



}
//Checks if username is valid.
bool MyDataStore::validUser(std::string name){

	if (UserList.find(name) != UserList.end()){
		return true;
	}
	else{
		return false;
	}
}