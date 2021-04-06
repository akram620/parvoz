package rebus.llc.parvoz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInData {

    UserData klient;            //Модель клиента
    String token_type = "";
        String token = "";
    Spravochniki spravochniki;  //Модель справочников

    public UserData getKlient() {
        return klient;
    }

    public void setKlient(UserData klient) {
        this.klient = klient;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Spravochniki getSpravochniki() {
        return spravochniki;
    }

    public void setSpravochniki(Spravochniki spravochniki) {
        this.spravochniki = spravochniki;
    }
}
