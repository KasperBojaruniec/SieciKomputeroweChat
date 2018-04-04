package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//Komunikat zapytania o logowanie
public class Ask_login extends ValidMessage{
    String login;
    String password;

    public Ask_login(String login, String password){
        this.login = login;
        this.password = password;
    }

    public Ask_login() {
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
    public JSONObject toJson() throws JSONException {
        JSONObject o = super.toJson();
        o.put("login", login);
        o.put("password", password);
        return o;
    }

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
        String login = obj.getString("login");
        String password = obj.getString("password");
        return new Ask_login(login, password);
    }
}
