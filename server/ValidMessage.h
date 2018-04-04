#pragma once
#include <stdlib.h>
#include <string>
#include <typeinfo>
#include "json.hpp"

using namespace std;
using json = nlohmann::json;

class ValidMessage{
    public :
        string key;
        ValidMessage(){};
};


