package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Constants;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Enums;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.FloatPoint;

import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LEVEL_CREATOR;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LOADRANDOM;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_POPULATE_ATOMS;

public class AtomicWorkbench implements Enums {
    private final Context context;
    private Particle currentParticle = new Particle();
    private Particle targetParticle = new Particle();
    private String fileName;
    private int levelNumber;
    private boolean cutLineActive = false;
    private FloatPoint cutLineBeginning, cutLineEnd;
    private boolean connectLineActive = false;
    private FloatPoint connectLineBeginning, connectLineEnd;
    private boolean bondAdded = false;

    public AtomicWorkbench(Context context, int levelNumber, Difficulty difficulty) {
        this.context = context;
        this.levelNumber = levelNumber;
        targetParticle.name = "Not loaded";
        currentParticle.atoms = new LinkedList<>();
        targetParticle.mappedConnections = new ArrayList<>();
        cutLineBeginning = new FloatPoint(0, 0);
        cutLineEnd = new FloatPoint(0, 0);

        if (DEV_POPULATE_ATOMS) {
            debugPopulate();
        }
        if (DEV_LEVEL_CREATOR) {
            creatorPopulate();
        }

        if(DEV_LOADRANDOM){
            loadLevel(this.levelNumber);
        }
    }

    private void creatorPopulate() {
        fileName = "fecl";
        targetParticle.name = "Chlorek żelaza";

        currentParticle.rarestElement = Element.Fe;
        currentParticle.rarestAtomsCount = 1;
        newAtom(Element.Fe, 100, 500);
        newAtom(Element.Cl, 500, 100);
        newAtom(Element.Cl, 500, 400);
        newAtom(Element.Cl, 500, 600);
    }

    private void debugPopulate() {
        targetParticle.name = "Kwas bromowotestowy";
        newAtom(Enums.Element.Ag, 140, 550);
        newAtom(Enums.Element.Ag, 250, 375);
        newAtom(Enums.Element.Xe, 100, 200);
        newAtom(Enums.Element.He, 500, 375);

        makeBond(currentParticle.atoms.get(0), currentParticle.atoms.get(1));
        makeBond(currentParticle.atoms.get(1), currentParticle.atoms.get(2));
        makeBond(currentParticle.atoms.get(1), currentParticle.atoms.get(3));
        targetParticle.rarestElement = Enums.Element.Ag;
        targetParticle.rarestAtomsCount = 2;

        targetParticle.mappedConnections = new ArrayList<>();

        List temp0 = new ArrayList(), temp1 = new ArrayList(), temp2 = new ArrayList(), temp3 = new ArrayList();

        temp3.add(Enums.Element.Ag);
        temp3.add(Enums.Element.Ag);
        targetParticle.mappedConnections.add(temp3);

        temp1.add(Enums.Element.Ag);
        temp1.add(Enums.Element.He);
        targetParticle.mappedConnections.add(temp1);

        temp2.add(Enums.Element.Ag);
        temp2.add(Enums.Element.Xe);
        targetParticle.mappedConnections.add(temp2);

        temp0.add(Enums.Element.Ag);
        targetParticle.mappedConnections.add(temp0);
    }

    public void makeBond(Atom atomA, Atom atomB) {
        if(hasConnectedAtomsReachedLimitOfValenceConnections(atomA, atomB)) {
            atomA.makeBond(atomB.getId());
            atomB.makeBond(atomA.getId());
            bondAdded = true;
        } else {
            bondAdded = false;
        }
    }

    private boolean hasConnectedAtomsReachedLimitOfValenceConnections(Atom atomA, Atom atomB){
        return (atomA.getConnectedAtomsIds().size() < atomA.getValence() || atomB.getConnectedAtomsIds().size() < atomB.getValence());
    }

    public void destroyBond(Atom atomA, Atom atomB) {
        atomA.destroyBond(atomB.getId());
        atomB.destroyBond(atomA.getId());
    }

