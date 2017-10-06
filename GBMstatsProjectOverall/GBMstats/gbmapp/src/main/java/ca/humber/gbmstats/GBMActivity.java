package ca.humber.gbmstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GBMActivity extends AppCompatActivity {

    public Button but, but2;

    //login button
    public void init() {
        but = (Button) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent boy = new Intent(GBMActivity.this, GBM2Activity.class);
                startActivity(boy);
            }
        });
    }

    //signup button
    public void init1() {
        but2 = (Button) findViewById(R.id.but2);
        but2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent boy1 = new Intent(GBMActivity.this, GBM3Activity.class);
                startActivity(boy1);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gbm);
        init();
        init1();
    }
}
