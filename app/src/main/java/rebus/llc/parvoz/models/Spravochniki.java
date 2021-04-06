package rebus.llc.parvoz.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Spravochniki {

    List<CountryModel> strany;
    List<CitiesModel> goroda;
    List<LangModel> languages;
    List<AirportModel> aeroporty;

    public List<CountryModel> getStrany() {
        return strany;
    }

    public void setStrany(List<CountryModel> strany) {
        this.strany = strany;
    }

    public List<CitiesModel> getGoroda() {
        return goroda;
    }

    public void setGoroda(List<CitiesModel> goroda) {
        this.goroda = goroda;
    }

    public List<LangModel> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LangModel> languages) {
        this.languages = languages;
    }

    public List<AirportModel> getAeroporty() {
        return aeroporty;
    }

    public void setAeroporty(List<AirportModel> aeroporty) {
        this.aeroporty = aeroporty;
    }
}
