package ca.humber.gbmstats;
//GBMstats

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//class to save user information based on the fields
public class User {

    private String username, email, fname, lname;
    private int userID;

    public User(int userID, String fname, String username, String email, String lname) {
        this.userID = userID;
        this.fname = fname;
        this.username = username;
        this.email = email;
        this.lname = lname;
    }

    public int getUserID(){
        return userID;
    }

    public String getFname(){
        return fname;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getLname(){
        return lname;
    }
}
