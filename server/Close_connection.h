#pragma once
#include <stdlib.h>
#include <string>
#include "json.hpp"
#include "ValidMessage.h"
using namespace std;
using json = nlohmann::json;

class Close_connection : public ValidMessage{
public:
    Close_connection(){
        key = "Close_connection";
    };
    
    Close_connection(json obj){
            key = obj["key"].get<string>();
        };
        json ConvertToJson(){
            json obj;
            obj["key"] = this->key;
            return obj;
        };
};