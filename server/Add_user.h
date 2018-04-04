#pragma once
#include <stdlib.h>
#include <string>
#include <typeinfo>
#include "json.hpp"
#include "ValidMessage.h"

using namespace std;
using json = nlohmann::json;

class Add_user : public ValidMessage{
    
    public:
        int id_to;
        int id_from;
        
        Add_user(int x, int y){
            id_to = x;
            id_from = y;
            key = "Add_user";
        };
        Add_user(json obj){
            id_to = obj["id_to"].get<int>();
            id_from = obj["id_from"].get<int>();
            key = obj["key"].get<string>();
        };
        json ConvertToJson(){
            json obj;
            obj["id_to"] = this->id_to;
            obj["id_from"] = this->id_from;
            obj["key"] = this->key;
            return obj;
        };
        
};
