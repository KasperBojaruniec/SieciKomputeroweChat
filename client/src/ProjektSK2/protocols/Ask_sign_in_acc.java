package ProjektSK2.protocols;

import org.json.JSONException;
import org.json.JSONObject;

//odpowiedź na komunikat zapytania o rejestrację
public class Ask_sign_in_acc extends ValidMessage{
    private Boolean acc;
    private String warning;

    public Ask_sign_in_acc(Boolean acc, String warning){
        this.acc = acc;
        this.warning = warning;
    }
    public Ask_sign_in_acc() {
    }

    public static ValidMessage createFromJSON(JSONObject obj) throws JSONException {
         Boolean acc = obj.getBoolean("acc");
         String warning = obj.getString("warning");
        return new Ask_sign_in_acc(acc, warning);
    }

    public Boolean getAcc() {
        return acc;
    }

    public void setAcc(Boolean acc) {
        this.acc = acc;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
