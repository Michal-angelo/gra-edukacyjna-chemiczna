package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.List;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.FloatPoint;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.Atom;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.AtomicWorkbench;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.R;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.View.GameView;

import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LEVEL_CREATOR;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LEVEL_LOADER;

public class GameController extends SurfaceView implements SurfaceHolder.Callback {
    AtomicWorkbench atomicWorkbench;
    GameView gameView;
    private GameControllerCallback callback;
    private Atom draggingAtom;
    short clickCount = 0;
    long startTime;
    long duration;
    boolean isLongPressed = false;
    boolean isMoving = false;
    boolean isBusy = false;
    Vibrator vibrate;
    Point p1 = new Point();

    public GameController(Context context, AtomicWorkbench atomicWorkbench, GameView gameView) {
        super(context);
        getHolder().addCallback(this);
        this.atomicWorkbench = atomicWorkbench;
        this.gameView = gameView;
        vibrate = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {

        public void run() {
            if(!isMoving && !isBusy) {
                isLongPressed = true;
                vibrate.vibrate(250);
                //System.out.println("longpress worked");
                atomicWorkbench.setConnectLineBeginning(new FloatPoint(p1));
                atomicWorkbench.setConnectLineEnd(new FloatPoint(p1));
                atomicWorkbench.setConnectLineActive(true);
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        List<Atom> atomList = atomicWorkbench.getAtomsList();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDownBehavior(event, atomList);
                break;
            case MotionEvent.ACTION_MOVE:
                try {
                    actionMoveBehavior(event, atomList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongPressed);
                try {
                    actionUpBehavior();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gameView.draw(canvas);
    }

    public void backPressed() {
        System.out.println(getContext().getFilesDir().getAbsolutePath());
        if (DEV_LEVEL_CREATOR)
            atomicWorkbench.saveLevel();
        if (DEV_LEVEL_LOADER)
            atomicWorkbench.loadLevel(R.raw.cu2o);
        invalidate();
    }

    public void setCallback(GameControllerCallback callback) {
        this.callback = callback;
    }

    //region actionUpMethods
    private void actionUpBehavior() throws Exception {
        if(shouldFinishAtomMoving()) { finishAtomMoving(); }
        else if(shouldConnectAtoms()) { connectAtoms(); }
        else if(shouldCutAtoms()) { cutAtoms(); }
        else if(draggingAtom != null && isLongPressed){
            isLongPressed = false;
            isBusy = false;
        }
        else{
            throw new Exception("Unexpected error");
        }

        if (atomicWorkbench.isComplete()) {
            callback.GameOver();
        }
    }
    private boolean shouldFinishAtomMoving(){
        return (draggingAtom != null && !isLongPressed);
    }
    private void finishAtomMoving(){
        isMoving = false;
        draggingAtom = null;
        isBusy = false;
    }
    private boolean shouldConnectAtoms(){
        return (draggingAtom == null && isLongPressed && isBusy);
    }
    public void connectAtoms(){
        atomicWorkbench.connectLineConnectAtoms(atomicWorkbench.getConnectLineBeginning(), atomicWorkbench.getConnectLineEnd());
        if(atomicWorkbench.isBondAdded()) {
            Toast.makeText(getContext(), "Connected "
                            + atomicWorkbench.getConnectLineBeginningOrEndAtomName(atomicWorkbench.getConnectLineBeginning()) +
                            " with " + atomicWorkbench.getConnectLineBeginningOrEndAtomName(atomicWorkbench.getConnectLineEnd()),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            //TODO: Create method to get atom name that limited his limit of connections
            Toast.makeText(getContext(), "{atom} has reached the limit of connections", Toast.LENGTH_SHORT).show();
        }
        isLongPressed = false;
        isBusy = false;
    }
    public boolean shouldCutAtoms(){
        return (draggingAtom == null && !isLongPressed);
    }
    public void cutAtoms(){
        isBusy = false;
        atomicWorkbench.cutByLine();
    }
//endregion

    //region action move methods
    private void actionMoveBehavior(MotionEvent event, List<Atom> atomList) throws Exception {

        //ToDo: Handle collisions
        if(shouldMoveConnectLine()){
            moveConnectLine(event);
        }
        else if (shouldMoveAtom()) {
            moveAtom(event, atomList);
        }
        else if(shouldCutAtoms()) {
            actionMoveCutLineBehavior(event);
        }
        else if(shouldConnectAtoms()){
            actionMoveConnectAtoms(event);
        }
        else{
            throw new Exception("Unexpected error");
        }
    }

    private void actionMoveCutLineBehavior(MotionEvent event){
        isBusy = true;
        atomicWorkbench.setCutLineEnd(new FloatPoint(event.getX(), event.getY()));
    }

    private void actionMoveConnectAtoms(MotionEvent event){
        atomicWorkbench.setConnectLineEnd(new FloatPoint(event.getX(), event.getY()));
    }

    private boolean shouldMoveAtom(){
        return (!isLongPressed && draggingAtom != null);
    }

    private void moveAtom(MotionEvent event, List<Atom> atomList){
        isBusy = true;
        isMoving = true;
        draggingAtom.updatePosition((int) event.getX(), (int) event.getY());
        for (Atom atom : atomList) {
            if (atom == draggingAtom) {
                continue;
            }
        }
    }

    private boolean shouldMoveConnectLine(){
        return (isLongPressed && draggingAtom != null);
    }
    private void moveConnectLine(MotionEvent event){
        draggingAtom = null;
        atomicWorkbench.setConnectLineEnd(new FloatPoint(event.getX(), event.getY()));
        isBusy = true;
    }
//endregion

    //region actionDownMethod
    public void actionDownBehavior(MotionEvent event, List<Atom> atomList){

        setPointValue(event);
        atomActionSetManager(atomList, event);

        if (shouldDrawCutLine()) {
            drawCutLine();
        }
    }
    private void atomActionSetManager(List <Atom> atomList, MotionEvent event){

        for (Atom atom : atomList) {
            if (atom.isTouched(atom.getHitBox(), (int) event.getX(), (int) event.getY())) {
                handler.postDelayed(mLongPressed, 500);
                draggingAtom = atom;
                break;
            } else {
                draggingAtom = null;
            }
        }
    }
    private void setPointValue(MotionEvent event)
    {
        p1.x = (int)event.getX();
        p1.y = (int)event.getY();
    }

    private boolean shouldDrawCutLine(){
        return (draggingAtom == null && !isLongPressed);
    }

    private void drawCutLine(){
        atomicWorkbench.setCutLineBeginning(new FloatPoint(p1));
        atomicWorkbench.setCutLineEnd(new FloatPoint(p1));
        atomicWorkbench.setCutLineActive(true);
    }

    public String getName(){
        return atomicWorkbench.getParticleName();
    }
    //endregion
}
