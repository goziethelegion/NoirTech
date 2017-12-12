package ca.humber.gbmstats;
//GBMstats

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassActivity extends AppCompatActivity {
private EditText email;
private String emai;
private Button buttonpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        final Context context = this;

        email = (EditText) findViewById(R.id.email);
        buttonpass = (Button) findViewById(R.id.buttonpass);

        buttonpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emai = email.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!emai.matches(emailPattern)) {
                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                    alertDialogBuilder2.setTitle(getString(R.string.emailinvalid));
                    alertDialogBuilder2.setMessage(getString(R.string.entervalidemail));
                    alertDialogBuilder2.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    alertDialog.setTitle(getString(R.string.emailsent));
                    alertDialog.setMessage(getString(R.string.checkmail));
                    alertDialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alertDialog1 = alertDialog.create();
                    alertDialog1.show();
                }
            }
        });
    }
}
