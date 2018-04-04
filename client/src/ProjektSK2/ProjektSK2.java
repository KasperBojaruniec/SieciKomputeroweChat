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
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONException;

/**
 *
 * @author Mikolaj Musidlowski
 */


//Główna klasa projektu zawierająca elementy GUI oraz odpowiedzialna za interakcje z użytkownikiem
public class ProjektSK2 extends Application {

    //Okno logowania
    private GridPane grid_logowanie;
    private Label userNameLabel = new Label("User Name:");
    private TextField userNameTextField;
    private Label passwordLabel = new Label("Password:");
    private PasswordField passwordTextField;
    private Button button_login;
    private Button button_register;
    private HBox logowanie_buttons;
    private Scene logowanieScene;

    //Feedback logowania
    private Label feedbackLoginLabel = new Label("Niepoprawne dane");
    private StackPane feedbackLoginPane;
    private Scene feedbackLoginScene;
    private Stage feedbackLoginStage;

    //Feedback rejestracji
    private Label feedbackRegisterLabel;
    private StackPane feedbackRegisterPane;
    private Scene feedbackRegisterScene;
    private Stage feedbackRegisterStage;

    //okno kontaktów
    private Stage kontaktyStage;
    private Scene kontaktyScene;
    private Label zalogowanoLabel = new Label("Zalogowano");
    private VBox kontakty;
    private ArrayList<Button> kontakty_buttons;
    private Button temp_button;
    private Button button_dodaj;

    //okno dodawania znajomego
    private VBox dodajVBox;
    private final Label dodajLabel = new Label("Wpisz ID znajomego");
    private TextField textDodaj;
    private Button button_dod;
    private Scene dodajScene;
    private Stage dodajStage;
    private Button addedButton;

    //okno czatu
    private Scene czatScene;
    private TextArea wiadomosci = new TextArea();
    private TextField tresc;
    private Button wyslij;
    private HBox wiadomosc;
    private VBox czat;
    private Stage czatStage;

    private Client_me serviced_client;
    private Communication_handler communication_handler;
    private HashMap<Integer, CzatStage> otwarteCzaty = new HashMap<>();
    private Refresher refresher;
    private ArrayList<Integer> listaID = new ArrayList<>();

    //monitory
    final Integer monitor_1 = 1;
    final Integer monitor_2 = 2;
    final Integer monitor_3 = 3;
    final Integer monitor_4 = 4;
    final Integer monitor_5 = 5;
    final Integer monitor_6 = 6;

    Boolean isRegistered = false;

