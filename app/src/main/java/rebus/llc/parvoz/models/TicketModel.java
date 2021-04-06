package rebus.llc.parvoz.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketModel {

    int id = 0;
    int gorod_vyleta_id = 0;
    int gorod_prileta_id = 0;
    int aeroport_vyleta_id = 0;
    int aeroport_prileta_id = 0;
    String valjuta = "";

    String data_vyleta = "";
    String vremja_vyleta = "";
    String data_prileta = "";
    String vremja_prileta = "";
    String gorod_vyleta_name = "";
    String gorod_prileta_name = "";
    String aeroport_vyleta_name = "";
    String aeroport_prileta_name = "";

    String nomer_bileta = "";
    String nomer_rejsa = "";
    float stoimost = 0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGorod_vyleta_id() {
        return gorod_vyleta_id;
    }

    public void setGorod_vyleta_id(int gorod_vyleta_id) {
        this.gorod_vyleta_id = gorod_vyleta_id;
    }

    public int getGorod_prileta_id() {
        return gorod_prileta_id;
    }

    public void setGorod_prileta_id(int gorod_prileta_id) {
        this.gorod_prileta_id = gorod_prileta_id;
    }

    public int getAeroport_vyleta_id() {
        return aeroport_vyleta_id;
    }

    public void setAeroport_vyleta_id(int aeroport_vyleta_id) {
        this.aeroport_vyleta_id = aeroport_vyleta_id;
    }

    public int getAeroport_prileta_id() {
        return aeroport_prileta_id;
    }

    public void setAeroport_prileta_id(int aeroport_prileta_id) {
        this.aeroport_prileta_id = aeroport_prileta_id;
    }

    public String getData_vyleta() {
        return data_vyleta;
    }

    public void setData_vyleta(String data_vyleta) {
        this.data_vyleta = data_vyleta;
    }

    public String getGorod_vyleta_name() {
        return gorod_vyleta_name;
    }

    public void setGorod_vyleta_name(String gorod_vyleta_name) {
        this.gorod_vyleta_name = gorod_vyleta_name;
    }

    public String getGorod_prileta_name() {
        return gorod_prileta_name;
    }

    public void setGorod_prileta_name(String gorod_prileta_name) {
        this.gorod_prileta_name = gorod_prileta_name;
    }

    public String getAeroport_vyleta_name() {
        return aeroport_vyleta_name;
    }

    public void setAeroport_vyleta_name(String aeroport_vyleta_name) {
        this.aeroport_vyleta_name = aeroport_vyleta_name;
    }

    public String getAeroport_prileta_name() {
        return aeroport_prileta_name;
    }

    public void setAeroport_prileta_name(String aeroport_prileta_name) {
        this.aeroport_prileta_name = aeroport_prileta_name;
    }

    public String getNomer_bileta() {
        return nomer_bileta;
    }

    public void setNomer_bileta(String nomer_bileta) {
        this.nomer_bileta = nomer_bileta;
    }

    public String getNomer_rejsa() {
        return nomer_rejsa;
    }

    public void setNomer_rejsa(String nomer_rejsa) {
        this.nomer_rejsa = nomer_rejsa;
    }

    public float getStoimost() {
        return stoimost;
    }

    public void setStoimost(float stoimost) {
        this.stoimost = stoimost;
    }

    public String getVremja_vyleta() {
        return vremja_vyleta;
    }

    public void setVremja_vyleta(String vremja_vyleta) {
        this.vremja_vyleta = vremja_vyleta;
    }

    public String getData_prileta() {
        return data_prileta;
    }

    public void setData_prileta(String data_prileta) {
        this.data_prileta = data_prileta;
    }

    public String getVremja_prileta() {
        return vremja_prileta;
    }

    public void setVremja_prileta(String vremja_prileta) {
        this.vremja_prileta = vremja_prileta;
    }

    public String getValjuta() {
        return valjuta;
    }

    public void setValjuta(String valjuta) {
        this.valjuta = valjuta;
    }
}
