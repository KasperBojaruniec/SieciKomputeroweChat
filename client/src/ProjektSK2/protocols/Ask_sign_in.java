package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//komunikat zapytania o rejestracje
public class Ask_sign_in extends ValidMessage{
    String login;
    String password;
    String name;
    public Ask_sign_in(String login, String password, String name){
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public Ask_sign_in() {
    }

    
    
    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
        String login = obj.getString("login");
        String password = obj.getString("password");
        String name = obj.getString("name");
        return new Ask_sign_in(login, password, name);
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
