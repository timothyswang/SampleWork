#ifndef MYDATASTORE_H
#define MYDATASTORE_H
#include <string>
#include <set>
#include <vector>
#include <map>
#include "product.h"
#include "user.h"
#include "datastore.h"
#include "util.h"

class MyDataStore : public DataStore {
public:

    MyDataStore();

    ~MyDataStore();

    /**
     * Adds a product to the data store
     */
    void addProduct(Product* p);

    /**
     * Adds a user to the data store
     */
    void addUser(User* u);

    /**
     * Performs a search of products whose keywords match the given "terms"
     *  type 0 = AND search (intersection of results for each term) while
     *  type 1 = OR search (union of results for each term)
     */
    std::vector<Product*> search(std::vector<std::string>& terms, int type);

    //Adding to cart
    void addCart(Product* p, std::string name);

    //Viewing cart
    void viewCart(std::string name);

    //Buying cart
    void buyCart(std::string name);

    //Output to file
    void dump(std::ostream& ofile);

    bool validUser(std::string name);



private:
    std::vector<Product*> ProductList;
    std::map<std::string, User*> UserList;
    std::map<std::string, std::vector<Product*> > Cart;

};

#endif