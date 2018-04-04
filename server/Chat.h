#pragma once
#include <vector>
#include "Message.h"

using namespace std;

class Chat{
public:
    vector<Message*> chat;
    Chat(vector<Message*> x){
        chat = x;
    };
    Chat(){
    };

};
