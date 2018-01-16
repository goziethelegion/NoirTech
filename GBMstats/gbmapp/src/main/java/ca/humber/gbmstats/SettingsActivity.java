package ca.humber.gbmstats;
//GBMstats

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    //Options for the listview
    String[] options = {"View User Profile","CCTV Settings","Bluetooth Settings"};
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = (ListView)findViewById(R.id.listview1);
        //creating an adapter for the options string
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    //Toast to show user selection from the listview
                    Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + " " + getString(R.string.selected), Toast.LENGTH_SHORT).show();
                    switch (position){
                        //index 0 is assigned to the viewprofile activity
                        //so if the user selects the "view user profile"
                        //the viewprofile activity is displayed
                        case 0:
                            Intent viewprofile = new Intent(SettingsActivity.this, ViewProfileActivity.class);
                            startActivity(viewprofile);
                            break;
                        //index 1 is assigned to the about page
                        //if user selects "about" the about activity is displayed
                        case 1:
                            Intent about =new Intent(SettingsActivity.this, CCTVSettingsActivity.class);
                            startActivity(about);
                            break;
                        //index 2 is assigned to the bluetooth
                        //if the user selects "bluetooth settings" the bluetooth settings
                        //activity is displayed
                        case 2:
                            Intent bluetooth = new Intent(SettingsActivity.this, BluetoothSettingsActivity.class);
                            startActivity(bluetooth);
                            break;
                    }
                }
        });
    }
}

