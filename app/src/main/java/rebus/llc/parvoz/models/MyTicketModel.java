package rebus.llc.parvoz.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyTicketModel {

    String status_id = "novyj";
    int gorod_vyleta_id = 0;
    int gorod_prileta_id = 0;
    int aeroport_vyleta_id = 0;
    int aeroport_prileta_id = 0;
    String data_vyleta = "";
    String vremja_vyleta = "";
    String data_prileta = "";
    String vremja_prileta = "";
    int user_id = 0;
    String updated_at = "";
    String created_at = "";
    int id = 0;
    String surname = "";
    String first_name = "";
    String patronymic = "";
    String birthday = "";
    int gender_id = 0;
    String passport_series = "";
    String passport_number = "";
    String passport_expires = "";
    int country_id = 0;

    String gorod_vyleta_name = "";
    String gorod_prileta_name = "";
    String aeroport_vyleta_name = "";
    String aeroport_prileta_name = "";

    String nomer_bileta = "";
    String nomer_rejsa = "";
    float stoimost = 0;
    String valjuta = "";
    String bagazh = "";


    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
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

    public String getData_vyleta() {
        return data_vyleta;
    }

    public void setData_vyleta(String data_vyleta) {
        this.data_vyleta = data_vyleta;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }

    public String getPassport_series() {
        return passport_series;
    }

    public void setPassport_series(String passport_series) {
        this.passport_series = passport_series;
    }

    public String getPassport_number() {
        return passport_number;
    }

    public void setPassport_number(String passport_number) {
        this.passport_number = passport_number;
    }

    public String getPassport_expires() {
        return passport_expires;
    }

    public void setPassport_expires(String passport_expires) {
        this.passport_expires = passport_expires;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
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

    public String getBagazh() {
        return bagazh;
    }

    public void setBagazh(String bagazh) {
        this.bagazh = bagazh;
    }
}
