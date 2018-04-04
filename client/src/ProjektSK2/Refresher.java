/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjektSK2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

/**
 *
 * @author Mikolaj Musidlowski
 */

//Klasa odpowiedzialna za odświeżanie otwartych czatów
public class Refresher extends Thread {

    private Communication_handler communication_handler;
    private HashMap<Integer, CzatStage> otwarteCzaty;
    private ArrayList<Integer> listaID;
    
    
    
    public void usun(int friendID){
        for(int i = 0; i < listaID.size(); i++){
            if(friendID == listaID.get(i)){
                listaID.remove(i);
            }
        }
    }
    
    public Refresher(Communication_handler communication_handler, ArrayList<Integer> listaID){
        this.communication_handler = communication_handler;
        this.listaID = listaID;
        this.start();
    }

    @Override
    public void run() {
        while (Boolean.TRUE) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Refresher.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < listaID.size(); i++) {
                try {
                    communication_handler.Ask_chat(communication_handler.getClient().getId(), listaID.get(i) , communication_handler.getClient().getConversations().get(listaID.get(i)).getChat().size());
                    System.out.println("odswiezam dla klienta id: "+ listaID.get(i));
                } catch (IOException ex) {
                    Logger.getLogger(Refresher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(Refresher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Refresher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
