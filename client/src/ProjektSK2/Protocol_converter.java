package ProjektSK2;

import ProjektSK2.protocols.Ask_sign_in;
import ProjektSK2.protocols.Ask_login_acc;
import ProjektSK2.protocols.Ask_chat;
import ProjektSK2.protocols.Send_message;
import ProjektSK2.protocols.Ask_login;
import ProjektSK2.protocols.Add_user;
import ProjektSK2.protocols.Ask_sign_in_acc;
import ProjektSK2.protocols.Add_user_acc;
import ProjektSK2.protocols.ValidMessage;
import org.json.JSONException;
import org.json.JSONObject;

//Klasa odpowiedziana za zamiane obkietów JSON na ValidMessage
public class Protocol_converter {

    public Protocol_converter() {
    }

    public static ValidMessage convert(JSONObject obj) throws JSONException {
        ValidMessage valMes = null;
        Object key = obj.get("key");
            if(key.equals(Add_user.class.getSimpleName()))
                valMes = Add_user.createFromJSON(obj);
            else if(key.equals(Add_user_acc.class.getSimpleName()))
                valMes = Add_user_acc.createFromJSON(obj);
            else if(key.equals(Ask_chat.class.getSimpleName()))
                valMes = Ask_chat.createFromJSON(obj);
            else if(key.equals(Ask_login.class.getSimpleName()))
                valMes = Ask_login.createFromJSON(obj);
            else if(key.equals(Ask_login_acc.class.getSimpleName()))
                valMes = Ask_login_acc.createFromJSON(obj);
            else if(key.equals(Ask_sign_in.class.getSimpleName()))
                valMes = Ask_sign_in.createFromJSON(obj);
            else if(key.equals(Ask_sign_in_acc.class.getSimpleName()))
                valMes = Ask_sign_in_acc.createFromJSON(obj);
            else if(key.equals(Send_message.class.getSimpleName()))
                valMes = Send_message.createFromJSON(obj);
        return valMes;
    }
}
