package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Enums;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.Atom;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.AtomicWorkbench;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.Bond;

import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Constants.SCREEN_HEIGHT;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Constants.SCREEN_WIDTH;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LEVEL_CREATOR;

public class GameView {
        private Context context;
        private AtomicWorkbench atomicWorkbench;
        private boolean drawAtomPicker;


        public GameView(Context context, AtomicWorkbench atomicWorkbench, Enums.Difficulty difficulty) {
            this.context = context;
            this.atomicWorkbench = atomicWorkbench;
            setDifficulty(difficulty);
        }

        public void draw(Canvas canvas) {

            drawBackground(canvas);

            drawBonds(canvas);
            drawAtoms(canvas);
            drawTopBar(canvas);
            if (atomicWorkbench.isCutLineActive()) {
                drawCutLine(canvas);
            }

            if(atomicWorkbench.isConnectLineActive()){
                drawConnect(canvas);
            }

            if (drawAtomPicker) {
                drawAtomPickMenu(canvas);
            }
            if (DEV_LEVEL_CREATOR) {
                drawLevelCreator(canvas);
            }
        }

        private void drawBackground(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
        }

        private void drawAtomPickMenu(Canvas canvas) {
            //TODO: drawAtomPickMenu
        }

        private void drawLevelCreator(Canvas canvas) {
            //TODO:drawLevelCreator
        }

        private void drawAtoms(Canvas canvas) {
            for (Atom atom : atomicWorkbench.getAtomsList()) {
                DrawableAtom.drawAtom(canvas, atom);
            }
        }

        private void drawBonds(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(10);
            for (Bond bond : atomicWorkbench.getBonds()) {
                canvas.drawLine(bond.getBeginningX(), bond.getBeginningY(), bond.getEndX(), bond.getEndY(), paint);
            }
        }

        private void setDifficulty(Enums.Difficulty difficulty) {
            drawAtomPicker = difficulty != Enums.Difficulty.Easy;
        }

        private void drawCutLine(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(12);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
            paint.setAntiAlias(true);
            Path baseline = new Path();
            baseline.moveTo(atomicWorkbench.getCutLineEnd().x, atomicWorkbench.getCutLineEnd().y);
            baseline.lineTo(atomicWorkbench.getCutLineBeginning().x, atomicWorkbench.getCutLineBeginning().y);
            canvas.drawPath(baseline, paint);
        }

        private void drawConnect(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(12);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
            paint.setAntiAlias(true);
            Path baseline = new Path();
            baseline.moveTo(atomicWorkbench.getConnectLineEnd().x, atomicWorkbench.getConnectLineEnd().y);
            baseline.lineTo(atomicWorkbench.getConnectLineBeginning().x, atomicWorkbench.getConnectLineBeginning().y);
            canvas.drawPath(baseline, paint);
        }

        private void drawTopBar(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            paint.setAlpha(125);
            canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT * (float) 0.1, paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(SCREEN_HEIGHT / 20);
            canvas.drawText(atomicWorkbench.getParticleName(), SCREEN_WIDTH * (float) 0.05, SCREEN_HEIGHT * (float) 0.07, paint);
        }

}
