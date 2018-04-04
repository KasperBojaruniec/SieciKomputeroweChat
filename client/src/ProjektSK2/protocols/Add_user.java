package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//Komunikat dodawania znajomego
public class Add_user extends ValidMessage{
    private int id_to;
    private int id_from;
    public Add_user(int id_to, int id_from){
        this.id_to = id_to;
        this.id_from = id_from;
    }

    public Add_user() {
    }

    public int getId_to() {
        return id_to;
    }

    public int getId_from() {
        return id_from;
    }

    @Override
    public String toString() {
        return "Add_user{" + "id_to=" + id_to + ", id_from=" + id_from + '}';
    }


    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
        int id_to = obj.getInt("id_to");
        int id_from = obj.getInt("id_from");
        return new Add_user(id_to, id_from);
    }

}
