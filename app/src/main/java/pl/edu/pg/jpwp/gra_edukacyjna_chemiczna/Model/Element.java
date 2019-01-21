package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum Element {
    H(1, "Wodór", "Hydrogen", 1.008),
    He(2, "Hel", "Helium", 4.003),
    Na(11, "Sód", "sodium", 22.990),
    O(8, "Tlen", "Oxygen", 16),
    Cl(17, "Chlor", "Chlor", 35);

    private final int atomicNumber;
    private final String polishName;
    private final String englishName;
    private final double atomicMass;
    private ArrayList<Element> elementList = new ArrayList<>();

    Element(int atomicNumber, String polishName, String englishName, double atomicMass) {
        this.atomicMass = atomicMass;
        this.atomicNumber = atomicNumber;
        this.polishName = polishName;
        this.englishName = englishName;
        Holder.map.put(atomicNumber, this);
        elementList.add(this);
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public String getPolishName() {
        return polishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public double getAtomicMass() {
        return atomicMass;
    }

    private static class Holder {
        static Map<Integer, Element> map = new HashMap<Integer, Element>();
    }

    private ArrayList<Element> getElementList(){
        return new ArrayList<Element>(Holder.map.values());
    }
}
