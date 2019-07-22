package za.co.steff.shopaholicsdk.common.dto;

import java.util.List;

public class Mall extends IdNameBase {

    private List<Shop> shops;

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    @Override
    public String toString() {
        return "Mall{" +
                "shops=" + shops +
                "} " + super.toString();
    }
}
