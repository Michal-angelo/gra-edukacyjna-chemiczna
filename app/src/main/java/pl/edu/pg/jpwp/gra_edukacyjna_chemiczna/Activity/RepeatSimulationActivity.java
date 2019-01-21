package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.R;

public class RepeatSimulationActivity extends AppCompatActivity {
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_simulation);
        btn1 = (Button)findViewById(R.id.button11);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
