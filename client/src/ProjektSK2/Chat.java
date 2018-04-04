package ProjektSK2;

import java.io.Serializable;
import java.util.ArrayList;

//Klasa trzymająca liste z Messege z danym użytkownikiem i jej długość

public class Chat implements Serializable{
    ArrayList<Message> chat;
    double length;


    public Chat(ArrayList<Message> chat, double length) {
        this.chat = chat;
        this.length = length;

    }

    public ArrayList<Message> getChat() {
        return chat;
    }

    public void setChat(ArrayList<Message> chat) {
        this.chat = chat;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
    
    public void incLength() {
        length++;
    }
    
    
    
}
