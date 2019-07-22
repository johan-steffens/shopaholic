package za.co.steff.shopaholicsdk.network.model;

import java.util.List;

import za.co.steff.shopaholicsdk.common.dto.City;

public class CitiesResponse {

    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "CitiesResponse{" +
                "cities=" + cities +
                '}';
    }
}
