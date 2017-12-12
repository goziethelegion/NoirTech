package ca.humber.gbmstats;
//GBMstats

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FlashActivity extends AppCompatActivity {

    private static int FLASH = 2800;
    ActionBar actionBar;
    private TextView te;
    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        actionBar = getSupportActionBar();
        actionBar.hide();
        te = (TextView)findViewById(R.id.te);
        im = (ImageView)findViewById(R.id.im);
        Animation manimation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        im.setVisibility(View.VISIBLE);
        te.startAnimation(manimation);
        im.startAnimation(manimation);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //String.Valueof(R.string.)
                Toast.makeText(getApplicationContext(), getString(R.string.welcome),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FlashActivity.this, GBMActivity.class);
                startActivity(intent);
                finish();
            }
        }, FLASH);
    }
}
