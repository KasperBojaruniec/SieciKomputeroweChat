#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Add_user_acc : public ValidMessage{
public:
    int id_from;
    int id_to;
    bool acc;
    string name;
    Add_user_acc(int id_fromC,int id_toC, bool accC, string nameC){
        id_from = id_fromC;
        id_to = id_toC;
        acc = accC;
        name = nameC;
        key = "Add_user_acc";
    };
    Add_user_acc(json obj){
        acc = obj["acc"].get<bool>();    
        name = obj["name"].get<string>();
        id_from = obj["id_from"].get<int>();
        id_to = obj["id_to"].get<int>();
        key = obj["key"].get<string>();
    };
    json ConvertToJson(){
        json obj;
        obj["acc"] = this->acc;
        obj["name"] = this->name;
        obj["id_from"] = this->id_from;
        obj["id_to"] = this->id_to;
        obj["key"] = this->key;
        return obj;
    };
};
