package ProjektSK2;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import ProjektSK2.protocols.ValidMessage;


//Klasa odpowiedzialna za połączenie z serwerem oraz wysyłanie i odbieranie komunikatów

public class Connection_handler {

    private Socket socket = null;
    private final int PORT = 8888;
    private final String HOST = "192.168.43.34";
    private BufferedReader inFromServer;
    private DataOutputStream outToServer;


    public Connection_handler() {
    }

    //Tworzenie połączenia
    public void Create_Connection() {
        try {
            socket = new Socket(HOST, PORT);

        } catch (IOException e) {
            
        }

    }
    //Tworzenie bufforów
    public void Create_Buffors() throws IOException {
        inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outToServer = new DataOutputStream(socket.getOutputStream());
    }

    //Słuchanie serwera
    public ValidMessage ListenToServer() throws JSONException, IOException {
        if (inFromServer.ready()) {
//            System.out.println("wchodze do reada");
            String test = inFromServer.readLine();
            System.out.println(test);
            return Protocol_converter.convert(new JSONObject(test));
        } else {
//            System.out.println("odsylam nulla");
            return null;
        }
    }

    //Wysyłanie zapytania
    public void sendRequest(ValidMessage req) throws IOException, JSONException {
        // TODO: 13.11.2017 przypomnij Kaspi inny Json wysylany (req.toJson())
        JSONObject json = new JSONObject(req);
//        System.out.println(json.toString());
        outToServer.writeBytes(json.toString()+"\n");
        outToServer.flush();
    }

    //Zamykanie połączenie
    public void Close_Connection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Couldn't close connection: " + e);
        }
    }
}
