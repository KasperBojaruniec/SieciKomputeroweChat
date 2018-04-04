#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Ask_sign_in_acc : public ValidMessage{
public:
    bool acc;
    string warning;
    Ask_sign_in_acc(bool a,string b){
        warning = b;
        acc = a;
        key = "Ask_sign_in_acc";
    };
    Ask_sign_in_acc(json obj){
        acc = obj["acc"].get<bool>();
        warning = obj["warning"].get<string>();
        key = obj["key"].get<string>();
    };
    json ConvertToJson(){
        json obj;
        obj["acc"] = this->acc;
        obj["warning"] = this->warning;
        obj["key"] = this->key;
        return obj;
    };
};