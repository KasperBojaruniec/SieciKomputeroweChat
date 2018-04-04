package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//komunikat wysyłający wiadomość między użytkownikami

public class Send_message extends ValidMessage{
    private String text;
    private int id_to;
    private int id_from;
    private int color;

    public Send_message(String text, int id_to, int id_from, int color) {

        this.text = text;
        this.id_to = id_to;
        this.id_from = id_from;
        this.color = color;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    
    
    public Send_message() {
    }

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
         String text = obj.getString("text");
         int id_to = obj.getInt("id_to");
         int id_from = obj.getInt("id_from");
         int color = obj.getInt("color");
        return new Send_message(text, id_to,id_from,color);
    }
    
}
