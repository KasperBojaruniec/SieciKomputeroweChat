/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjektSK2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author Mikolaj Musidlowski
 */
//Klasa odpowiedzialna za zapisywanie historii czatu
public class WriteChat {

    public static void serializeChat(Chat chat, int servicedClientID, int friendID) throws IOException {
        
        String name = new Integer(friendID).toString();
        FileOutputStream fout = new FileOutputStream("C:\\Users\\Mikolaj Musidlowski\\Documents\\NetBeansProjects\\SK_2\\client\\chats\\chat"+servicedClientID+friendID+".ser");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(chat);
    }
}
