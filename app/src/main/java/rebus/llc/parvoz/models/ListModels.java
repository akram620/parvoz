package rebus.llc.parvoz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListModels {

    List<MyTicketModel> zakazy;
    List<TicketModel> aktualnyeRejsy;
    List<ObjavlenijaModel> objavlenija;



    public List<MyTicketModel> getZakazy() {
        return zakazy;
    }

    public void setZakazy(List<MyTicketModel> zakazy) {
        this.zakazy = zakazy;
    }

    public List<TicketModel> getAktualnyeRejsy() {
        return aktualnyeRejsy;
    }

    public void setAktualnyeRejsy(List<TicketModel> aktualnyeRejsy) {
        this.aktualnyeRejsy = aktualnyeRejsy;
    }

    public List<ObjavlenijaModel> getObjavlenija() {
        return objavlenija;
    }

    public void setObjavlenija(List<ObjavlenijaModel> objavlenija) {
        this.objavlenija = objavlenija;
    }
}
