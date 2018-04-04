#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Ask_login_acc : public ValidMessage{
public:
    bool acc;
    string warning;
    int id;
    Ask_login_acc(bool b,string c, int d){
        id = d;
        acc = b;
        warning = c;
        key = "Ask_login_acc";
    };
    
    Ask_login_acc(json obj){
            acc = obj["acc"].get<bool>();
            warning = obj["warning"].get<string>();
            key = obj["key"].get<string>();
            id = obj["id"].get<int>();
        };
        json ConvertToJson(){
            json obj;
            obj["acc"] = this->acc;
            obj["warning"] = this ->warning;
            obj["key"] = this->key;
            obj["id"] = this->id;
            return obj;
        };
};
