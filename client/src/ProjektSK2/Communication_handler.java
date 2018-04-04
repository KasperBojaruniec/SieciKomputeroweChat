/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjektSK2;

import ProjektSK2.protocols.Ask_sign_in;
import ProjektSK2.protocols.Ask_chat;
import ProjektSK2.protocols.Close_connection;
import ProjektSK2.protocols.Send_message;
import ProjektSK2.protocols.Ask_login;
import ProjektSK2.protocols.Add_user;
import ProjektSK2.protocols.ValidMessage;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Logger;

//Klasa odpowiedzialna za kolejkowanie komunikatów do wysłania od clienta do serwera oraz za obsługę komunikatów odebranych od servera

public class Communication_handler extends Thread {

    private Queue<ValidMessage> messages_from_server = new PriorityQueue<>();
    private Client_me client;
    private Connection_handler connection_handler;
    private ArrayList<ValidMessage> wiadomosciDoWyslania = new ArrayList<>();
    private Boolean czyWyslalem = false;
    private final Integer monitor_1 = 1;
    private final Integer monitor_2 = 2;
    private final Integer monitor_3 = 3;
    private final Integer monitor_4 = 4;
    private final Integer monitor_5 = 5;
    private final Integer monitor_6 = 6;
    private ProjektSK2 GUI;

    
    //Konstruktory
    public Communication_handler(Client_me client, Connection_handler con_handler) {
        this.client = client;
        this.connection_handler = con_handler;

    }

    public Communication_handler(Client_me client, ProjektSK2 GUI) {
        this.GUI = GUI;
        this.client = client;
        this.start();
    }

    public Communication_handler(Connection_handler con_handler) {
        this.connection_handler = con_handler;
        this.run();
    }

    //Metody do wysyłania komunikatów
    public void Send_message_to_user(int id_from, int id_to, String text, int color) throws IOException, JSONException {
        wiadomosciDoWyslania.add(new Send_message(text, id_to, id_from, 255255255));
    }

    public void Ask_chat(int id_from, int id_to, int len) throws IOException, JSONException {
        wiadomosciDoWyslania.add(new Ask_chat(id_from, id_to, len));
    }

    public void Ask_login(Client_me client) throws IOException, JSONException {
        wiadomosciDoWyslania.add(new Ask_login(client.getLogin(), client.getPassword()));
    }

    public void Ask_sign_in(String password, String login, String name) throws IOException, JSONException {
        wiadomosciDoWyslania.add(new Ask_sign_in(password, login, name));
    }

    public void Add_user(int id_to, int id_from) {
        wiadomosciDoWyslania.add(new Add_user(id_to, id_from));
    }

    public void Close_connection() {
        wiadomosciDoWyslania.add(new Close_connection());
    }

    public void Listen_to_server() throws IOException, InterruptedException, JSONException {
        ValidMessage add;
        while ((add = connection_handler.ListenToServer()) != null) {
            messages_from_server.add(add);
        }
    }

    
    @Override
    public void run() {
        connection_handler = new Connection_handler();
        connection_handler.Create_Connection();
        try {
            connection_handler.Create_Buffors();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                //Sprawdzanie czy są jakies komunikaty do wysłania i obsługa przychodzących
                if (!wiadomosciDoWyslania.isEmpty()) {
                    connection_handler.sendRequest(wiadomosciDoWyslania.get(wiadomosciDoWyslania.size() - 1));
                    wiadomosciDoWyslania.remove(wiadomosciDoWyslania.size() - 1);
                    synchronized (monitor_2) {
                        monitor_2.notify();
                    }
                }

                ValidMessage response = connection_handler.ListenToServer();
                if (response != null) {
                    handleMessages(response);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(Variables.waitForRespondTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Metoda obsługująca przychodzące komunikaty
    public void handleMessages(ValidMessage handled_message) {

        if ("Ask_login_acc".equals(handled_message.getKey())) {
            if (handled_message.getAcc_login()) {
                client.setLogged(Boolean.TRUE);
                client.setId(handled_message.getId());
            }
            synchronized (monitor_1) {
                monitor_1.notify();
            }
        }
        if ("Add_user_acc".equals(handled_message.getKey())) {
            if (handled_message.getAcc()) {
                Client from_client = new Client(handled_message.getName(), handled_message.getId_from());
                client.getFriends().add(from_client);
                ArrayList<Message> xd = new ArrayList<>();
                Chat chat = new Chat(xd, 0);
                client.getConversations().put(handled_message.getId_from(), chat);
            }
            synchronized (monitor_5) {
                monitor_5.notify();
            }
        }
        if ("Ask_sign_in_acc".equals(handled_message.getKey())) {
            if (handled_message.getAcc()) {
                client.setIsRegistered(Boolean.TRUE);
            }
            synchronized (monitor_3) {
                monitor_3.notify();
            }
        }

        if ("Send_message".equals(handled_message.getKey())) {
            if (handled_message.getText().equals("\n\n\n")) {
            } else {
                int niemoje;
                if (handled_message.getId_from() == client.getId()) {
                    niemoje = handled_message.getId_to();
                } else {
                    niemoje = handled_message.getId_from();
                }
                System.out.println(GUI.getOtwarteCzaty().get(niemoje));
                
                GUI.getOtwarteCzaty().get(niemoje).update(handled_message.getText());
                System.out.println("zapisuje do czatu z: " + niemoje);
                client.getConversations().get(niemoje).getChat().add(new Message(handled_message.getText()));
            }
            synchronized (monitor_4) {
                monitor_4.notify();
            }
        }
    }

    public Client_me getClient() {
        return client;
    }
}
