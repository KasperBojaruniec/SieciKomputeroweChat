#pragma once 
#include <string>

class Client{
public:
    string name;
    int id;
    Client(string x, int y){
        name = x;
        id = y;
    };
    Client(){};
};
