package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model;

import android.graphics.Rect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Enums;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.FloatPoint;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.View.DrawableAtom;

import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Constants.SCREEN_HEIGHT;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Constants.SCREEN_WIDTH;


public class Atom implements Enums, Serializable {
    private float x, y;
    private int id;
    private short valence = 4;//TODO:fix it
    private Enums.Element element;
    private List<Integer> connections;

    Atom(int id, Enums.Element element, float x, float y) {
        this.id = id;
        this.element = element;
        this.x = x;
        this.y = y;
        connections = new ArrayList<Integer>();
    }

    int getId() {
        return id;
    }

    public void setRandomPosition() {
        setX((float) Math.random() * (SCREEN_WIDTH - 2 * DrawableAtom.getRadius()) + DrawableAtom.getRadius());
        setY((float) Math.random() * (SCREEN_HEIGHT - 2 * DrawableAtom.getRadius()) + DrawableAtom.getRadius());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void updatePosition(float x, float y){this.x =x; this.y=y;}

    public FloatPoint getPosition(){
        return new FloatPoint(this.x, this.y);
    }

    public Enums.Element getType() {
        return element;
    }

    void makeBond(int id) { connections.add(id); }

    void destroyBond(int id) {
        connections.remove((Object)id);
    }

    void clearBonds() {
        connections.clear();
    }

    List<Integer> getConnectedAtomsIds (){
        return connections;
    }

    public boolean isTouched(Rect rect, int x, int y){
        return rect.contains(x, y);
    }

    //TODO:Future issue: change hitbox from rect to circle, fast implementation here
    //TODO:add DrawableAtom.SCALE
    public Rect getHitBox(){
        return new Rect((int) (this.getX() - DrawableAtom.getRadius()), (int) (this.getY() - DrawableAtom.getRadius()), (int) (this.getX() + DrawableAtom.getRadius()), (int) (this.getY() + DrawableAtom.getRadius()));
    }

    public short getValence() {
        return valence;
    }

    public void setValence(short valence) {
        this.valence = valence;
    }


}