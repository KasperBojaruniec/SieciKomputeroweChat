package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//komunikat zapytania o czat
public class Ask_chat extends ValidMessage {

    private int id_to;
    private int id_from;
    private int len;

    public Ask_chat(int id_from, int id_to, int len) {
        this.id_from = id_from;
        this.id_to = id_to;
        this.len = len;
    }

    public Ask_chat() {
    }

    public int getId_to() {
        return id_to;
    }

    public void setId_to(int id_to) {
        this.id_to = id_to;
    }

    public int getId_from() {
        return id_from;
    }

    public void setId_from(int id_from) {
        this.id_from = id_from;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
    
    

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
        int id_to = obj.getInt("id_to");
        int id_from = obj.getInt("id_from");
        int len = obj.getInt("len");;
        return new Ask_chat(id_from, id_to, len);
    }
}
