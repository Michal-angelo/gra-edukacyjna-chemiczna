package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Constants;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.Enums;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Controller.GameController;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Controller.GameControllerCallback;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Model.AtomicWorkbench;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.R;
import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.View.GameView;

import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_DIFFICULTY;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_INSTANT_PLAY;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LEVEL_CREATOR;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LEVEL_LOADER;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LOADRANDOM;
import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_LOAD_LEVEL_ID;

public class PlayActivity extends AppCompatActivity implements GameControllerCallback {

    private GameController gameController;
    private Integer[] temp = {R.raw.ch4, R.raw.fecl3, R.raw.naoh, R.raw.cuoh2, R.raw.cu2o, R.raw.h2o};
    List<Integer> raws =  Arrays.asList(temp);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int levelId;
        Enums.Difficulty difficulty;

        if (DEV_INSTANT_PLAY) {
            levelId = DEV_LOAD_LEVEL_ID;
            difficulty = DEV_DIFFICULTY;
            levelId = 1;
        } else {
            levelId=1;
            difficulty= Enums.Difficulty.Easy;
        }
        if(Constants.counter < 5) {
            AtomicWorkbench atomicWorkbench = new AtomicWorkbench(this, generateLevel(), difficulty);
            GameView gameView = new GameView(this, atomicWorkbench, difficulty);
            this.gameController = new GameController(this, atomicWorkbench, gameView);
            gameController.setCallback(this);
            //System.out.println("setting content view to GC");
            setContentView(gameController);
            //System.out.println("failed?");
        }


    }
    @Override
    public void onBackPressed() {
        if (DEV_LEVEL_CREATOR || DEV_LEVEL_LOADER)
            gameController.backPressed();
        else if(DEV_LOADRANDOM)
            this.recreate();
        else
            super.onBackPressed();
    }


    public int generateLevel(){
        int rnd = new Random().nextInt(raws.size());
        int tempRaw = (int)raws.get(rnd);
        System.out.println(raws.size());
        System.out.println(Constants.counter  + " " + (raws.size() - 1) + " " + rnd);
        if(Constants.counter == 4){
            finishSimulation();
        }
        Constants.counter++;
        return tempRaw;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameController.onTouchEvent(event);
    }

    @Override
    public void GameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlayActivity.super.recreate();
            }
        });
        builder.setMessage("Pomyślnie ułożyłeś cząsteczkę: " + gameController.getName());
        AlertDialog dialog = builder.create();
        dialog.show();
        //this.recreate();
    }

    private void finishSimulation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), RepeatSimulationActivity.class);
                startActivity(intent);
                finish();
                Constants.counter = 0;
            }
        }).setMessage("Symulacja zakończona ");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void SaveButtonPressed() {
        //TODO: Ask if you really want to save that shit
    }
}
