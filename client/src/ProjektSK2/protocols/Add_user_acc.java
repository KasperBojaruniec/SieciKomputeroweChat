package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//Komunikat odpowiedzi na dodanie znajomego
public class Add_user_acc extends ValidMessage{
    private int id_from;
    private int id_to;
    private Boolean acc;
    private String name;
    public Add_user_acc(int id_from, Boolean acc, String name){
        this.id_from = id_from;
        this.acc = acc;
        this.name = name;
    }

    public Add_user_acc() {
    }

    public int getId_from() {
        return id_from;
    }
    
    public int getId_to() {
        return id_to;
    }

    public Boolean getAcc() {
        return acc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
        int id_from = obj.getInt("id_from");
        Boolean acc = obj.getBoolean("acc");
        String name = obj.getString("name");
        return new Add_user_acc(id_from, acc, name);
    }



}
