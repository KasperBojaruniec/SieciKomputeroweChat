#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Ask_login : public ValidMessage{
public:
    string login;
    string password;
    Ask_login(string a,string b){
        login = a;
        password = b;
        key = "Ask_login";
    };
    
    Ask_login(json obj){
            login = obj["login"].get<string>();
            password = obj["password"].get<string>();
            key = obj["key"].get<string>();
        };
        json ConvertToJson(){
            json obj;
            obj["login"] = this->login;
            obj["password"] = this->password;
            obj["key"] = this->key;
            return obj;
        };
};
