package ca.humber.gbmstats;
//GBMstats

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GBMActivity extends AppCompatActivity {

    public Button loginbutton, signupbutton;

    public void Loginmethod() {
        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent login = new Intent(GBMActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }

    public void Signupmethod() {
        signupbutton = (Button) findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signup = new Intent(GBMActivity.this, SignupActivity.class);
                startActivity(signup);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gbm);
        Loginmethod();
        Signupmethod();

        //checks if the user is already logged in
        if(UserSessionManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, Menu2Activity.class));
        }
    }
}
