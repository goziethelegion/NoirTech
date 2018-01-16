package ca.humber.gbmstats;
//GBMstats

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothSettingsActivity extends AppCompatActivity {

    ListView listView2;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    CheckBox enableblth;
    Button enablefind;
    private BluetoothAdapter btAdapter;
    public String toastText2="";
    private BluetoothDevice remoteDevice;
    protected static final int DISCOVERY_REQUEST = 0;

    private BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            String toastText1 = "";

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    {
                        toastText1=getString(R.string.discenable);
                        Toast.makeText(BluetoothSettingsActivity.this, toastText1, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                    {
                        toastText1= getString(R.string.discdisable);
                        Toast.makeText(BluetoothSettingsActivity.this, toastText1, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BluetoothAdapter.SCAN_MODE_NONE:
                    {
                        toastText1=getString(R.string.discdisable2);
                        Toast.makeText(BluetoothSettingsActivity.this, toastText1, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BluetoothAdapter.STATE_CONNECTING:
                    {
                        toastText1=getString(R.string.connecting);
                        Toast.makeText(BluetoothSettingsActivity.this, toastText1, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case BluetoothAdapter.STATE_CONNECTED:
                    {
                        toastText1=getString(R.string.connected);
                        Toast.makeText(BluetoothSettingsActivity.this, toastText1, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    };

    private BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
        String stateExtra = BluetoothAdapter.EXTRA_STATE;
        int state = intent.getIntExtra(stateExtra, -1);
        String toastText = "";
            switch (state){
                case (BluetoothAdapter.STATE_TURNING_ON):
                {
                    toastText=getString(R.string.blthturnon);
                    Toast.makeText(BluetoothSettingsActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothAdapter.STATE_ON):
                {
                    toastText=getString(R.string.onblth);
                    Toast.makeText(BluetoothSettingsActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;
                }
                case (BluetoothAdapter.STATE_TURNING_OFF):
                {
                    toastText=getString(R.string.blthturnoff);
                    Toast.makeText(BluetoothSettingsActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothAdapter.STATE_OFF):
                {
                    toastText=getString(R.string.offblth);
                    Toast.makeText(BluetoothSettingsActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    setupUI();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_settings);

        enablefind = (Button)findViewById(R.id.enablefind);
        listView2 = (ListView)findViewById(R.id.listview2);
        enableblth = (CheckBox) findViewById(R.id.enableblth);
        mBTDevices = new ArrayList<>();

        setupUI();
    }

    private void setupUI() {
        enableblth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (enableblth.isChecked()) {
                    final SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(BluetoothSettingsActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    btAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (btAdapter == null) {
                        //Log.d(TAG, "Does not have Bluetooth capabilities");
                    }
                    if (btAdapter.isEnabled()) {
                        String address = btAdapter.getAddress();
                        String name = btAdapter.getName();
                        String statusText = name + " : " + address;
                        Toast.makeText(getBaseContext(), statusText, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.offblth), Toast.LENGTH_SHORT).show();
                    }
                    String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
                    String actionRequestEanble = BluetoothAdapter.ACTION_REQUEST_ENABLE;
                    IntentFilter filter = new IntentFilter(actionStateChanged);
                    registerReceiver(bluetoothState, filter);
                    startActivityForResult(new Intent(actionRequestEanble), 0);
                    editor.putBoolean("CheckBox_Value", enableblth.isChecked());
                    editor.commit();
                } else {
                    btAdapter.disable();
                }
            }
        });

        enablefind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
                    String discoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;

                    IntentFilter intentFilter = new IntentFilter(scanModeChanged);
                    registerReceiver(mBroadcastReceiver2, intentFilter);
                    startActivityForResult(new Intent(discoverable), DISCOVERY_REQUEST);
            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            if (requestCode == DISCOVERY_REQUEST){
                Toast.makeText(BluetoothSettingsActivity.this, getString(R.string.progdisc), Toast.LENGTH_SHORT).show();
                setupUI();
                findDevices();
            }
        }

        private void findDevices(){
            String lastUsedRemoteDevice = getLastUsedRemoteBTDevice();
            if (lastUsedRemoteDevice != null){
                toastText2 = "Checking for known paired devices, namely: " +lastUsedRemoteDevice;
                Toast.makeText(BluetoothSettingsActivity.this, toastText2, Toast.LENGTH_SHORT).show();
                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                for (BluetoothDevice pairedDevice : pairedDevices){
                    if (pairedDevice.getAddress().equals(lastUsedRemoteDevice)){
                        toastText2="Found Device: "+ pairedDevice.getName() + "@" + lastUsedRemoteDevice;
                        Toast.makeText(BluetoothSettingsActivity.this, toastText2, Toast.LENGTH_SHORT).show();
                        remoteDevice = pairedDevice;
                    }
                }
            }

            if (remoteDevice == null){
                toastText2=getString(R.string.start);
                Toast.makeText(BluetoothSettingsActivity.this, toastText2, Toast.LENGTH_SHORT).show();
                try {
                    String address = btAdapter.getAddress();
                    String name = btAdapter.getName();
                    btAdapter.startDiscovery();
                    if (btAdapter.startDiscovery()) {
                        checkBTPermissions();
                        toastText2 = getString(R.string.threadstart);
                        Toast.makeText(BluetoothSettingsActivity.this, toastText2, Toast.LENGTH_SHORT).show();
                        registerReceiver(broadcastReceiver3, new IntentFilter(BluetoothDevice.ACTION_FOUND));

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            toastText2 = "Discovered:" + remoteDeviceName;
            Toast.makeText(BluetoothSettingsActivity.this, toastText2, Toast.LENGTH_SHORT).show();
            mBTDevices.add(remoteDevice);
            mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
            listView2.setAdapter(mDeviceListAdapter);
            /*final String action = intent.getAction();
            Toast.makeText(BluetoothSettingsActivity.this, "ACTION FOUND",Toast.LENGTH_SHORT).show();

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                listView2.setAdapter(mDeviceListAdapter);
            }*/
        }
    };

        private String getLastUsedRemoteBTDevice(){
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            String result = prefs.getString("LAST_REMOTE_DEVICE_ADDRESS", null);
            return result;
        }

        /*find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String address = btAdapter.getAddress();
                    String name = btAdapter.getName();
                    String statusText = name + " : " + address;
                    if (btAdapter.isDiscovering()) {
                        btAdapter.cancelDiscovery();
                        checkBTPermissions();
                        btAdapter.startDiscovery();
                        IntentFilter discoverdevices = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(broadcastReceiver3, discoverdevices);
                    }

                    if (btAdapter.isDiscovering()) {

                        checkBTPermissions();
                        btAdapter.startDiscovery();
                        IntentFilter discoverdevices = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(broadcastReceiver3, discoverdevices);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/

    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
            else{
                Toast.makeText(BluetoothSettingsActivity.this, getString(R.string.noneed), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
