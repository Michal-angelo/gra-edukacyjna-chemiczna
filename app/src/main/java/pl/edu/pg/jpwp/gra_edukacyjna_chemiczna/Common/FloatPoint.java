package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common;

import android.graphics.Point;

public class FloatPoint {

    public float x, y;

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FloatPoint(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

}