    public void cutByLine() {
        for (Bond bond : getBonds()) {
            if (bond != null) {
                if (bond.intersects(cutLineBeginning, cutLineEnd)) {
                    destroyBond(getAtomById(bond.getStartAtomId()), getAtomById(bond.getEndAtomId()));
                }
            }
        }
        cutLineActive = false;
    }

    public void newAtom(Enums.Element element, float x, float y) {
        currentParticle.atoms.add(new Atom(currentParticle.atoms.size(), element, x, y));
    }

    public List<Bond> getBonds() {
        List<Bond> bonds = new ArrayList<>();
        FloatPoint beginning;
        for (Atom atom : currentParticle.atoms) {
            beginning = new FloatPoint(atom.getX(), atom.getY());
            if (atom.getConnectedAtomsIds() != null) {
                for (int id : atom.getConnectedAtomsIds()) {
                    FloatPoint end = new FloatPoint(getAtomById(id).getX(), getAtomById(id).getY());
                    Bond bond = new Bond(beginning, end, atom.getId(), id);
                    bonds.add(bond);
                }
            }
        }
        return bonds;
    }

    public Atom getAtomById(int id) {
        for (Atom atom : currentParticle.atoms) {
            if (id == atom.getId()) {
                return atom;
            }
        }
        return null;
    }

    public void loadLevel(int androidResourceId) {
        ObjectInputStream input;

        try {
            input = new ObjectInputStream(context.getResources().openRawResource(androidResourceId));
            targetParticle = (Particle) input.readObject();
            currentParticle = new Particle(targetParticle);
            input.close();
            for (Atom atom : currentParticle.atoms) {
                atom.clearBonds();
            }
            for (Atom atom : currentParticle.atoms) {
                atom.setRandomPosition();
            }
            do{
                if(currentParticle.atoms.size() >= 8){break;}
                else{
                    generateRandomAtom();
                }
            }while(currentParticle.atoms.size() < 8);

            System.out.println("Particle loaded");
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomAtom(){
        newAtom(getRandomElement(), (float)new Random().nextInt(Constants.SCREEN_WIDTH), (float)new Random().nextInt(Constants.SCREEN_HEIGHT));
    }

    public Element getRandomElement(){
        return Constants.ELEMENT_VALUES.get(new Random().nextInt(Constants.ELEMENT_VALUES.size()));
    }


    public void saveLevel() {
        currentParticle.name = targetParticle.name;
        for (Atom atom : currentParticle.atoms) {
            if (atom.getType() == currentParticle.rarestElement) {
                currentParticle.mappedConnections = makeMap(currentParticle, atom);
                break;
            }
        }
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir(), "") + File.separator + fileName));
            out.writeObject(currentParticle);
            out.close();
            System.out.println("Level saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isComplete() {

        if (currentParticle.atoms.size() != targetParticle.atoms.size()) {
            System.out.println("Atoms count mismatch, found:" + currentParticle.atoms.size() + " expected:" + targetParticle.atoms.size());
            return false;
        }

        List<Atom> startPointAtoms = new ArrayList<>(0);
        for (Atom atom : currentParticle.atoms) {
            if (atom.getType() == targetParticle.rarestElement) {
                startPointAtoms.add(atom);
            }
        }
        if (startPointAtoms.size() != targetParticle.rarestAtomsCount)
            return false;


        for (Atom startingAtom : startPointAtoms) {
            List<List<Enums.Element>> particleMap = makeMap(currentParticle, startingAtom);
            if (particleMap.size() != targetParticle.mappedConnections.size()) {
                System.out.println("Bonds count mismatch, found:" + particleMap.size() + " expected:" + targetParticle.mappedConnections.size());
                return false;
            }
            System.out.println("Current:" + particleMap);
            System.out.println("Wanted:" + targetParticle.mappedConnections);

            for (List<Enums.Element> targetWay : targetParticle.mappedConnections) {
                for (List<Enums.Element> way : particleMap) {
                    if (way.equals(targetWay)) {
                        particleMap.remove(way);
                        break;
                    }
                }
            }
            if (particleMap.isEmpty()) {
                System.out.println("Current and target match");
                return true;
            }
        }
        return false;
    }

