package za.co.steff.shopaholicsdk.common.dto;

import za.co.steff.shopaholicsdk.network.model.MallResponse;

public class Mall extends IdNameBase {

    public Mall() {}

    public Mall(long id, String name) {
        super(id, name);
    }

    public Mall(MallResponse mall) {
        super(mall.getId(), mall.getName());
    }

}
