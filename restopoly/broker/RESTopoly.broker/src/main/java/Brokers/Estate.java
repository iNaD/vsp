package Brokers;

import java.util.ArrayList;
import java.util.List;

public class Estate {

    private String place;
    private String owner = "";
    private Integer value = 0;
    private List<Integer> rent = new ArrayList<>();
    private List<Integer> cost = new ArrayList<>();
    private Integer houses = 0;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<Integer> getRent() {
        return rent;
    }

    public void setRent(List<Integer> rent) {
        this.rent = rent;
    }

    public List<Integer> getCost() {
        return cost;
    }

    public void setCost(List<Integer> cost) {
        this.cost = cost;
    }

    public Integer getHouses() {
        return houses;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }

    public boolean isOwned() {
        return !this.getOwner().isEmpty();
    }

    public Integer cost() {
        return this.cost.get(this.houses);
    }

    public Integer rent() {
        return this.rent.get(this.houses);
    }

}
