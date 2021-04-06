package rebus.llc.parvoz.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjavlenijaModel {

    int id = 0;
    String zagolovok = "";
    String opisanie = "";
    String created_at = "";
    String updated_at = "";
    String received_date_time = "";
    int id_type = 0;
    int id_flight = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZagolovok() {
        return zagolovok;
    }

    public void setZagolovok(String zagolovok) {
        this.zagolovok = zagolovok;
    }

    public String getOpisanie() {
        return opisanie;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getReceived_date_time() {
        return received_date_time;
    }

    public void setReceived_date_time(String received_date_time) {
        this.received_date_time = received_date_time;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public int getId_flight() {
        return id_flight;
    }

    public void setId_flight(int id_flight) {
        this.id_flight = id_flight;
    }
}
