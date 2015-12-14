package Brokers;

import java.util.ArrayList;
import java.util.Collection;

public class EstatesList {
    public Collection<Estate> estates = new ArrayList<>();

    public EstatesList() {
    }

    public EstatesList(Collection<Estate> estates) {
        this.estates = estates;
    }
}
