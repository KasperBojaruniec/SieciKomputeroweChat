package ProjektSK2;

import java.io.Serializable;

//Klasa trzymająca treść wiadomości, czas wysłania oraz nadawce

public class Message implements Serializable{
    String text;
    int time;
    int source;

    public Message(String text, int time, int source) {
        this.text = text;
        this.time = time;
        this.source = source;
    }
    
    public Message(String text){
        this.text = text;
    }
}
