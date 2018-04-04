#pragma once
#include <vector>
#include <map>
#include <iostream>
#include <cassert>
#include "Chat.h"
using namespace std;
string endLineIndicator = "";
string startLineIndicator = "";
class Chats{
public:
    map<vector<int>, Chat*> chats;
    Chats(){};
    
    
    void addNewChat(vector<int> vec){
        chats[vec] = new Chat();
    }
    
    void loadChats(const char* fileName){
        FILE * plik = fopen(fileName, "rt");
        int id_1 = 0;
        int id_2 = 0;
        int id;
        char text[256];
        char check[3];
        Chat* chat = new Chat();
        if (plik != NULL) {
            while(fscanf(plik, "%s\n", (char*)&check) != EOF)
{
                if(strcmp(check, "/") == 0){
                    fscanf(plik, "%d %d\n", &id_1, &id_2);
                    chat = new Chat();
                }
                else if(strncmp(check, "{", 1) == 0){
                    string str = "";
                    fscanf(plik, "%d\n", &id);
                    fscanf(plik, "%s\n", (char*)&text);
                    while(strncmp(text, "}", 1) != 0){
                        str += " ";
                        str.append(text);
                        fscanf(plik, "%s\n", (char*)&text);
                    }
                    Message* mes = new Message(str, id);
                    chat->chat.push_back(mes);
                }
                else{
                    vector<int> vec;
                    vec.push_back(id_1);
                    vec.push_back(id_2);
                    chats[vec] = chat;
                }
            }
        }
        fclose(plik);
    };
    
    void saveChats(const char* fileName){
        FILE * plik = fopen(fileName, "w+t");
        string strTmp = "";
        string str = "";
        if (plik != NULL){
            for (std::map<vector<int>, Chat*>::iterator it=chats.begin(); it!=chats.end(); ++it){
                fprintf(plik, "/\n");
                fprintf(plik, "%d %d\n", it->first.at(0), it->first.at(1));
                for(int i = 0; i < (int)it->second->chat.size(); i++){
                    fprintf(plik, "{\n");
                    fprintf(plik, "%d\n", it -> second -> chat.at(i) -> id);
                    str = it->second->chat.at(i)->text;
                    strTmp = "";
                    for(int j = 0; j < (int)str.length(); j++){
                        if(str.at(j) == ' '){
                            fprintf(plik, "%s \n", strTmp.c_str());
                            strTmp = "";
                        }
                        else{
                            strTmp += str.at(j);
                        }
                    }
                    fprintf(plik, "%s \n", strTmp.c_str());
                    fprintf(plik, "}\n");
                }
                fprintf(plik, "END\n");
            }
        }
        fclose(plik);
    };
};
