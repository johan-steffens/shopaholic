package za.co.steff.shopaholicsdk.common.dto;

import java.util.List;

public class City extends IdNameBase {

    private List<Mall> malls;

    public List<Mall> getMalls() {
        return malls;
    }

    public void setMalls(List<Mall> malls) {
        this.malls = malls;
    }

    @Override
    public String toString() {
        return "City{" +
                "malls=" + malls +
                "} " + super.toString();
    }
}
