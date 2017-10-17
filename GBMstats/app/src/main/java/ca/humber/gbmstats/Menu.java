package ca.humber.gbmstats;

/**
 * Created by Ras on 2017-10-17.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    public Button camera, weather, fingerprint, settings;

    public void init() {
        but = (Button) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent boy = new Intent(GBMActivity.this, GBM2Activity.class);
                startActivity(boy);
            }
        });
    }
}