    @Override
    public void start(Stage primaryStage) {

        //tworzenie okna logowania
        primaryStage.setTitle("Projekt SK2");
        grid_logowanie = new GridPane();
        grid_logowanie.setAlignment(Pos.CENTER);
        grid_logowanie.setHgap(10);
        grid_logowanie.setVgap(10);
        grid_logowanie.setPadding(new Insets(25, 25, 25, 25));
        userNameTextField = new TextField();
        passwordTextField = new PasswordField();
        grid_logowanie.add(userNameLabel, 0, 1);
        grid_logowanie.add(userNameTextField, 1, 1);
        grid_logowanie.add(passwordLabel, 0, 2);
        grid_logowanie.add(passwordTextField, 1, 2);

        serviced_client = new Client_me();
        communication_handler = new Communication_handler(serviced_client, this);

        button_login = new Button("Sign in");
        button_login.setOnAction((ActionEvent event) -> {

            serviced_client.setLogin(userNameTextField.getText());
            serviced_client.setPassword(passwordTextField.getText());
            
            //wysyłanie zapytania o logowanie
            try {
                communication_handler.Ask_login(serviced_client);
            } catch (IOException | JSONException ex) {
                Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
            }
            synchronized (monitor_1) {
                try {
                    monitor_1.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (serviced_client.getLogged()) {

                //tworzenie okna kontaktów
                kontakty = new VBox(5);
                kontakty.setAlignment(Pos.TOP_CENTER);
                kontakty.setPadding(new Insets(25, 25, 25, 25));
                kontakty_buttons = new ArrayList<>();

                for (Client friend : serviced_client.getFriends()) {
                    temp_button = new Button(friend.getName());
                    temp_button.setOnAction((ActionEvent e) -> {

                        
                        listaID.add(friend.getId());
                        try {
                            ReadChat.deserializeChat(serviced_client, friend.getId());
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //otwieranie okna czatu 
                        otwarteCzaty.put(friend.getId(), new CzatStage(this.communication_handler, this.serviced_client, friend.getId(), friend.getName(), this, this.refresher));
                        
                        //zapytanie o czat
                        try {
                            communication_handler.Ask_chat(serviced_client.getId(), friend.getId(), serviced_client.getConversations().get(friend.getId()).getChat().size());
                        } catch (IOException | JSONException ex) {
                            Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        synchronized (monitor_4) {
                            try {
                                monitor_4.wait();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });

                    kontakty_buttons.add(temp_button);
                }

                kontakty.getChildren().add(zalogowanoLabel);

                textDodaj = new TextField();
                textDodaj.setAlignment(Pos.CENTER);
                button_dodaj = new Button("Dodaj znajomego");
                button_dodaj.setOnAction((ev) -> {
                    button_dod = new Button("Dodaj");
                    button_dod.setOnAction((eve) -> {
                        
                        //tworzenie okna dodawania znajomych
                        if ((!(textDodaj.getText().equals("") || Integer.parseInt(textDodaj.getText()) == this.serviced_client.getId())) && sprawdz(Integer.parseInt(textDodaj.getText()))) {
                            
                            //dodawanie znajomych
                            communication_handler.Add_user(Integer.parseInt(textDodaj.getText()), serviced_client.getId());
                            synchronized (monitor_5) {
                                try {
                                    monitor_5.wait();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        textDodaj.clear();
                        int k = serviced_client.getFriends().size();
                        int j = kontakty_buttons.size();
                        for (int i = j; i < k; i++) {
                            addedButton = new Button(serviced_client.getFriends().get(i).getName());
                            int id = serviced_client.getFriends().get(i).getId();
                            int xd = i;
                            addedButton.setOnAction((ActionEvent e) -> {
                                
                                // otwieranie okna czatu
                                otwarteCzaty.put(serviced_client.getFriends().get(xd).getId(), new CzatStage(this.communication_handler, this.serviced_client, serviced_client.getFriends().get(xd).getId(), serviced_client.getFriends().get(xd).getName(), this, this.refresher));
                                listaID.add(serviced_client.getFriends().get(xd).getId());
                                try {
                                    
                                    //zapytanie o czat
                                    communication_handler.Ask_chat(serviced_client.getId(), id, serviced_client.getConversations().get(serviced_client.getFriends().get(xd).getId()).getChat().size());
                                } catch (IOException | JSONException ex) {
                                    Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                synchronized (monitor_4) {
                                    try {
                                        monitor_4.wait();
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            });
                            kontakty.getChildren().add(addedButton);
                            kontakty_buttons.add(addedButton);
                        }

                    });
                    
                    //tworzenie okna dodawania znajomego
                    dodajVBox = new VBox(5);
                    dodajVBox.setAlignment(Pos.CENTER);
                    dodajVBox.getChildren().add(dodajLabel);
                    dodajVBox.getChildren().add(textDodaj);
                    dodajVBox.getChildren().add(button_dod);
                    dodajScene = new Scene(dodajVBox, 200, 100);
                    dodajStage = new Stage();
                    dodajStage.setTitle("Dodaj znajomego");
                    dodajStage.setScene(dodajScene);
                    dodajStage.show();
                });
                
                //tworzenie okna kontaktów
                kontakty.getChildren().add(button_dodaj);
                for (int i = 0; i < kontakty_buttons.size(); i++) {
                    kontakty.getChildren().add(kontakty_buttons.get(i));
                }
                kontaktyScene = new Scene(kontakty, 300, 600);
                kontaktyStage = new Stage();
                kontaktyStage.setTitle("Kontakty");
                kontaktyStage.setScene(kontaktyScene);
                kontaktyStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    //wysyłanie zamykania połączenia
                    public void handle(WindowEvent we) {
                        communication_handler.Close_connection();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.exit(0);
                    }
                });
                primaryStage.close();
                kontaktyStage.show();
                refresher = new Refresher(communication_handler, listaID);
            } else {
                
                //tworzenie okna z wiadomością logowania
                feedbackLoginPane = new StackPane();
                feedbackLoginPane.getChildren().add(feedbackLoginLabel);
                feedbackLoginScene = new Scene(feedbackLoginPane, 200, 100);
                feedbackLoginStage = new Stage();
                feedbackLoginStage.setTitle("Błąd logowania");
                feedbackLoginStage.setScene(feedbackLoginScene);
                feedbackLoginStage.show();
            }
        });

        button_register = new Button("Register");
        button_register.setOnAction((event) -> {
            
            //wysyłanie prośby o rejestrację
            try {
                communication_handler.Ask_sign_in(userNameTextField.getText(), passwordTextField.getText(), userNameTextField.getText());
            } catch (IOException | JSONException ex) {
                Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
            }

            synchronized (monitor_3) {
                try {
                    monitor_3.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //tworzenie okna z wiadomościa rejestracji
            if (serviced_client.getIsRegistered()) {
                feedbackRegisterPane = new StackPane();
                feedbackRegisterLabel = new Label("Konto zostało utworzone");
                feedbackRegisterPane.getChildren().add(feedbackRegisterLabel);
                feedbackRegisterScene = new Scene(feedbackRegisterPane, 200, 100);
                feedbackRegisterStage = new Stage();
                feedbackRegisterStage.setTitle("Rejestracja udana");
                feedbackRegisterStage.setScene(feedbackRegisterScene);
                feedbackRegisterStage.show();

            } else {

                feedbackRegisterLabel = new Label("Podany login jest już zajęty");
                feedbackRegisterPane = new StackPane();
                feedbackRegisterPane.getChildren().add(feedbackRegisterLabel);
                feedbackRegisterScene = new Scene(feedbackRegisterPane, 200, 100);
                feedbackRegisterStage = new Stage();
                feedbackRegisterStage.setTitle("Rejestracja nieudana");
                feedbackRegisterStage.setScene(feedbackRegisterScene);
                feedbackRegisterStage.show();
            }
        });
        logowanie_buttons = new HBox(10);
        logowanie_buttons.setAlignment(Pos.BOTTOM_RIGHT);
        logowanie_buttons.getChildren().add(button_login);
        logowanie_buttons.getChildren().add(button_register);
        grid_logowanie.add(logowanie_buttons, 1, 4);
        logowanieScene = new Scene(grid_logowanie, 300, 275);
        primaryStage.setScene(logowanieScene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                
                //zamykanie połączenie
                communication_handler.Close_connection();
                synchronized (monitor_2) {
                    try {
                        monitor_2.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProjektSK2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    public Boolean sprawdz(int id) {

        for (int i = 0; i < this.serviced_client.getFriends().size(); i++) {
            if (this.serviced_client.getFriends().get(i).getId() == id) {
                return Boolean.FALSE;

            }
        }
        return Boolean.TRUE;
    }

    public void update(String msg) {
        wiadomosci.setText(wiadomosci.getText() + msg + "\n");
    }

    public HashMap<Integer, CzatStage> getOtwarteCzaty() {
        return otwarteCzaty;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
