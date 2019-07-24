package za.co.steff.shopaholicsdk.common.dto;

import za.co.steff.shopaholicsdk.network.model.ShopResponse;

public class Shop extends IdNameBase {

    public Shop() {}

    public Shop(long id, String name) {
        super(id, name);
    }

    public Shop(ShopResponse shop) {
        super(shop.getId(), shop.getName());
    }

}
