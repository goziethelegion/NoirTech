package ca.humber.gbmstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button loginbutton2;
    public EditText usrname;
    private TextView link;

    public void Loginmethod2() {
        usrname = (EditText) findViewById(R.id.usrnametext);

        loginbutton2 = (Button) findViewById(R.id.loginbutton2);
        loginbutton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent login2 = new Intent(LoginActivity.this, Menu2Activity.class);
                login2.putExtra("name",usrname.getText().toString());
                startActivity(login2);
            }
        });
    }

    public void Linkmethod() {
        link = (TextView) findViewById(R.id.link);
        link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent link2 = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(link2);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Loginmethod2();
        Linkmethod();
    }
}
