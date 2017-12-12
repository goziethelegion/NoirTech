package ca.humber.gbmstats;
//GBMstats

/**
 * Created by Chigozie on 11/7/2017.
 */
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class tab3scan extends Fragment{
    private Button bluetthbutn;

    //This would handle the bluetooth button to connect to the scanner
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3scan, container, false);

        return rootView;
    }
}
