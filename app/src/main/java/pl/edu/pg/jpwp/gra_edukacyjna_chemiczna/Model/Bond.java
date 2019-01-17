package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model;


import android.graphics.Point;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.FloatPoint;

public class Bond {
    private FloatPoint beginning;
    private FloatPoint end;
    private int startAtomId, endAtomId;

    public Bond(FloatPoint beginning, FloatPoint end, int startAtomId, int endAtomId) {
        this.beginning = beginning;
        this.end = end;
        this.startAtomId=startAtomId;
        this.endAtomId=endAtomId;
    }

    public float getBeginningX() {
        return beginning.x;
    }

    public float getBeginningY() {
        return beginning.y;
    }

    public float getEndX() {
        return end.x;
    }

    public float getEndY() {
        return end.y;
    }

    public int getStartAtomId() {
        return startAtomId;
    }

    public int getEndAtomId() {
        return endAtomId;
    }

    boolean intersects(FloatPoint a, FloatPoint b) {

        //System.out.println("cut line from: "+a+" to: "+b);

        Point c = new Point((int)getBeginningX(), (int)getBeginningY());
        Point d = new Point((int)getEndX(), (int)getEndY());
        float denominator = ((b.x - a.x) * (d.y - c.y)) - ((b.y - a.y) * (d.x - c.x));
        float numerator1 = ((a.y - c.y) * (d.x - c.x)) - ((a.x - c.x) * (d.y - c.y));
        float numerator2 = ((a.y - c.y) * (b.x - a.x)) - ((a.x - c.x) * (b.y - a.y));

        // Detect coincident lines (has a problem, read below)
        if (denominator == 0) return numerator1 == 0 && numerator2 == 0;

        float r = numerator1 / denominator;
        float s = numerator2 / denominator;

        return (r >= 0 && r <= 1) && (s >= 0 && s <= 1);
    }

}
