package ca.humber.gbmstats;
//GBMstats

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import ca.humber.gbmstats.LoginActivity;

/**
 * Created by Chigozie on 12/2/2017.
 */

public class UserSessionManager {

    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_FNAME = "keyfirstname";
    private static final String KEY_LNAME = "keylname";

    private static UserSessionManager mInstance;
    private static Context mCtx;

    private UserSessionManager(Context context) {
        mCtx = context;
    }

    public static synchronized UserSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserSessionManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getUserID());
        editor.putString(KEY_FNAME, user.getFname());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_LNAME, user.getLname());
        editor.apply();
    }

    //this method checks whether user is already logged in or not
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method gives the logged in user
    public User getUser()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_LNAME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_FNAME, null)
        );
    }

    //this method will logout the user
    public void logout()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}

