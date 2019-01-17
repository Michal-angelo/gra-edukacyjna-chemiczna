package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Enums;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.Atom;

public class DrawableAtom {
    static float SCALE = (float) 1.2;

    private static int RADIUS = 50;
    static int TEXT_SIZE = 50;
    static boolean ANTI_ALIASING = true;

    static int RING_SIZE = 70;//outter diameter
    static int RING_WIDTH = 5;

    static void drawAtom(Canvas canvas, Atom atom) {
        float x = atom.getX();
        float y = atom.getY();
        Enums.Element element = atom.getType();
        drawBondingRing(canvas, x, y);
        drawCircle(canvas, x, y, element);
        drawElementName(canvas, x, y, element);

    }

    public static float getRadius() {
        return RADIUS * SCALE;
    }

    private static void drawCircle(Canvas canvas, float x, float y, Enums.Element element) {
        Paint paint = new Paint();
        paint.setAntiAlias(ANTI_ALIASING);
        paint.setColor(getColor(element));
        paint.setStrokeWidth(0);
        canvas.drawCircle(x, y, RADIUS * SCALE, paint);
    }

    private static int getColor(Enums.Element element) {
        Color color = new Color();
        switch (element) {
            case C:
                return Color.BLACK;
            case H:
                return Color.rgb(110, 207, 246);
            case O:
                return Color.rgb(0, 150, 255);
            case Ag:
                return Color.rgb(252, 203, 69);
            default:
                return Color.MAGENTA;
        }
    }

    private static void drawElementName(Canvas canvas, float x, float y, Enums.Element element) {
        Paint paint = new Paint();
        paint.setAntiAlias(ANTI_ALIASING);
        paint.setColor(Color.WHITE);
        paint.setTextSize(TEXT_SIZE * SCALE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(element.toString(), x, (float) (y + TEXT_SIZE * 0.6), paint);
    }

    private static void drawBondingRing(Canvas canvas, float x, float y) {
        Paint paint = new Paint();
        paint.setAntiAlias(ANTI_ALIASING);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(x, y, RING_SIZE * SCALE, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, (RING_SIZE - RING_WIDTH) * SCALE, paint);
    }
}
