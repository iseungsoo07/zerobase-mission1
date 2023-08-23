package zerobase.mission1;

import lombok.Getter;

@Getter
public class Pos {
    private double lat;
    private double lnt;

    public Pos(double lat, double lnt) {
        this.lat = lat;
        this.lnt = lnt;
    }
}
