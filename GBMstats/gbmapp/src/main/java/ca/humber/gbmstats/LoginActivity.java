package ca.humber.gbmstats;
//GBMstats

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private Button loginbutton2;
    public EditText usrnametext, passwdtext;
    private TextView link;
    private String usrnametex, passwdtex;

    public void Loginmethod2() {
        usrnametext = (EditText) findViewById(R.id.usrnametext);

        loginbutton2 = (Button) findViewById(R.id.loginbutton2);
        loginbutton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                usrnametex = usrnametext.getText().toString();
                passwdtex = passwdtext.getText().toString();

                new LoginBackgroundTasks(LoginActivity.this).execute();
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

        usrnametext = (EditText) findViewById(R.id.usrnametext);
        passwdtext = (EditText) findViewById(R.id.passwdtext);
    }

    class LoginBackgroundTasks extends AsyncTask<String, Void, String>{
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public LoginBackgroundTasks(Context ctx){
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        @Override
        protected void onPreExecute(){
            builder = new AlertDialog.Builder(ctx);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progressdialog, null);
            ((TextView) dialogView.findViewById(R.id.tprogressdialog)).setText(getString(R.string.wait));
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
            json_url = getString(R.string.website1);
        }

        @Override
        protected String doInBackground(String... params){
            try
            {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("username", "UTF-8") + "=" +
                                URLEncoder.encode(usrnametex, "UTF-8") + "&" +
                                URLEncoder.encode("password", "UTF-8") + "=" +
                                URLEncoder.encode(passwdtex, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line + "\n");
                }
                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result){
            loginDialog.dismiss();
            if(TextUtils.isEmpty(result))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage(getString(R.string.errconnection));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        new LoginActivity.LoginBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject JO = jsonArray.getJSONObject(0);

                    //Whether login is successful or not, server will always return code and message
                    String message = JO.getString("message");
                    String code = JO.getString("code");

                    //Empty string just to avoid null pointer exception
                    String user_id = "";
                    String username = "";
                    String firstname = "";
                    String lastname = "";
                    String email = "";

                        try
                        {
                            //Server will only return these values if login is successful.
                            user_id = JO.getString("userid");
                            username = JO.getString("username");
                            firstname = JO.getString("firstname");
                            lastname = JO.getString("lastname");
                            email= JO.getString("email");
                        }
                        catch (Exception e)
                        {
                            // Server did not respond  with these values
                        }
                    if (code.equals("login_true"))
                    {
                        int castedUserID = Integer.valueOf(user_id);
                        User user = new User(castedUserID, firstname, username, email, lastname);
                        UserSessionManager.getInstance(ctx).userLogin(user);

                        Intent login2 = new Intent(LoginActivity.this, Menu2Activity.class);
                        login2.putExtra("name",usrnametex);
                        startActivity(login2);
                    }
                    else if (code.equals("login_false"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle(getString(R.string.err));
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                new LoginActivity.LoginBackgroundTasks(ctx).execute();
                            }
                        });
                        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                       AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle(getString(R.string.err));
                        builder.setMessage(getString(R.string.retry1));
                        builder.setCancelable(false);
                        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                new LoginActivity.LoginBackgroundTasks(ctx).execute();
                            }
                        });
                        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                catch (JSONException e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle(getString(R.string.apperror));
                    builder.setMessage(getString(R.string.retry1));
                    builder.setCancelable(true);
                    builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            new LoginActivity.LoginBackgroundTasks(ctx).execute();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }
}
