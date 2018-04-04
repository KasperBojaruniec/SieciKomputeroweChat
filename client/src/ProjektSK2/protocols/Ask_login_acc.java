package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//Odpowiedź zapytania o logowanie
public class Ask_login_acc extends ValidMessage{
    private Boolean acc;
    private String warning;
    private int id;
    public Ask_login_acc( Boolean acc, String warning, int id){
        this.acc = acc;
        this.warning = warning;
        this.id = id;
    }

    public Ask_login_acc() {
    }

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
         Boolean acc = obj.getBoolean("acc");
         String warning = obj.getString("warning");
         int id = obj.getInt("id");
        return new Ask_login_acc( acc, warning, id);
    }

    public Boolean getAcc_login() {
        return acc;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAcc_login(Boolean acc_login) {
        this.acc = acc_login;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
    
    
    
    
}
