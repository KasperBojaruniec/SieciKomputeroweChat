#pragma once
#include <vector>
#include "Client.h"
#include <string>

class Client_me : public Client{
public:
    vector<Client*> contacts;
    string login,password;
    int fd;
    bool logged;
    Client_me(string a, int b, string d, string e):Client(a,b){
        login = d;
        password = e;
    };
    
    Client_me(){};
    void setFd(int fdS){
        fd = fdS;
    };
};
