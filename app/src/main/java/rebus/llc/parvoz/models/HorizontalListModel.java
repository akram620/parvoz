package rebus.llc.parvoz.models;

public class HorizontalListModel {

    int id_country = 0;
    String countryName = "";
    boolean state = false;
    int amount_of_flights = 0;

    public int getId_country() {
        return id_country;
    }

    public void setId_country(int id_country) {
        this.id_country = id_country;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getAmount_of_flights() {
        return amount_of_flights;
    }

    public void setAmount_of_flights(int amount_of_flights) {
        this.amount_of_flights = amount_of_flights;
    }
}
