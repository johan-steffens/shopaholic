package za.co.steff.shopaholicsdk.network.model;

import java.util.List;

public class MallResponse extends IdNameBaseResponse {

    private List<ShopResponse> shops;

    public List<ShopResponse> getShops() {
        return shops;
    }

    public void setShops(List<ShopResponse> shops) {
        this.shops = shops;
    }

    @Override
    public String toString() {
        return "Mall{" +
                "shops=" + shops +
                "} " + super.toString();
    }
}
