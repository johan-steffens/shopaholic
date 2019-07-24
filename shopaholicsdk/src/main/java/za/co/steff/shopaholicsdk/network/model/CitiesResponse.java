package za.co.steff.shopaholicsdk.network.model;

import java.util.List;

public class CitiesResponse {

    private List<CityResponse> cities;

    public List<CityResponse> getCities() {
        return cities;
    }

    public void setCities(List<CityResponse> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "CitiesResponse{" +
                "cities=" + cities +
                '}';
    }
}