    private List<List<Element>> makeMap(Particle particle, Atom startAtom) {
        List<List<Enums.Element>> particleMap = new ArrayList<>();
        List<Atom> currentWay = new ArrayList<>();
        List<Atom> visited = new ArrayList<>();
        currentWay.add(startAtom);
        visited.add(startAtom);
        while (true) {
            boolean working = true;
            while (working) {
                working = false;
                for (int connectedAtomId : currentWay.get(currentWay.size() - 1).getConnectedAtomsIds()) {
                    if (!visited.contains(particle.atoms.get(connectedAtomId))) {
                        currentWay.add(particle.atoms.get(connectedAtomId));
                        visited.add(particle.atoms.get(connectedAtomId));
                        working = true;
                        break;
                    }
                }
            }

            List<Element> temp = new ArrayList<>();
            for (Atom a : currentWay) {
                temp.add(a.getType());
            }
            particleMap.add(temp);
            visited.add(currentWay.get(currentWay.size() - 1));
            currentWay.remove(currentWay.size() - 1);
            if (currentWay.isEmpty())
                return particleMap;
        }
    }

    public void connectLineConnectAtoms(FloatPoint connectLineBeginning, FloatPoint connectLineEnd){
        Atom startingAtom = null;
        Atom endingAtom = null;
        for(Atom atom : getAtomsList()) {
            if (atom.isTouched(atom.getHitBox(), (int) connectLineBeginning.x, (int) connectLineBeginning.y)) {
                startingAtom = atom;
                System.out.println("starting atom has been set: " + startingAtom.getType().toString());
                break;
            } else {
                System.out.println("no starting atom");
            }
        }
        for(Atom atom : getAtomsList()) {
            if (atom.isTouched(atom.getHitBox(), (int) connectLineEnd.x, (int) connectLineEnd.y)) {
                endingAtom = atom;
                break;
            } else {
                setConnectLineActive(false);
            }
        }
        if(startingAtom != null && endingAtom != null){
            makeBond(startingAtom, endingAtom);
            setConnectLineActive(false);
        }

        startingAtom = null;
        endingAtom = null;
    }

    public String getParticleName() {
        return targetParticle.name;
    }

    public List<Atom> getAtomsList() {
        return currentParticle.atoms;
    }

    public boolean isCutLineActive() {
        return cutLineActive;
    }

    public void setCutLineActive(boolean cutLineActive) {
        this.cutLineActive = cutLineActive;
    }

    public FloatPoint getCutLineBeginning() {
        return cutLineBeginning;
    }

    public void setCutLineBeginning(FloatPoint cutLineBeginning) {
        this.cutLineBeginning = cutLineBeginning;
    }

    public FloatPoint getCutLineEnd() {
        return cutLineEnd;
    }

    public void setCutLineEnd(FloatPoint cutLineEnd) {
        this.cutLineEnd = cutLineEnd;
    }

    public FloatPoint getConnectLineBeginning() {
        return connectLineBeginning;
    }

    public void setConnectLineBeginning(FloatPoint connectLineBeginning) {
        this.connectLineBeginning = connectLineBeginning;
    }

    public FloatPoint getConnectLineEnd() {
        return connectLineEnd;
    }

    public void setConnectLineEnd(FloatPoint connectLineEnd) {
        this.connectLineEnd = connectLineEnd;
    }

    public boolean isConnectLineActive() {
        return connectLineActive;
    }

    public void setConnectLineActive(boolean connectLineActive) {
        this.connectLineActive = connectLineActive;
    }

    public boolean isBondAdded() {
        return bondAdded;
    }
    public String getConnectLineBeginningOrEndAtomName(FloatPoint floatPoint){
        for(Atom wantedAtom : getAtomsList())
        {
            if(wantedAtom.isTouched(wantedAtom.getHitBox(), (int)floatPoint.x, (int)floatPoint.y))
                return wantedAtom.getType().toString();
        }
        return " ";
    }
}
