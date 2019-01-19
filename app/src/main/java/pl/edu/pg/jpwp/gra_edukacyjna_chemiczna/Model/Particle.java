package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model;

import java.io.Serializable;
import java.util.List;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Enums;

public class Particle implements Serializable, Cloneable {
    String name;
    List<Atom> atoms;
    List<List<Enums.Element>> mappedConnections;
    int rarestAtomsCount;
    Enums.Element rarestElement;

    Particle() {
    }

    Particle(Particle particle) {
        this.atoms = particle.atoms;
        this.rarestElement = particle.rarestElement;
        this.rarestAtomsCount = particle.rarestAtomsCount;
        this.name = particle.name;
    }
}