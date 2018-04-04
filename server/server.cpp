#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <map>
#include <stddef.h>
#include <netdb.h>
#include "stdio.h"
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include <pthread.h>
#include "json.hpp"
#include <string>
#include <iostream>
#include <vector>
#include <queue>
#include "header.h"

#define CHAT_DATA "ChatData"
#define CHAT_DATA_TEST "ChatDataTest"
#define OK_EXIT 1
#define PORT 8888
#define CLIENTS_DATA "Data"
#define CLIENTS_DATA_TEST "DataTest"

using namespace std;
using json = nlohmann::json;
double ids = 0;

json string_to_json(string data){
    return json::parse(string(data));
}

void append(char* s, char c) {
        int len = strlen(s);
        s[len] = c;
        s[len+1] = '\0';
}

/*
* ----------------------------------------
 FUNKCJA OBSLUGUJACA WYSYLANIE INFORMACJI DO KLIENTOW
-------------------------------------------
*/
void writeToClient(int socket, char* message){
    int i = 0;
    int j = 0;
    int len = strlen(message);
    printf("BUFFOR SEND: \n");
    while((i = write(socket, message + j, len)) > 0){
        printf("%s", message +j);
        len -= i;
        j+= i;
    }
}


/*
* ----------------------------------------
 ODCZYT INFORMACJI O KLIENTACH Z PLIKU
-------------------------------------------
*/
void loadClients(const char* fileName, map<string, Client_me*> &clientsLogin, map<int, Client_me*> &clientsID){
        FILE * plik = fopen(fileName, "rt");
        int colCount, idd;
        char check[256];
        map<int, vector<int>> col;
        if (plik != NULL) {
            while(fscanf(plik, "%s\n", (char*)&check) != EOF){
                char* name;
                name = check;
                char login[128];
                char password[128];
                int id;
                fscanf(plik, "%s\n", (char*)&login);
                fscanf(plik, "%s\n", (char*)&password);
                fscanf(plik, "%d\n", &id);
                fscanf(plik, "%d\n", &colCount);
                Client_me* client = new Client_me(name, id, login, password);
                printf("NAME: %s, LOGIN: %s, PASSWORD: %s, ID: %d, COUNT: %d \n", name, login, password, id, colCount);
                for (int i = 0; i < colCount; i++){
                    fscanf(plik, "%d\n", &idd);
                    col[client -> id].push_back(idd);
                }
                //clientsLogin[client -> login] = client;
                clientsID[client -> id] = client;
                if(id > ids) ids = id;
            }
            for (map<int, Client_me*>::iterator it=clientsID.begin(); it!=clientsID.end(); ++it){
                for (int a = 0; a < (int)col[it -> first].size(); a++){
                    it -> second -> contacts.push_back(clientsID[col[it -> first].at(a)]);
                    //clientsLogin[it -> second -> login] -> contacts.push_back(clientsID[col[it -> first].at(a)]);
                }
            }
            for (map<int, Client_me*>::iterator it=clientsID.begin(); it!=clientsID.end(); ++it)
                for(int a = 0; a < (int)it -> second -> contacts.size(); a++)
                    for(int b = 0; b < (int)it -> second -> contacts.size(); b++)
                        if((it -> second -> contacts.at(a) -> id == it -> second -> contacts.at(b) -> id) && (a != b))
                            it -> second -> contacts.erase(it -> second -> contacts.begin() + b);
            //for (map<string, Client_me*>::iterator it=clientsLogin.begin(); it!=clientsLogin.end(); ++it)
              //  for(int a = 0; a < it -> second -> contacts.size(); a++)
                //    for(int b = 0; b < it -> second -> contacts.size(); b++)
                  //      if((it -> second -> contacts.at(a) -> id == it -> second -> contacts.at(b) -> id) && (a != b))
                    //        it -> second -> contacts.erase(it -> second -> contacts.begin() + b);
            ids++;
        }
        fclose(plik);
}


