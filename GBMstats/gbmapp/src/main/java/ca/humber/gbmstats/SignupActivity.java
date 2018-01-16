package ca.humber.gbmstats;
//GBMstats

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                    //convert the texts to strings
                    lnam = lname.getText().toString();
                    fnam = fname1.getText().toString();
                    editemai = editemail.getText().toString();
                    editconfir = editconfirm.getText().toString();
                    editpassw = editpasswd.getText().toString();
                    editusernam = editusername.getText().toString();

                    //validates if the text fields are empty
                    if (editusernam.equals("") && editpassw.equals("") && editconfir.equals("") &&
                            lnam.equals("") && fnam.equals("") && editemai.equals("")) {
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
                        else {
                        //validates if the password and the confirm password are the same
                            if (!editpassw.equals(editconfir)) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                                alertDialogBuilder.setTitle(getString(R.string.passinvalid));
                                alertDialogBuilder.setMessage(R.string.passmatch);
                                alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else {
                                //validates the length of the password the user entered
                                if (!((editpassw.length() >= 6) && (editpassw.length() <= 12))) {
                                    AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);

                                    alertDialogBuilder1.setTitle(getString(R.string.passinvalid));
                                    alertDialogBuilder1.setMessage(getString(R.string.passnumber));
                                    alertDialogBuilder1.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                    alertDialog1.show();
                                } else {
                                    //validates the email format
                                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                                    if (!editemai.matches(emailPattern)) {
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
                                    } else {
                                        new SignupBackgroundTasks(SignupActivity.this).execute();
                                    }
                                }
                            }
                        }
                }
            });
    }

    class SignupBackgroundTasks extends AsyncTask<String, Void, String>
    {
        String json_url;
        String JSON_STRING;
        //used to access an activity
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        //constructor
        public SignupBackgroundTasks(Context ctx)
        {
            //used to access an activity
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
            //progressdialog layout to monitor connection progress
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progressdialog, null);
            ((TextView) dialogView.findViewById(R.id.tprogressdialog)).setText(getString(R.string.wait));
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
            json_url = getString(R.string.website2);
        }

        //This is where the connection is established
        //Data is sent to server and response is awaited
        //Starts before the onPreExecute finishes its process
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

                //assigning the values user entered for verification
                //and saving the values in ASCII code format
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

                //write the user login for verification and then close
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

        //could be used to monitor progress while doInBackground method is still running
        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        //This is implemented after execution
        @Override
        protected void onPostExecute(String result)
        {
            loginDialog.dismiss();
            //Checks if there is an internet connection
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
                        new SignupBackgroundTasks(ctx).execute();
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
            //if there is a connection, then do the following
            else
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject JO = jsonArray.getJSONObject(0);

                    //Whether signup is successful or not, server will always return code and message
                    String message = JO.getString("message");
                    String code = JO.getString("code");

                    //executes if user sign up is successful
                    if (code.equals("reg_true"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle(getString(R.string.signup));
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                //after dialog is dismissed, the text fields are set to empty again
                                editusername.setText("");
                                editpasswd.setText("");
                                fname1.setText("");
                                lname.setText("");
                                editemail.setText("");
                                editconfirm.setText("");
                                //signup activity is ended upon successful signup
                                //and user is taken back to the previous activity(GBMstats activity)
                            finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    //executed if user sign up isn't successful
                    //that means something is wrong with text fields
                    else if (code.equals("reg_false"))
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
                                new SignupBackgroundTasks(ctx).execute();
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
                                new SignupBackgroundTasks(ctx).execute();
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
                catch (JSONException e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle(getString(R.string.err));
                    builder.setMessage(getString(R.string.retry1));
                    builder.setCancelable(true);
                    builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            new SignupBackgroundTasks(ctx).execute();
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
        }
    }
}

