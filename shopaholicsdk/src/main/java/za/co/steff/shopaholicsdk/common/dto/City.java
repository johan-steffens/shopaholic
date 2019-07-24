package za.co.steff.shopaholicsdk.common.dto;

import za.co.steff.shopaholicsdk.network.model.CityResponse;

public class City extends IdNameBase {

    public City() {}

    public City(long id, String name) {
        super(id, name);
    }

    public City(CityResponse city) {
        super(city.getId(), city.getName());
    }

}
