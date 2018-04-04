#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Send_message : public ValidMessage{
public:
    string text;
    int id_to;
    int id_from;
    int color;
    Send_message(string a, int b, int c, int d){
        text = a;
        id_to = b;
        id_from = c;
        color = d;
        key = "Send_message";
    };
    
    Send_message(json obj){
            text = obj["text"].get<string>();
            id_to = obj["id_to"].get<int>();
            id_from = obj["id_from"].get<int>();
            key = obj["key"].get<string>();
            color = obj["color"].get<int>();
        };
        json ConvertToJson(){
            json obj;
            obj["text"] = this->text;
            obj["id_to"] = this->id_to;
            obj["id_from"] = this->id_from;
            obj["key"] = this->key;
            obj["color"] = this->color;
            return obj;
        };
};
