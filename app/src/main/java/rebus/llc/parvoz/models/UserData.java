package rebus.llc.parvoz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
	int id = 0;
    String surname = "";
    String first_name = "";
    String patronymic = "";
    int gender_id = 0;
    String birthday = "";
    String passport_series = "";
    String passport_number = "";
    String passport_expires = "";
    int spoken_language_id = 0;
    int country_id = 0;
    String email = "";
    String token = "";
    String login = "";
    String photo_doc = "";

    List<String> phones;

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

    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public int getSpoken_language_id() {
        return spoken_language_id;
    }

    public void setSpoken_language_id(int spoken_language_id) {
        this.spoken_language_id = spoken_language_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getPhoto_doc() {
        return photo_doc;
    }

    public void setPhoto_doc(String photo_doc) {
        this.photo_doc = photo_doc;
    }
}
