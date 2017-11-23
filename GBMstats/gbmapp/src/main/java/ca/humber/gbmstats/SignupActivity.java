package ca.humber.gbmstats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class SignupActivity extends AppCompatActivity {
    private Button nextbutton;
    private EditText lname, fname1, editemail, editusername, editpasswd, editconfirm;
    private String lnam, fnam, editemai, editusernam, editpassw, editconfir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final Context context = this;

        lname = (EditText) findViewById(R.id.lname);
        fname1 = (EditText) findViewById(R.id.fname1);
        editemail = (EditText) findViewById(R.id.editemail);
        editusername = (EditText) findViewById(R.id.editusername);
        editpasswd = (EditText) findViewById(R.id.editpasswd);
        editconfirm = (EditText) findViewById(R.id.editconfirm);

        //When button is clicked, get string values
            nextbutton = (Button) findViewById(R.id.nextbutton);
            nextbutton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    lnam = lname.getText().toString();
                    fnam = fname1.getText().toString();
                    editemai = editemail.getText().toString();
                    editconfir = editconfirm.getText().toString();
                    editpassw = editpasswd.getText().toString();
                    editusernam = editusername.getText().toString();

                    if(!editpassw.equals(editconfir))
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        alertDialogBuilder.setTitle("Invalid Password");
                        alertDialogBuilder.setMessage("Password and Confirm Password must match!");
                        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    else
                    {
                        if (!((editpassw.length() >= 6) && (editpassw.length() <= 12)))
                        {
                            AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);

                            alertDialogBuilder1.setTitle("Invalid Password");
                            alertDialogBuilder1.setMessage("Password must be greater than 5 characters and less than 12 characters!");
                            alertDialogBuilder1.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alertDialog1 = alertDialogBuilder1.create();
                            alertDialog1.show();
                        }
                        else
                        {
                            new SignupBackgroundTasks(SignupActivity.this).execute();
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
            ((TextView) dialogView.findViewById(R.id.tprogressdialog)).setText("Please wait...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
            json_url = "http://partscribdatabase.tech/gbmstats/registerstudent.php";
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

                String data =
                        URLEncoder.encode("username", "UTF-8") + "=" +
                                URLEncoder.encode(editusernam, "UTF-8") + "&" +
                                URLEncoder.encode("password", "UTF-8") + "=" +
                                URLEncoder.encode(editpassw, "UTF-8") + "&" +
                                URLEncoder.encode("firstname", "UTF-8") + "=" +
                                URLEncoder.encode(fnam, "UTF-8") + "&" +
                                URLEncoder.encode("lastname", "UTF-8") + "=" +
                                URLEncoder.encode(lnam, "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" +
                                URLEncoder.encode(editemai, "UTF-8");

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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                builder.setMessage("Connection Error.");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        new SignupBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                android.support.v7.app.AlertDialog alert = builder.create();
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

                    if (code.equals("reg_true"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Signed up");
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                editusername.setText("");
                                editpasswd.setText("");
                                fname1.setText("");
                                lname.setText("");
                                editemail.setText("");
                                editconfirm.setText("");
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else if (code.equals("reg_false"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Something went wrong!");
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                new SignupBackgroundTasks(ctx).execute();
                            }
                        });
                        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Unidentified Error Occurred");
                        builder.setMessage("Please try again.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                new SignupBackgroundTasks(ctx).execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                catch (JSONException e)
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Unknown Application Error Occurred");
                    builder.setMessage("Please try again.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            new SignupBackgroundTasks(ctx).execute();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }
}

