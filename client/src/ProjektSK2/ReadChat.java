/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjektSK2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author Mikolaj Musidlowski
 */
//Klasa odpowiedzialna za wczytywanie historii czatu z pliku
public class ReadChat {
    
    public static void deserializeChat(Client_me servicedClient, int friendID) throws FileNotFoundException, IOException, ClassNotFoundException{
        String path = "C:\\Users\\Mikolaj Musidlowski\\Documents\\NetBeansProjects\\SK_2\\client\\chats\\chat"+new Integer(servicedClient.getId()).toString()+new Integer(friendID).toString()+".ser";
        FileInputStream streamIn = new FileInputStream(path);
        ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
        Chat chat = (Chat)objectinputstream.readObject();
        servicedClient.getConversations().replace(friendID, chat);
    }
    
}
