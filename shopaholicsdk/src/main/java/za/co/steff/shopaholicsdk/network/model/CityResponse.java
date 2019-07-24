package za.co.steff.shopaholicsdk.network.model;

import java.util.List;

public class CityResponse extends IdNameBaseResponse {

    private List<MallResponse> malls;

    public List<MallResponse> getMalls() {
        return malls;
    }

    public void setMalls(List<MallResponse> malls) {
        this.malls = malls;
    }

    @Override
    public String toString() {
        return "City{" +
                "malls=" + malls +
                "} " + super.toString();
    }
}
