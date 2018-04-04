#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Ask_sign_in : public ValidMessage{
public:
    string login;
    string password;
    string name;
    Ask_sign_in(string a, string b, string c){
        login = a;
        password = b;
        name = c;
        key = "Ask_sign_in";
    };
    
    Ask_sign_in(json obj){
        name = obj["name"].get<string>();
        login = obj["login"].get<string>();
        password = obj["password"].get<string>();
        key = obj["key"].get<string>();
    };
    json ConvertToJson(){
        json obj;
        obj["name"] = this->name;
        obj["login"] = this->login;
        obj["password"] = this->password;
        obj["key"] = this->key;
        return obj;
    };
};
