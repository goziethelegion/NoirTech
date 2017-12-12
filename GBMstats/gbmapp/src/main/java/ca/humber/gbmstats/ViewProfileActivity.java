package ca.humber.gbmstats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class ViewProfileActivity extends AppCompatActivity {

    private EditText musername, mfirstname, mlastname, memail;
    private Button savebtn;
    private String usrnam, firname, lasname, emai, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        final Context context = this;
        mfirstname = (EditText) findViewById(R.id.fnameview);
        mlastname = (EditText) findViewById(R.id.lnameview);
        musername = (EditText) findViewById(R.id.editusrname);
        memail = (EditText) findViewById(R.id.editemail1);
        savebtn = (Button) findViewById(R.id.savebtn);

        User user = UserSessionManager.getInstance(this).getUser();

        musername.setText(user.getUsername());
        mfirstname.setText(user.getFname());
        mlastname.setText(user.getLname());
        memail.setText(user.getEmail());

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                firname = mfirstname.getText().toString();
                usrnam = musername.getText().toString();
                lasname = mlastname.getText().toString();
                emai = memail.getText().toString();

                if (usrnam.equals("")  || lasname.equals("") || firname.equals("") || emai.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getString(R.string.empty));
                    builder.setMessage(getString(R.string.enterinfo));
                    builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!emai.matches(emailPattern))
                    {
                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                        alertDialogBuilder2.setTitle(getString(R.string.emailinvalid));
                        alertDialogBuilder2.setMessage(getString(R.string.entervalidemail));
                        alertDialogBuilder2.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog2 = alertDialogBuilder2.create();
                        alertDialog2.show();
                    }
                    else
                    {
                        new SignupBackgroundTasks(ViewProfileActivity.this).execute();
                    }
                }
            }
        });
    }

    class SignupBackgroundTasks extends AsyncTask<String, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;



        //constructor
        public SignupBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        //Before execution, a dialog box is created to monitor the progress and the database url is assigned
        //to the variable name json_url.
        //onPreExecute method works with the Asyntask abstract class
        @Override
        protected void onPreExecute()
        {

            builder = new AlertDialog.Builder(ctx);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progressdialog, null);
            ((TextView) dialogView.findViewById(R.id.tprogressdialog)).setText(getString(R.string.wait));
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
            json_url = getString(R.string.website);
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                User user = UserSessionManager.getInstance(ctx).getUser();

                String data =
                        URLEncoder.encode("user_id", "UTF-8") + "=" +
                                URLEncoder.encode(String.valueOf(user.getUserID()), "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" +
                                URLEncoder.encode(usrnam, "UTF-8") + "&" +
                                URLEncoder.encode("firstname", "UTF-8") + "=" +
                                URLEncoder.encode(firname, "UTF-8") + "&" +
                                URLEncoder.encode("lastname", "UTF-8") + "=" +
                                URLEncoder.encode(lasname, "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" +
                                URLEncoder.encode(emai, "UTF-8");

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
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result)
        {
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
                        new ViewProfileActivity.SignupBackgroundTasks(ctx).execute();
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

                    if (code.equals("update_true"))
                    {
                        int castedUserID = Integer.valueOf(user_id);
                        User user = new User(castedUserID, firstname, username, email, lastname);
                        UserSessionManager.getInstance(ctx).userLogin(user);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle(getString(R.string.updated));
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else if (code.equals("update_false"))
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
                                new ViewProfileActivity.SignupBackgroundTasks(ctx).execute();
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
                                new ViewProfileActivity.SignupBackgroundTasks(ctx).execute();
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
                            new ViewProfileActivity.SignupBackgroundTasks(ctx).execute();
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

