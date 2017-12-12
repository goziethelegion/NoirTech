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

    String[] options = {"View User Profile","About","Bluetooth Settings"};
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = (ListView)findViewById(R.id.listview1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + getString(R.string.selected), Toast.LENGTH_SHORT).show();
                    switch (position){
                        case 0:
                            Intent viewprofile = new Intent(SettingsActivity.this, ViewProfileActivity.class);
                            startActivity(viewprofile);
                            break;
                        case 1:
                            Intent updateprofile =new Intent(SettingsActivity.this, AboutActivity.class);
                            startActivity(updateprofile);
                            break;
                        case 2:
                            Intent bluetooth = new Intent(SettingsActivity.this, BluetoothSettingsActivity.class);
                            startActivity(bluetooth);
                            break;
                    }
                }
        });
    }
}