/*
* ----------------------------------------
 ZAPIS INFORMACJI O KLIENTACH DO PLIKU
-------------------------------------------
*/
void saveClients(const char* fileName, map<int, Client_me*> &clientsID){
    FILE * plik = fopen(fileName, "w+t");
    if (plik != NULL){
        for (map<int, Client_me*>::iterator it=clientsID.begin(); it!=clientsID.end(); ++it){
            fprintf(plik, "%s\n", (it -> second -> name).c_str());
            fprintf(plik, "%s\n", it -> second -> login.c_str());
            fprintf(plik, "%s\n", it -> second -> password.c_str());
            fprintf(plik, "%d\n", it -> second -> id);
            fprintf(plik, "%d\n", (int)it -> second -> contacts.size());
            for(int i = 0; i < (int)it -> second -> contacts.size(); i++){
                fprintf(plik, "%d\n", it -> second -> contacts.at(i) -> id);
            }
        }
    }
    fclose(plik);
}

void startServer(){
    /*
    * ----------------------------------------
     - TWORZENIE STRUKTUR DO KOMUNIKACJU Z UZYTKOWNIKAMI
     - ODCZYT DANYCH KLIENTOW Z PLIKU
    -------------------------------------------
    */
    Chats * mainChats = new Chats();
    mainChats->loadChats(CHAT_DATA);
    
    vector<int> ask_login_acc_fd_vector;
    vector<Ask_login_acc*> ask_login_acc_vector;
    
    vector<int> add_user_acc_fd_vector;
    vector<Add_user_acc*> add_user_acc_vector;
    
    vector<int> ask_sign_in_acc_fd_vector;
    vector<Ask_sign_in_acc*> ask_sign_in_acc_vector;
    
    vector<int> send_message_fd_vector;
    vector<Send_message*> send_message_vector;
    
    map<string, Client_me*> clientsLogin;
    map<int, Client_me*> clientsId;
    loadClients(CLIENTS_DATA, clientsLogin, clientsId);
    
    /*
    * ----------------------------------------
     TWORZENIE ZMIENNYCH
    -------------------------------------------
    */
    int id_larger;
    int id_smaller;
    vector<int> posTmp;
    int fd = socket(PF_INET,SOCK_STREAM,0),i,on=1,cfd,fdmax,fda,rc;
    char buffor[128] = "";
    fd_set tmpRmask, tmpWmask,  rmask, wmask;
    static struct timeval timeout;
    socklen_t slt;
    string buf;
    struct sockaddr_in saddr,caddr;
    
    setsockopt(fd,SOL_SOCKET,SO_REUSEADDR,(char*)&on,sizeof(on));
    memset(&saddr,0,sizeof(saddr));
    saddr.sin_family = PF_INET;
    saddr.sin_port = htons(PORT);
    saddr.sin_addr.s_addr = INADDR_ANY;
    
    bind(fd,(struct sockaddr*)&saddr,sizeof(saddr));
    listen(fd,1);
    FD_ZERO(&tmpRmask);
    FD_ZERO(&tmpWmask);
    FD_ZERO(&rmask);
    FD_ZERO(&wmask);
    fdmax=fd;
    /*
    * ----------------------------------------
     SPRAWDZA CZY KLIENT GOTOWY DO ODCZYTU/ZAPISU
    -------------------------------------------
    */
    while(1){
       rmask = tmpRmask;
       wmask = tmpWmask;
       FD_SET(fd,&rmask);
       timeout.tv_sec = 5*60;
       timeout.tv_usec = 0;
       rc = select(fdmax +1, &rmask, &wmask, (fd_set*)0, &timeout);
       if(rc == 0){
           printf("timed out\n");
           continue;
       }
       fda = rc;
       /*
        * ----------------------------------------
       NAWIAZUJE NOWE POLACZENIE
       -------------------------------------------
       */
       if(FD_ISSET(fd, &rmask)){
           fda -= 1;
           slt = sizeof(caddr);
           cfd = accept(fd, (struct sockaddr *)&caddr,&slt);
           printf("new connection read: %s\n",inet_ntoa((struct in_addr)caddr.sin_addr));
           FD_SET(cfd,&tmpRmask);
           if(cfd>fdmax)fdmax = cfd;
       }
       /*
        * ----------------------------------------
       ODCZYTYWANIE I WYSYLANIE DANYCH DO KLIENTOW
       -------------------------------------------
       */
       for(i = fd; i <= fdmax && fda > 0; i++){
           /*
            * ----------------------------------------
            ODCZYTYWANIE DANYCH OD KLIENTOW
            -------------------------------------------
            */
            if(FD_ISSET(i, &rmask)){
                int signI = 0;
                char bufforTmp[1];
                read(i, bufforTmp, 1);
                while(bufforTmp[0] != '\n'){
                    buffor[signI++] = bufforTmp[0];
                    read(i, bufforTmp, 1);
                }
                puts("BUFFOR READ:");
                puts(buffor);
                string msg = buffor;
                FD_CLR(i,&tmpRmask);
                fda -= 1;
                json jObject;
                string key = "";
                try{
                    jObject = string_to_json(buffor);
                    key = jObject["key"].get<string>();
                }
                catch(exception e){
                    printf("Nie udalo sie sparsowac buffora: %s\n", buffor);
                    FD_CLR(i, &tmpRmask);
                    FD_CLR(i, &tmpWmask);
                    FD_CLR(i, &wmask);
                    FD_CLR(i, &rmask);
                    close(i);
                }
                /*
                * ----------------------------------------
                OBSŁUGA OTRZYMANYCH WIADOMOSCI OD KLIENTA
                -------------------------------------------
                */
                /* ----------------------------------------
                ZAMYKANIE POLACZENIA Z DANYM KLIENTEM
                -------------------------------------------
                */
                if(key == "Close_connection"){
                    printf("Closing socket: %d...\n", i);
                    FD_CLR(i, &tmpRmask);
                    FD_CLR(i, &tmpWmask);
                    FD_CLR(i, &wmask);
                    FD_CLR(i, &rmask);
                    mainChats -> saveChats(CHAT_DATA);
                    saveClients(CLIENTS_DATA, clientsId);
                    close(i);
                    
                    printf("Socket closed.");
                }
                /*
                * ----------------------------------------
                DODAWANIE KONTAKTOW
                -------------------------------------------
                */
                else if(key == "Add_user"){
                    Add_user* msg = new Add_user(jObject);
                    if(clientsId[msg->id_to] != nullptr){
                        if(msg->id_from > msg->id_to){
                            id_smaller = msg->id_to;
                            id_larger = msg->id_from;
                        }
                        else{
                            id_smaller = msg->id_from;
                            id_larger = msg->id_to;
                        }
                        posTmp.clear();
                        posTmp.push_back(id_smaller);
                        posTmp.push_back(id_larger);
                        //printf("ID_SMALLER: %d, ID_LARGER: %d \n", posTmp.at(0), posTmp.at(1));
                        if(mainChats -> chats[posTmp] == nullptr){
                            mainChats -> addNewChat(posTmp);
                            clientsId[msg->id_from] -> contacts.push_back(clientsId[msg->id_to]);
                            clientsId[msg->id_to] -> contacts.push_back(clientsId[msg->id_from]);
                            //clientsLogin[clientsId[msg->id_to] -> login] -> contacts.push_back(clientsId[msg->id_from]);
                            //clientsLogin[clientsId[msg->id_from] -> login] -> contacts.push_back(clientsId[msg->id_to]);
                            add_user_acc_fd_vector.push_back(i);
                            add_user_acc_vector.push_back(new Add_user_acc(msg->id_to, msg->id_from, true, clientsId[msg->id_to]->name));
                        }
                        else{
                            add_user_acc_fd_vector.push_back(i);
                            add_user_acc_vector.push_back(new Add_user_acc(msg->id_to, msg->id_from, true, clientsId[msg->id_to]->name));
                        }
                        FD_SET(i,&tmpWmask);
                        FD_CLR(i,&tmpRmask);
                    }
                    else{
                        add_user_acc_fd_vector.push_back(i);
                        add_user_acc_vector.push_back(new Add_user_acc(msg->id_to, msg->id_from, false, ""));
                        FD_SET(i,&tmpWmask);
                        FD_CLR(i,&tmpRmask);
                    }
                }
                /*
                * ----------------------------------------
                LOGOWANIE
                -------------------------------------------
                */
                else if(key == "Ask_login"){
                    Ask_login* msg = new Ask_login(jObject);
                    if(clientsLogin[msg->login] != nullptr){
                        if(clientsLogin[msg->login]->password == msg->password){
                            clientsLogin[msg->login]->logged = true;
                            FD_SET(i,&tmpWmask);
                            FD_CLR(i,&tmpRmask);
                            for(int n = 0; n < (int)clientsLogin[msg->login]->contacts.size(); n++){
                                add_user_acc_fd_vector.push_back(i);
                                add_user_acc_vector.push_back(new Add_user_acc(clientsLogin[msg->login]->contacts.at(n)->id, clientsLogin[msg->login] -> id, true, clientsLogin[msg->login]->contacts.at(n)->name));
                            }
                            ask_login_acc_fd_vector.push_back(i);
                            ask_login_acc_vector.push_back(new Ask_login_acc(true, "Pomyślnie zalogowano", clientsLogin[msg->login] -> id));
                        }
                    }
                    else{
                        FD_SET(i,&tmpWmask);
                        FD_CLR(i,&tmpRmask);
                        ask_login_acc_fd_vector.push_back(i);
                        ask_login_acc_vector.push_back(new Ask_login_acc(false, "Zły login lub hasło", -1));
                    }
                }
                /*
                * ----------------------------------------
                ZAPYTANIE O NIEODCZYTANE WIADOMOSCI
                -------------------------------------------
                */
                else if(key == "Ask_chat"){
                    Ask_chat* msg = new Ask_chat(jObject);
                    if(msg->id_from > msg->id_to){
                        id_smaller = msg->id_to;
                        id_larger = msg->id_from;
                    }
                    else{
                        id_smaller = msg->id_from;
                        id_larger = msg->id_to;
                    }
                    posTmp.clear();
                    posTmp.push_back(id_smaller);
                    posTmp.push_back(id_larger);
                    if(msg -> len == (int)mainChats -> chats[posTmp] -> chat.size()){
                        send_message_fd_vector.push_back(i);
                        send_message_vector.push_back(new Send_message("\n\n\n", -1, -1, 0));
                    }
                    for (int j = msg->len; j < (int)mainChats -> chats[posTmp] -> chat.size(); j++){
                        send_message_fd_vector.push_back(i);
                        if(id_smaller == mainChats -> chats[posTmp] -> chat.at(j)->id)
                            send_message_vector.push_back(new Send_message(mainChats -> chats[posTmp] -> chat.at(j) -> text, id_larger, id_smaller, 0));
                        else
                            send_message_vector.push_back(new Send_message(mainChats -> chats[posTmp] -> chat.at(j) -> text, id_smaller, id_larger, 0));
                    }
                    FD_SET(i,&tmpWmask);
                    FD_CLR(i,&tmpRmask);
                }
                /*
                * ----------------------------------------
                ODCZYTYWANIE WIADOMOSCI OD KLIENTOW I ICH ZAPIS
                -------------------------------------------
                */
                else if(key == "Send_message"){
                    Send_message* msg = new Send_message(jObject);
                    int id_smaller;
                    int id_larger;
                    if(msg->id_from > msg->id_to){
                        id_smaller = msg->id_to;
                        id_larger = msg->id_from;
                    }
                    else{
                        id_smaller = msg->id_from;
                        id_larger = msg->id_to;
                    }
                    vector<int> posTmp;
                    posTmp.push_back(id_smaller);
                    posTmp.push_back(id_larger);
                    mainChats -> chats[posTmp] -> chat.push_back(new Message(msg->text, msg->id_from));
                    FD_SET(i,&tmpRmask);
                }
                /*
                * ----------------------------------------
                REJESTRACJA UZYTKOWNIKA
                -------------------------------------------
                */
                else if(key == "Ask_sign_in"){
                    Ask_sign_in* msg = new Ask_sign_in(jObject);
                    if(clientsLogin[msg->login] == nullptr){
                        Client_me* newClient = new Client_me(msg->login, ids,msg->login, msg->password);
                        clientsLogin[msg->login] = newClient;
                        clientsId[ids] = newClient;
                        ids++;
                        ask_sign_in_acc_fd_vector.push_back(i);
                        ask_sign_in_acc_vector.push_back(new Ask_sign_in_acc(true, "Pomyślnie utworzono konto"));
                        FD_SET(i,&tmpWmask);
                        FD_CLR(i,&tmpRmask);
                    }
                    else{
                        ask_sign_in_acc_fd_vector.push_back(i);
                        ask_sign_in_acc_vector.push_back(new Ask_sign_in_acc(false, "Podany login jest już zajęty"));
                        FD_SET(i,&tmpWmask);
                        FD_CLR(i,&tmpRmask);
                    }
                }
                else{
                    FD_SET(i,&tmpRmask);
                    FD_SET(i,&tmpWmask);
                }
                memset(buffor, 0, sizeof(buffor));
            }
            /*
            * ----------------------------------------
            WYSYLANIE DANYCH DO KLIENTOW
            -------------------------------------------
            */
            if(FD_ISSET(i, &wmask)){
                for(int j = 0; j < (int)add_user_acc_fd_vector.size(); j++){
                    if(add_user_acc_fd_vector.at(j) == i){
                        json jObj = add_user_acc_vector.at(j)->ConvertToJson();
                        string temp = jObj.dump();
                        temp.append("\n");
                        char* tmp = (char*)temp.c_str();
                        writeToClient(i, tmp);
                        add_user_acc_fd_vector.erase(add_user_acc_fd_vector.begin() + j);
                        add_user_acc_vector.erase(add_user_acc_vector.begin() + j);
                        j--;
                    }
                }
                for(int j = 0; j < (int)ask_sign_in_acc_fd_vector.size(); j++){
                    if(ask_sign_in_acc_fd_vector.at(j) == i){
                        json jObj = ask_sign_in_acc_vector.at(j)->ConvertToJson();
                        string temp = jObj.dump();
                        temp.append("\n");
                        char* tmp = (char*)temp.c_str();
                        writeToClient(i, tmp);
                        ask_sign_in_acc_fd_vector.erase(ask_sign_in_acc_fd_vector.begin() + j);
                        ask_sign_in_acc_vector.erase(ask_sign_in_acc_vector.begin() + j);
                        j--;
                    }
                }
                for(int j = 0; j < (int)ask_login_acc_fd_vector.size(); j++){
                    if(ask_login_acc_fd_vector.at(j) == i){
                        json jObj = ask_login_acc_vector.at(j)->ConvertToJson();
                        string temp = jObj.dump();
                        temp.append("\n");
                        char* tmp = (char*)temp.c_str();
                        writeToClient(i, tmp);
                        ask_login_acc_fd_vector.erase(ask_login_acc_fd_vector.begin() + j);
                        ask_login_acc_vector.erase(ask_login_acc_vector.begin() + j);
                        j--;
                    }
                }
                for(int j = 0; j < (int)send_message_fd_vector.size(); j++){
                    if(send_message_fd_vector.at(j) == i){
                        json jObj = send_message_vector.at(j)->ConvertToJson();
                        string temp = jObj.dump();
                        temp.append("\n");
                        char* tmp = (char*)temp.c_str();
                        writeToClient(i, tmp);
                        send_message_fd_vector.erase(send_message_fd_vector.begin() + j);
                        send_message_vector.erase(send_message_vector.begin() + j);
                        j--;
                    }
                }
                FD_SET(i,&tmpRmask);
                FD_CLR(i,&tmpWmask);
                fda -= 1;
            }
            /*
            * ----------------------------------------
            FILTROWANIE NIEUZYWANYCH POLACZEN
            -------------------------------------------
            */
            if(i == fdmax){
                while(fdmax > fd && !FD_ISSET(fdmax, &tmpRmask) && !FD_ISSET(fdmax, &tmpWmask)){
                    fdmax--;
                }
            }
        }
    }
    close(fd);
}

int main(){
    startServer();
    return OK_EXIT;
}
