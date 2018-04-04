package ProjektSK2.protocols;


import org.json.JSONException;
import org.json.JSONObject;

//Komunikat z którego dziedziczą wszystkie inne komunikaty
public class ValidMessage {
    protected String key = this.getClass().toString();

    public ValidMessage(){
        this.key = this.getClass().getSimpleName();
    }

    public String getKey() {
        return key;
    }

    public int getId_to() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getId_from() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getConversation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean getAcc_login() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean getAcc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getWarning() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException{
        return new ValidMessage();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject t = new JSONObject();
        t.put("key", key);
        return t;
    }

    public String getText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

