package rebus.llc.parvoz.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirportModel {

    	int id = 0;

        int city_id = 0;
        String name = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
