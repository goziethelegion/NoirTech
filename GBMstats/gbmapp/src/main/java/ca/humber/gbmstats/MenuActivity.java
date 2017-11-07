package ca.humber.gbmstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private TextView textVie;
    private String usrname;
    private Button buttonsettings;

    public void Settingsmethod() {
        buttonsettings = (Button) findViewById(R.id.settingsbutton);
        buttonsettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settings = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(settings);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textVie = (TextView) findViewById(R.id.textView4);
        Intent login2 = this.getIntent();
        textVie.setVisibility(View.VISIBLE);

        usrname = login2.getStringExtra("name");
        textVie.setText("Welcome " + usrname);

        Settingsmethod();
    }
}
