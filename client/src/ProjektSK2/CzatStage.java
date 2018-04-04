/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjektSK2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONException;

/**
 *
 * @author Mikolaj Musidlowski
 */
//Klasa dziedzicząca z Stage, zawiera elementy GUI okna czatu oraz metody odpowiedzialne za wysyłanie wiadomości
public class CzatStage extends Stage {

    private Scene czatScene;
    private TextArea wiadomosci = new TextArea();
    private TextField tresc;
    private Button wyslij;
    private HBox wiadomosc;
    private VBox czat;
    private Stage czatStage;

    private Communication_handler communication_handler;
    private Client_me serviced_client;
    private ProjektSK2 GUI;
    private Refresher refresher;

    private int id;
    private String name;

    CzatStage(Communication_handler communication_handler, Client_me serviced_client, int id, String name, ProjektSK2 GUI, Refresher refresher) {

        this.communication_handler = communication_handler;
        this.serviced_client = serviced_client;
        this.id = id;
        this.name = name;
        this.GUI = GUI;
        this.refresher = refresher;

        wiadomosci.setEditable(false);
        wiadomosci.setPrefHeight(550);
        tresc = new TextField();
        tresc.setPrefWidth(530);
        tresc.setPrefHeight(120);
        tresc.setAlignment(Pos.TOP_LEFT);
        wyslij = new Button("Wyślij");
        wyslij.setAlignment(Pos.CENTER);
        wyslij.setOnAction(new EventHandler<ActionEvent>() {
            //wysyłanie wiadomości
            @Override
            public void handle(ActionEvent e) {
                if (!("".equals(tresc.getText()))) {
                    try {
                        communication_handler.Send_message_to_user(serviced_client.getId(), id, serviced_client.getLogin() + ": " + tresc.getText(), 255255255);
                        tresc.clear();
                    } catch (IOException | JSONException ex) {
                        Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("wyslij");
                }
            }
        });

        wiadomosc = new HBox(10);
        wiadomosc.getChildren().addAll(tresc, wyslij);
        wiadomosc.setPrefWidth(600);
        wiadomosc.setAlignment(Pos.CENTER);
        czat = new VBox(5);
        czat.getChildren().addAll(wiadomosci, wiadomosc);
        czat.setAlignment(Pos.CENTER);
        czat.setPrefSize(600, 600);
        czat.setPadding(new Insets(5));
        czatScene = new Scene(czat, 600, 600);

        //inicjalizacja historii czatu
        for (int i = 0; i < serviced_client.getConversations().get(id).getChat().size(); i++) {
            System.out.println("wypisuje wiadomosc z: " + id);
            update(serviced_client.getConversations().get(id).getChat().get(i).text);
        }

        this.setTitle("Czat z " + name);
        this.setScene(czatScene);
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //zamykanie okna czatu
                wiadomosci.clear();
                GUI.getOtwarteCzaty().remove(id);
                refresher.usun(id);
                try {
                    WriteChat.serializeChat(serviced_client.getConversations().get(id), serviced_client.getId(), id);
                } catch (IOException ex) {
                    Logger.getLogger(CzatStage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        this.show();
    }

    //metoda odpowiedzialna za aktualizacje czatu
    public void update(String msg) {
        wiadomosci.setText(wiadomosci.getText() + msg + "\n");
        System.out.println("update");
    }
}
