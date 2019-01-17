package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.R;

import static pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common.DevModeSettings.DEV_INSTANT_PLAY;

public class IntroActivity extends AppCompatActivity {

    private static int TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEV_INSTANT_PLAY){
            Intent playIntent = new Intent(this, PlayActivity.class);
            startActivity(playIntent);
        }else {
            setContentView(R.layout.activity_intro); //xml sciagnac z mainmenuactivity
            TextView txtView = (TextView) findViewById(R.id.mainText);
            txtView.startAnimation(AnimationUtils.loadAnimation(IntroActivity.this, android.R.anim.slide_in_left));
            ImageView imgView = (ImageView) findViewById(R.id.imageView);
            imgView.startAnimation(AnimationUtils.loadAnimation(IntroActivity.this, android.R.anim.fade_in));
            // final ProgressDialog progressDialog = ProgressDialog.show(MainActivityView.this, "Please wait...", "Loading game", true);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent menuIntent = new Intent(IntroActivity.this, MainMenuActivity.class);
                    startActivity(menuIntent);
                    finish();
                }
            }, TIME_OUT);
        }
    }
}
