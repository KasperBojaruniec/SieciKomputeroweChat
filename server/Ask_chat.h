#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Ask_chat : public ValidMessage{
public:
    int id_from;
    int id_to;
    int len;
    Ask_chat(int a, int b, int c){
        id_to = a;
        id_from = b;
        len = c;
        key = "Ask_chat";
    };
    
    Ask_chat(json obj){
            id_to = obj["id_to"].get<int>();
            id_from = obj["id_from"].get<int>();
            key = obj["key"].get<string>();
            len = obj["len"].get<int>();
        };
        json ConvertToJson(){
            json obj;
            obj["id_to"] = this->id_to;
            obj["id_from"] = this->id_from;
            obj["key"] = this->key;
            obj["len"] = this->len;
            return obj;
        };
};
