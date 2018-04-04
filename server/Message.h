#pragma once
#include <string>
#include "json.hpp"

using namespace std;
using json = nlohmann::json;

class Message{
public :
    string text;
    double time;
    int id;
    Message(string x, double y, int z){
        text = x;
        time = y;
        id = z;
    };
    
    Message(string x, int z){
        text = x;
        id = z;
    };
};
