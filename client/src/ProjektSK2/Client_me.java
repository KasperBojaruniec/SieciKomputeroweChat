package ProjektSK2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//Klasa dla zalogowanego użytkownika, trzymająca jego login, hasłom rozmowy, znajomych
public class Client_me extends Client {
    private String login, password;
    private HashMap<Integer, Chat> conversations;
    private Boolean logged = false;
    private ArrayList<Client> friends;
    private Boolean isRegistered = false;
    public Client_me(String name, int id, String login, String password, ArrayList<Client> friends) {
        super(name, id);
        this.login = login;
        this.password = password;
        this.friends = friends;
    }

    public Client_me(String name, int id) {
        super(name, id);
    }

    public Client_me() {
        friends = new ArrayList<>();
        conversations = new HashMap<>();
    }

    public Boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }
    
    

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<Integer, Chat> getConversations() {
        return conversations;
    }

    public void setConversations(HashMap<Integer, Chat> conversations) {
        this.conversations = conversations;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public ArrayList<Client> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Client> friends) {
        this.friends = friends;
    }
}
